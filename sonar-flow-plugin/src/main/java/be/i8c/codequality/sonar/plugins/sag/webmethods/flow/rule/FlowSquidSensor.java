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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.FlowLanguage;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowMetric;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.settings.FlowLanguageProperties;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.FlowAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.NodeAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowConfiguration;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.CheckList;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheck;

import com.sonar.sslr.api.Grammar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.indexer.QueryByParent;
import org.sonar.squidbridge.indexer.QueryByType;

/**
 * The FlowSquidSensor is the Sensor used by the Flow plugin. It will run the @see FlowAstScanner on
 * the flow files.
 * 
 * @author DEWANST
 */
public class FlowSquidSensor implements Sensor {

  final Logger logger = LoggerFactory.getLogger(getClass());

  private final Checks<FlowCheck> flowChecks;
  private final Checks<FlowCheck> nodeChecks;
  private final FileSystem fileSystem;
  private final Configuration config;
  private final PathResolver pathResolver;

  /**
   * Main Constructor. Parameters are injected.
   * 
   * @param config
   *          Configuration
   * @param checkFactory
   *          Checkfactory
   * @param fileSystem
   *          Filesystem
   * @param pathResolver
   *          Pathresolver
   */
  public FlowSquidSensor(Configuration config, CheckFactory checkFactory,
      FileSystem fileSystem, PathResolver pathResolver) {
    logger.debug("** FlowSquidSenser constructor");
    this.fileSystem = fileSystem;
    this.config = config;
    this.pathResolver = pathResolver;
    this.flowChecks = checkFactory.<FlowCheck>create(FlowRulesDefinition.REPO_KEY)
        .addAnnotatedChecks(CheckList.getFlowChecks().toArray())
        .addAnnotatedChecks(config.getBoolean(
            FlowLanguageProperties.IGNORE_TOPLEVEL_KEY).get() ? null
                : CheckList.getTopLevelChecks().toArray());
    this.nodeChecks = checkFactory.<FlowCheck>create(FlowRulesDefinition.REPO_KEY)
        .addAnnotatedChecks(CheckList.getNodeChecks().toArray());
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("Scans flow files");
    descriptor.onlyOnLanguage("flow");
    descriptor.createIssuesForRuleRepositories(FlowRulesDefinition.REPO_KEY);

  }

  @SuppressWarnings("deprecation")
  @Override
  public void execute(SensorContext context) {
    logger.debug("FlowSquidSensor analysis called");
    AstScanner<Grammar> flowScanner = FlowAstScanner.create(createConfiguration(), flowChecks.all(),
        null);
    logger.debug("Scanning Flow Files with " + flowChecks.all().size() + " checks active");
    FileSystem fs = context.fileSystem();
    Iterable<InputFile> flowFiles = fs.inputFiles(
        fs.predicates().and(fs.predicates().hasLanguage(FlowLanguage.KEY),
            fs.predicates().matchesPathPatterns(FlowLanguage.getFlowFilePatterns())));
    ArrayList<File> files = new ArrayList<File>();
    flowFiles.forEach(file -> files.add(file.file()));
    try {
      flowScanner.scanFiles(files);
    } catch (Exception e) {
      if (config.getBoolean(FlowLanguageProperties.FAIL_ON_SCANERROR).get()) {
        throw e;
      } else {
        logger.error("** * Exception while scanning file, skipping.", e);
      }
    }
    Collection<SourceCode> squidSourceFiles = flowScanner.getIndex()
        .search(new QueryByType(SourceFile.class));
    logger.debug("** Done Scanning");
    // Process sourceFiles
    logger.debug("** Getting Interface Files");
    getInterfaceFiles(squidSourceFiles, context);
    logger.debug("** Setting Top Level Services");
    setTopLevelServices(squidSourceFiles);
    logger.debug("** Saving Source Files");
    save(context, flowScanner, squidSourceFiles);

  }

  @SuppressWarnings("deprecation")
  private void getInterfaceFiles(Collection<SourceCode> squidSourceFiles, SensorContext context) {
    // Scan node.ndf files
    AstScanner<Grammar> scanner = NodeAstScanner.create(createConfiguration(), nodeChecks.all(),
        null);
    logger.debug("Scanning Interface Files with " + nodeChecks.all().size() + " checks active");
    FileSystem fs = context.fileSystem();
    Iterable<InputFile> nodeFiles = fs
        .inputFiles(fs.predicates().matchesPathPatterns(FlowLanguage.getNodeFilePatterns()));
    for (InputFile nodeFile : nodeFiles) {
      try {
        logger.debug("** * Scanning File: " + nodeFile.toString());
        scanner.scanFile(nodeFile.file());
      } catch (Exception e) {
        if (config.getBoolean(FlowLanguageProperties.FAIL_ON_SCANERROR).get()) {
          throw e;
        } else {
          logger.error("** * Exception while scanning file, skipping.", e);
        }
      }
    }
    Collection<SourceCode> nodeSources = scanner.getIndex()
        .search(new QueryByType(SourceFile.class));
    logger.debug("*NODE* nodes found:" + nodeSources.size() + " *");
    for (SourceCode squidSourceFile : squidSourceFiles) {
      for (SourceCode nodeSource : nodeSources) {
        if ((new File(nodeSource.getKey())).getParent()
            .equals((new File(squidSourceFile.getKey())).getParent())) {
          squidSourceFile.addChild(nodeSource);
          String relativePath = pathResolver.relativePath(fileSystem.baseDir(),
              new java.io.File(nodeSource.getKey()));
          InputFile inputFile = fileSystem
              .inputFile(fileSystem.predicates().hasRelativePath(relativePath));
          saveViolations(context, inputFile, (SourceFile) nodeSource);
        }

      }
    }
  }

