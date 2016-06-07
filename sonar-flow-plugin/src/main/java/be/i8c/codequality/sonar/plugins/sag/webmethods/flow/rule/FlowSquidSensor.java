/*
 * i8c
 * Copyright (C) 2016 i8c NV
 * mailto:contact AT i8c DOT be
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.rule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issuable.IssueBuilder;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.squidbridge.indexer.QueryByParent;
import org.sonar.squidbridge.indexer.QueryByType;

import com.google.common.collect.Lists;
import com.sonar.sslr.api.Grammar;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.FlowLanguage;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.FlowPlugin;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.CheckList;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.type.NodeCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.type.TopLevelCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowMetric;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.FlowAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.NodeAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowConfiguration;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitors.FlowLinesOfCodeVisitor;

public class FlowSquidSensor implements Sensor {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	private final Checks<SquidCheck<Grammar>> checks,nodeChecks;
	private final FileLinesContextFactory fileLinesContextFactory;
	private final FileSystem fileSystem;
	private final ResourcePerspectives resourcePerspectives;
	private final FilePredicate mainFilePredicates;
	private final PathResolver pathResolver;
	private final Settings settings;
	
	private SensorContext context;
	private AstScanner<Grammar> scanner;

	public FlowSquidSensor(Settings settings, CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory,
		                         FileSystem fileSystem, ResourcePerspectives resourcePerspectives, PathResolver pathResolver) {
			this.settings = settings;
			this.pathResolver = pathResolver;
		    this.checks = checkFactory
		      .<SquidCheck<Grammar>>create(CheckList.REPOSITORY_KEY)
		      .addAnnotatedChecks((Iterable)CheckList.getChecks(settings.getBoolean(FlowPlugin.IGNORE_TOPLEVEL_KEY),false));
		    this.nodeChecks = checkFactory
				      .<SquidCheck<Grammar>>create(CheckList.REPOSITORY_KEY)
				      .addAnnotatedChecks((Iterable)CheckList.getChecks(settings.getBoolean(FlowPlugin.IGNORE_TOPLEVEL_KEY), true));
		    this.fileLinesContextFactory = fileLinesContextFactory;
		    this.fileSystem = fileSystem;
		    this.resourcePerspectives = resourcePerspectives;
		    this.mainFilePredicates = fileSystem.predicates().and(
		      fileSystem.predicates().hasLanguage(FlowLanguage.KEY),
		      fileSystem.predicates().hasType(InputFile.Type.MAIN));
		  }

	@Override
	public boolean shouldExecuteOnProject(Project project) {
		return fileSystem.hasFiles(mainFilePredicates);
	}

	@Override
	public void analyse(Project project, SensorContext context) {
		this.context = context;

	    List<SquidAstVisitor<Grammar>> visitors = Lists.newArrayList(checks.all());
	    visitors.add(new FlowLinesOfCodeVisitor<Grammar>(FlowMetric.LINES_OF_CODE));
	    this.scanner = FlowAstScanner.create(createConfiguration(), visitors.toArray(new SquidAstVisitor[visitors.size()]));
	    FilePredicates p = fileSystem.predicates();
	    scanner.scanFiles(Lists.newArrayList(fileSystem.files(p.and(p.hasType(InputFile.Type.MAIN), p.hasLanguage(FlowLanguage.KEY), p.matchesPathPattern("**/flow.xml")))));

	    Collection<SourceCode> squidSourceFiles = scanner.getIndex().search(new QueryByType(SourceFile.class));
	    // Process sourceFiles
	    getInterfaceFiles(squidSourceFiles);
	    setTopLevelServices(squidSourceFiles);
	    save(squidSourceFiles);
	}

	private void getInterfaceFiles(Collection<SourceCode> squidSourceFiles) {
		// Scan node.ndf files
		List<SquidAstVisitor<Grammar>> visitors = Lists.newArrayList(nodeChecks.all());
		AstScanner<Grammar> scanner = NodeAstScanner.create(createConfiguration(), visitors.toArray(new SquidAstVisitor[visitors.size()]));
		FilePredicates p = fileSystem.predicates();
		scanner.scanFiles(Lists.newArrayList(fileSystem.files(p.and(p.hasType(InputFile.Type.MAIN), p.hasLanguage(FlowLanguage.KEY), p.matchesPathPattern("**/node.ndf")))));
	    Collection<SourceCode> nodeFiles = scanner.getIndex().search(new QueryByType(SourceFile.class));
	    logger.debug("*NODE* nodes found:" + nodeFiles.size() + " *");
	    for(SourceCode squidSourceFile : squidSourceFiles){
	    	for(SourceCode nodeFile : nodeFiles){
	    		if((new File(nodeFile.getKey())).getParent().equals((new File(squidSourceFile.getKey())).getParent())){
	    			squidSourceFile.addChild(nodeFile);
	    			String relativePath = pathResolver.relativePath(fileSystem.baseDir(), new java.io.File(nodeFile.getKey()));
	    			InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasRelativePath(relativePath));
	    			saveViolations(inputFile, (SourceFile) nodeFile);
	    		}
	    		
	    	}
	    }
	}

	private void setTopLevelServices(Collection<SourceCode> squidSourceFiles) {
		HashSet<String> dependencies = new HashSet<String>();
		for (SourceCode squidSourceFile : squidSourceFiles) {
			dependencies.addAll((ArrayList<String>) squidSourceFile.getData(FlowMetric.DEPENDENCIES));
		}
		for (SourceCode squidSourceFile : squidSourceFiles) {
			String relativePath = pathResolver.relativePath(fileSystem.baseDir(), new java.io.File(squidSourceFile.getKey()));
			String service = relativePath.replaceFirst(".*?/", "").replaceAll("/flow\\.xml", "").replaceAll("/", ".").replaceFirst("(.*)\\.(.*?)$", "$1:$2");
			logger.debug("NrOfDependencies:" + dependencies.size());
			if(dependencies.contains(service)){
				logger.debug("X--" + squidSourceFile.getKey() + " is not a top-level Service");
				squidSourceFile.add(FlowMetric.IS_TOP_LEVEL, 0);
			}else{
				logger.debug("X++" + squidSourceFile.getKey() + " is a top-level Service");
				squidSourceFile.add(FlowMetric.IS_TOP_LEVEL, 1);
			}
			logger.debug("+++relativePath: " + relativePath + " +++service: " + service + " +++topLevel: " + squidSourceFile.getInt(FlowMetric.IS_TOP_LEVEL));
		}
	}

	private FlowConfiguration createConfiguration() {
		return new FlowConfiguration(fileSystem.encoding());
	}

	private void save(Collection<SourceCode> squidSourceFiles) {
		for (SourceCode squidSourceFile : squidSourceFiles) {
			SourceFile squidFile = (SourceFile) squidSourceFile;
			
			String relativePath = pathResolver.relativePath(fileSystem.baseDir(), new java.io.File(squidFile.getKey()));
			InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasRelativePath(relativePath));
			
			saveClassComplexity(inputFile, squidFile);
			saveMeasures(inputFile, squidFile);
			saveViolations(inputFile, squidFile);
		}
	}

	private void saveMeasures(InputFile inputFile, SourceFile squidFile) {
		context.saveMeasure(inputFile, CoreMetrics.COMPLEXITY, squidFile.getDouble(FlowMetric.COMPLEXITY));
		context.saveMeasure(inputFile, CoreMetrics.NCLOC, squidFile.getDouble(FlowMetric.LINES_OF_CODE));
		context.saveMeasure(inputFile, CoreMetrics.COMMENT_LINES, squidFile.getDouble(FlowMetric.COMMENT_LINES));
	}

	private void saveClassComplexity(InputFile inputFile, SourceFile squidFile) {
		Collection<SourceCode> classes = scanner.getIndex().search(new QueryByParent(squidFile),
				new QueryByType(SourceClass.class));
		double complexityInClasses = 0;
		for (SourceCode squidClass : classes) {
			double classComplexity = squidClass.getDouble(FlowMetric.INVOKES);
			complexityInClasses += classComplexity;
		}
		context.saveMeasure(inputFile, CoreMetrics.COMPLEXITY_IN_CLASSES, complexityInClasses);
	}



	private void saveViolations(InputFile inputFile, SourceFile squidFile) {
		Collection<CheckMessage> messages = squidFile.getCheckMessages();
		if (messages != null) {
			for (CheckMessage message : messages) {
				SquidCheck<Grammar> c = (SquidCheck<Grammar>) message.getCheck();
				logger.debug("+++File: " + squidFile.getKey() + " - Checking message ToplevelService: " + squidFile.getInt(FlowMetric.IS_TOP_LEVEL) + " TopLevelCheck: " +  String.valueOf(c instanceof TopLevelCheck));
				if(squidFile.getInt(FlowMetric.IS_TOP_LEVEL)!=1 && c instanceof TopLevelCheck){
					logger.debug("+++Ignoring toplevelCheck: " + c.getKey() + " for file: " + squidFile.getKey());
				}else{
					logger.debug("+++ Message " + message.getDefaultMessage());
					logger.debug("+++ Message " + message.toString());
					RuleKey ruleKey;
					if(message.getCheck() instanceof NodeCheck){
						ruleKey = nodeChecks.ruleKey((SquidCheck<Grammar>) message.getCheck());
					}else
						ruleKey = checks.ruleKey((SquidCheck<Grammar>) message.getCheck());
					Issuable issuable = resourcePerspectives.as(Issuable.class, inputFile);
					if (issuable != null) {
						IssueBuilder issueBuilder = issuable.newIssueBuilder().ruleKey(ruleKey).line(message.getLine())
								.message(message.getText(Locale.ENGLISH));
	
						if (message.getCost() != null) {
							issueBuilder.effortToFix(message.getCost());
						}
	
						issuable.addIssue(issueBuilder.build());
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
