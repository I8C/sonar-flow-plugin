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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.config.Configuration;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.rule.RuleKey;
import org.sonar.squidbridge.ProgressReport;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.impl.Parser;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowFileMetrics;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowMetrics;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.rule.FlowRulesDefinition;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.settings.FlowLanguageProperties;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowParser;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.NodeParser;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.utils.FlowUtils;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowVisitorContext;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.Issue;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.NodeCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.NodeVisitorContext;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.CheckList;

/**
 * The FlowSquidSensor is the Sensor used by the Flow plugin. It will run the @see FlowAstScanner on
 * the flow files.
 * 
 * @author DEWANST
 */
public class FlowSquidSensor implements Sensor {

  final Logger logger = LoggerFactory.getLogger(getClass());

  private final Checks<FlowCheck> flowChecks;
  private final Checks<NodeCheck> nodeChecks;
  private final Configuration config;

  private final FlowSquidSensorContext fssContext;

  /**
   * Main Constructor. Parameters are injected.
   * 
   * @param config
   *          Configuration
   * @param checkFactory
   *          Checkfactory
   */
  public FlowSquidSensor(Configuration config, CheckFactory checkFactory) {
    logger.debug("** FlowSquidSenser constructor");
    this.config = config;
    this.flowChecks = checkFactory.<FlowCheck>create(FlowRulesDefinition.REPO_KEY)
        .addAnnotatedChecks(CheckList.getFlowChecks().toArray()).addAnnotatedChecks(
            config.getBoolean(FlowLanguageProperties.IGNORE_TOPLEVEL_KEY).get() ? null
                : CheckList.getTopLevelChecks().toArray());
    this.nodeChecks = checkFactory.<NodeCheck>create(FlowRulesDefinition.REPO_KEY)
        .addAnnotatedChecks(CheckList.getNodeChecks().toArray());
    fssContext = new FlowSquidSensorContext();
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("Scans flow files");
    descriptor.onlyOnLanguage("flow");
    descriptor.createIssuesForRuleRepositories(FlowRulesDefinition.REPO_KEY);

  }

  @Override
  public void execute(SensorContext context) {
    logger.debug("Scanning Flow Files with " + flowChecks.all().size() + " checks active");
    FileSystem fileSystem = context.fileSystem();
    FilePredicates predicates = fileSystem.predicates();

    FilePredicate flowPredicate = predicates.and(predicates.hasLanguage(FlowLanguage.KEY),
        predicates.matchesPathPatterns(FlowLanguage.getFlowFilePatterns()));
    FilePredicate nodePredicate = predicates.and(predicates.hasLanguage(FlowLanguage.KEY),
        predicates.matchesPathPatterns(FlowLanguage.getNodeFilePatterns()));

    List<InputFile> flowFiles = new ArrayList<>();
    fileSystem.inputFiles(flowPredicate).forEach(flowFiles::add);
    List<InputFile> nodeFiles = new ArrayList<>();
    fileSystem.inputFiles(nodePredicate).forEach(flowFiles::add);

    ProgressReport flowProgressReport = new ProgressReport("Report about progress of Flow analyzer",
        TimeUnit.SECONDS.toMillis(10));
    ProgressReport nodeProgressReport = new ProgressReport("Report about progress of Node analyzer",
        TimeUnit.SECONDS.toMillis(10));
    List<String> ffNames = flowFiles.stream().map(InputFile::toString).collect(Collectors.toList());
    List<String> nfNames = nodeFiles.stream().map(InputFile::toString).collect(Collectors.toList());

    flowProgressReport.start(ffNames);
    nodeProgressReport.start(nfNames);
    try {
      // PROCESS FLOW FIILES
      Charset charset = fileSystem.encoding();
      for (InputFile flowFile : flowFiles) {
        analyseFlowFile(context, charset, flowFile);
        flowProgressReport.nextFile();
      }
      // PROCESS NODE FIILES
      for (InputFile nodeFile : nodeFiles) {
        analyseNodeFile(context, charset, nodeFile);
        nodeProgressReport.nextFile();
      }
      // PROCESS AGGREGATED DATA
      setTopLevelServicesAndSaveIssues(context, flowFiles);
    } catch (Exception e) {
      if (config.getBoolean(FlowLanguageProperties.FAIL_ON_SCANERROR).get()) {
        throw e;
      } else {
        logger.error("*** Exception while scanning file, skipping.", e);
      }
    } finally {
      flowProgressReport.stop();
      nodeProgressReport.stop();
    }

  }

  /**
   * Analyzes the file by parsing it into an AST Tree and passing that tree to the
   * 
   * @see FlowFileMetrics to identify metrics. Finally the tree is checked for issues.
   * 
   * @param context
   * @param charset
   * @param inputFile
   * @param dependencies
   */
  private void analyseFlowFile(SensorContext context, Charset charset, InputFile inputFile) {
    String fileContent;
    try {
      fileContent = inputFile.contents();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read " + inputFile, e);
    }

    Parser<Grammar> parser = FlowParser.create(charset);
    FlowVisitorContext visitorContext;
    try {
      visitorContext = new FlowVisitorContext(fileContent, parser.parse(fileContent), inputFile);
      saveFlowMeasures(context, fssContext, inputFile, visitorContext);
      for (FlowCheck check : flowChecks.all()) {
          saveFlowIssues(context, check, check.scanFileForIssues(visitorContext), inputFile,
            flowChecks);
      }
    } catch (RecognitionException e) {
      logger.error("Unable to parse file: {}", inputFile);
      logger.error(e.getMessage());
    }

    

  }