  private void setTopLevelServices(Collection<SourceCode> squidSourceFiles) {
    HashSet<String> dependencies = new HashSet<String>();
    for (SourceCode squidSourceFile : squidSourceFiles) {
      @SuppressWarnings("unchecked")
      ArrayList<String> deps = (ArrayList<String>) squidSourceFile.getData(FlowMetric.DEPENDENCIES);
      if (deps != null) {
        dependencies.addAll(deps);
      }
    }
    for (SourceCode squidSourceFile : squidSourceFiles) {
      String relativePath = pathResolver.relativePath(fileSystem.baseDir(),
          new java.io.File(squidSourceFile.getKey()));
      String service = relativePath.replaceFirst(".*?/", "").replaceAll("/flow\\.xml", "")
          .replaceAll("/", ".").replaceFirst("(.*)\\.(.*?)$", "$1:$2");
      logger.debug("NrOfDependencies:" + dependencies.size());
      if (dependencies.contains(service)) {
        logger.debug("X--" + squidSourceFile.getKey() + " is not a top-level Service");
        squidSourceFile.add(FlowMetric.IS_TOP_LEVEL, 0);
      } else {
        logger.debug("X++" + squidSourceFile.getKey() + " is a top-level Service");
        squidSourceFile.add(FlowMetric.IS_TOP_LEVEL, 1);
      }
      logger.debug("+++relativePath: " + relativePath + " +++service: " + service + " +++topLevel: "
          + squidSourceFile.getInt(FlowMetric.IS_TOP_LEVEL));
    }
  }

  private FlowConfiguration createConfiguration() {
    return new FlowConfiguration(fileSystem.encoding());
  }

  private void save(SensorContext context,AstScanner<Grammar> flowScanner,
      Collection<SourceCode> squidSourceFiles) {
    for (SourceCode squidSourceFile : squidSourceFiles) {
      SourceFile squidFile = (SourceFile) squidSourceFile;

      String relativePath = pathResolver.relativePath(fileSystem.baseDir(),
          new java.io.File(squidFile.getKey()));
      InputFile inputFile = fileSystem
          .inputFile(fileSystem.predicates().hasRelativePath(relativePath));

      saveClassComplexity(context, flowScanner, inputFile, squidFile);
      saveMeasures(context, inputFile, squidFile);
      saveViolations(context, inputFile, squidFile);
    }
  }

  private void saveMeasures(SensorContext context, InputFile inputFile, SourceFile squidFile) {
    context.<Integer>newMeasure().on(inputFile).forMetric(CoreMetrics.COMPLEXITY)
        .withValue(squidFile.getInt(FlowMetric.COMPLEXITY)).save();
    context.<Integer>newMeasure().on(inputFile).forMetric(CoreMetrics.NCLOC)
        .withValue(squidFile.getInt(FlowMetric.LINES_OF_CODE)).save();
    context.<Integer>newMeasure().on(inputFile).forMetric(CoreMetrics.COMMENT_LINES)
        .withValue(squidFile.getInt(FlowMetric.COMMENT_LINES)).save();
  }

  private void saveClassComplexity(SensorContext context, AstScanner<Grammar> flowScanner,
      InputFile inputFile, SourceFile squidFile) {
    Collection<SourceCode> classes = flowScanner.getIndex().search(new QueryByParent(squidFile),
        new QueryByType(SourceClass.class));
    Integer complexityInClasses = 0;
    for (SourceCode squidClass : classes) {
      int classComplexity = squidClass.getInt(FlowMetric.INVOKES);
      complexityInClasses += classComplexity;
    }
    context.<Integer>newMeasure().on(inputFile).forMetric(CoreMetrics.COMPLEXITY_IN_CLASSES)
        .withValue(complexityInClasses).save();
  }

  private void saveViolations(SensorContext context, InputFile inputFile, SourceFile squidFile) {
    Collection<CheckMessage> messages = squidFile.getCheckMessages();
    logger.debug("+++Nr of violations found: " + messages.size());
    if (messages != null) {
      for (CheckMessage message : messages) {
        Object c = message.getCheck();
        if (c instanceof FlowCheck) {
          FlowCheck fc = (FlowCheck) c;
          if (squidFile.getInt(FlowMetric.IS_TOP_LEVEL) != 1 && fc.isTopLevelCheck()) {
            logger.debug("+++Ignoring toplevelCheck for file: " + squidFile.getKey());
          } else {
            RuleKey ruleKey;
            if (fc.isNodeCheck()) {
              ruleKey = nodeChecks.ruleKey(fc);
            } else {
              ruleKey = flowChecks.ruleKey(fc);
            }
            FlowIssue.create(context, ruleKey, message.getCost()).setPrimaryLocation(inputFile,
                message).save();
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