  /**
   * Analyzes the node file by parsing it into an AST Tree and passing that tree to the
   * 
   * @see FlowFileMetrics to identify metrics. Finally the tree is checked for issues.
   * 
   * @param context
   * @param charset
   * @param inputFile
   */
  private void analyseNodeFile(SensorContext context, Charset charset, InputFile inputFile) {
    String fileContent;
    try {
      fileContent = inputFile.contents();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read " + inputFile, e);
    }

    Parser<Grammar> parser = NodeParser.create(charset);
    NodeVisitorContext visitorContext;
    try {
      visitorContext = new NodeVisitorContext(fileContent, parser.parse(fileContent), inputFile);
      saveNodeMeasures(context, fssContext, inputFile, visitorContext);
      for (NodeCheck check : nodeChecks.all()) {
        saveNodeIssues(context, check, check.scanFileForIssues(visitorContext), inputFile,
            nodeChecks);
      }
    } catch (RecognitionException e) {
      logger.error("Unable to parse file: {}", inputFile);
      logger.error(e.getMessage());
    }

  }

  private void saveFlowIssues(SensorContext context, FlowCheck check, List<Issue> issues,
      InputFile inputFile, Checks<FlowCheck> checks) {
    for (Issue flowIssue : issues) {
      RuleKey ruleKey = checks.ruleKey(check);
      NewIssue issue = context.newIssue();
      NewIssueLocation location = issue.newLocation().on(inputFile).message(flowIssue.message());
      Integer line = flowIssue.line();
      if (line != null) {
        location.at(inputFile.selectLine(line));
      }
      Double cost = flowIssue.cost();
      if (cost != null) {
        issue.gap(cost);
      }
      if(!check.isTopLevel()) {
        fssContext.addTopLevelIssue(inputFile.uri(), issue.at(location).forRule(ruleKey));
      }else {
        issue.at(location).forRule(ruleKey).save();
      }
    }

  }
  
  private void saveNodeIssues(SensorContext context, NodeCheck check, List<Issue> issues,
      InputFile inputFile, Checks<NodeCheck> checks) {
    for (Issue flowIssue : issues) {
      RuleKey ruleKey = checks.ruleKey(check);
      NewIssue issue = context.newIssue();
      NewIssueLocation location = issue.newLocation().on(inputFile).message(flowIssue.message());
      Integer line = flowIssue.line();
      if (line != null) {
        location.at(inputFile.selectLine(line));
      }
      Double cost = flowIssue.cost();
      if (cost != null) {
        issue.gap(cost);
      }
      issue.at(location).forRule(ruleKey).save();
    }

  }

  private void saveFlowMeasures(SensorContext context, FlowSquidSensorContext fssContext,
      InputFile inputFile, FlowVisitorContext visitorContext) {
    FlowFileMetrics metrics = new FlowFileMetrics(visitorContext);
    context.<Integer>newMeasure().on(inputFile).forMetric(CoreMetrics.COMPLEXITY)
        .withValue(metrics.getComplexity()).save();
    context.<Integer>newMeasure().on(inputFile).forMetric(CoreMetrics.NCLOC)
        .withValue(metrics.linesOfCode().size()).save();
    context.<Integer>newMeasure().on(inputFile).forMetric(CoreMetrics.COMMENT_LINES)
        .withValue(metrics.commentLines().size()).save();
    context.<Serializable>newMeasure().on(inputFile).forMetric(FlowMetrics.FLOW_DEPENDENCIES)
        .withValue(formatDepenencies(metrics.dependencies())).save();
    fssContext.addDependencies(metrics.dependencies().values());
  }

  private void saveNodeMeasures(SensorContext context, FlowSquidSensorContext fssContext,
      InputFile inputFile, NodeVisitorContext visitorContext) {
    // NodeFileMetrics metrics = new NodeFileMetrics(visitorContext);
    // TODO: Add node metrics
  }

  private String formatDepenencies(Map<Integer, String> dependencies) {
    HashSet<String> deps = new HashSet<String>();
    for (String dep : dependencies.values()) {
      deps.add(dep);
    }
    for (String dep : dependencies.values()) {
      deps.add(dep);
    }
    return String.join(",", deps);
  }

  private void setTopLevelServicesAndSaveIssues(SensorContext context, List<InputFile> flowFiles) {
    for (InputFile flowFile : flowFiles) {
      URI flowURI = flowFile.uri();
      String serviceName = FlowUtils.getQualifiedName(flowURI);
      HashSet<String> dependencies = fssContext.getDependencies();
      logger.debug("NrOfDependencies:" + dependencies.size());
      if (dependencies.contains(serviceName)) {
        context.<Boolean>newMeasure().on(flowFile).forMetric(FlowMetrics.IS_TOPLEVEL)
            .withValue(false).save();
      } else {
        context.<Boolean>newMeasure().on(flowFile).forMetric(FlowMetrics.IS_TOPLEVEL)
            .withValue(true).save();
        if(fssContext.getTopLevelIssues() != null && fssContext.getTopLevelIssues().containsKey(flowURI)) {
          for(NewIssue issue : fssContext.getTopLevelIssues().get(flowURI)) {
            issue.save();
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
