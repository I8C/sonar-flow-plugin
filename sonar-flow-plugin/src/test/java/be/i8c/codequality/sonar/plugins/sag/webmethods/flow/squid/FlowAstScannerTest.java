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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.SimpleMetricVisitor;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.BranchPropertiesCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.CheckList;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.DisabledCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.EmptyFlowCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.EmptyMapCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.ExitCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.InterfaceCommentsCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.QualifiedNameCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.SavePipelineCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.TryCatchCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheck;

import com.sonar.sslr.api.Grammar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceFile;

public class FlowAstScannerTest {

  static final Logger logger = LoggerFactory.getLogger(FlowAstScannerTest.class);

  // File flowFile = new File("src/test/resources/WmPackage/ns/WmPackage/flows/myService/flow.xml");

  @Test
  public void debug() {
    List<Class<? extends FlowCheck>> checks = CheckList.getChecks();
    for (Class<? extends FlowCheck> check : checks) {
      logger.debug(check.toString());
    }
  }

  @Test
  public void gitFolderTest() {
    logger.debug("Scanning file");
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    FlowAstScanner.scanSingleFile(new File("src/test/resources/WmPackage/.git/someFile"), checks,
        metrics);

  }

  @Test
  public void scanFile() {
    logger.debug("Scanning file");
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    FlowAstScanner.scanSingleFile(
        new File(
            "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/flows/subProcess/flow.xml"),
        checks, metrics);

  }

  @Test
  public void tryCatchCheck() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new TryCatchCheck());

    // check valid flow
    String validFlowPath 
        = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkTryCatchValid/flow.xml";
    SourceFile sfCorrect = FlowAstScanner.scanSingleFile(new File(validFlowPath), checks, metrics);
    Set<CheckMessage> scmCorrect = sfCorrect.getCheckMessages();
    assertEquals(0, scmCorrect.size());

    metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    checks = new ArrayList<FlowCheck>();
    checks.add(new TryCatchCheck());

    // check invalid flow
    String invalidFlowPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkTryCatchInvalid/flow.xml";
    String expectedMessage = "Create try-catch sequence";

    SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidFlowPath), checks,
        metrics);
    List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(
        sfViolation.getCheckMessages());
    assertEquals(1, violationMessages.size());
    assertTrue("Returned check message not as expected",
        expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));

  }

  @Test
  public void savePipelineCheck() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new SavePipelineCheck());

    String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkSavePipelineInvalid/flow.xml";
    String expectedMessage = "Remove service pub.flow:savePipeline";

    SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), checks, metrics);
    List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(
        sfViolation.getCheckMessages());
    assertEquals(1, violationMessages.size());
    assertTrue("Returned check message not as expected",
        expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));

  }

  @Test
  public void disabledCheck() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new DisabledCheck());

    String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkDisabledInvalid/flow.xml";
    String expectedMessage = "Remove disabled code";

    SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), checks, metrics);
    List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(
        sfViolation.getCheckMessages());
    assertEquals(1, violationMessages.size());
    assertTrue("Returned check message not as expected",
        expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));
  }

  @Test
  public void interfaceCommentsCheck() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new InterfaceCommentsCheck());

    String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkInterfaceCommentsInvalid/flow.xml";

    FlowAstScanner.scanSingleFile(new File(invalidPath), checks, metrics);
    // Todo functional validation
  }

  @Test
  public void qualifiedNameCheck() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new QualifiedNameCheck());

    String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkQualityNameInvalid/flow.xml";

    FlowAstScanner.scanSingleFile(new File(invalidPath), checks, metrics);
    // Todo functional validation
  }

  @Test
  public void exitCheck() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new ExitCheck());

    String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkExitStepInvalid/flow.xml";

    SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), checks, metrics);
    List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(
        sfViolation.getCheckMessages());
    assertEquals(2, violationMessages.size());
    // Todo check both violation messages
  }

  @Test
  public void emptyMapCheck() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new EmptyMapCheck());

    // Invalid
    String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkEmptyMapInvalid/flow.xml";
    String expectedMessage 
        = "This map step in the flow is empty, create content or remove the map.";

    SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), checks, metrics);
    List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(
        sfViolation.getCheckMessages());
    assertEquals(1, violationMessages.size());
    assertTrue("Returned check message not as expected",
        expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));

    metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    checks = new ArrayList<FlowCheck>();
    checks.add(new EmptyMapCheck());

    // Valid
    invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkEmptyMapValid/flow.xml";

    sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), checks, metrics);
    violationMessages = new ArrayList<CheckMessage>(sfViolation.getCheckMessages());
    assertEquals(0, violationMessages.size());
  }

  @Test
  public void emptyFlowCheck() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new EmptyFlowCheck());

    String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkEmptyFlowInvalid/flow.xml";
    String expectedMessage 
        = "Service doesn't contain any flow steps. Remove service or add flow steps.";

    SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), checks, metrics);
    List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(
        sfViolation.getCheckMessages());
    assertEquals(1, violationMessages.size());
    assertTrue("Returned check message not as expected",
        expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));
  }

  @Test
  public void branchPropertiesCheck() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new BranchPropertiesCheck());

    final String expectedMessageA 
        = "Both switch and evaluate labels are defined in properties of BRANCH";
    final String expectedMessageB
        = "Evaluate labels must be true when no switch parameter is defined in BRANCH";

    // Check correct flow
    SourceFile sfCorrect = FlowAstScanner.scanSingleFile(new File(
        "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkBranchPropertiesValid/flow.xml"),
        checks, metrics);
    Set<CheckMessage> scmCorrect = sfCorrect.getCheckMessages();
    assertEquals(0, scmCorrect.size());

    metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    checks = new ArrayList<FlowCheck>();
    checks.add(new BranchPropertiesCheck());

    // Check violation flow A: both switch and evaluate labels defined
    SourceFile sfViolationA = FlowAstScanner.scanSingleFile(new File(
        "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkBranchPropertiesInvalidA/flow.xml"),
        checks, metrics);
    List<CheckMessage> violationAMessages = new ArrayList<CheckMessage>(
        sfViolationA.getCheckMessages());
    assertEquals(1, violationAMessages.size());
    assertTrue("Returned check message not as expected",
        expectedMessageA.equals(violationAMessages.get(0).getDefaultMessage()));

    metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    checks = new ArrayList<FlowCheck>();
    checks.add(new BranchPropertiesCheck());

    // Check violation flow B: neither switch nor evaluate labels defined
    SourceFile sfViolationB = FlowAstScanner.scanSingleFile(new File(
        "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkBranchPropertiesInvalidB/flow.xml"),
        checks, metrics);
    List<CheckMessage> violationBMessages = new ArrayList<CheckMessage>(
        sfViolationB.getCheckMessages());
    assertEquals(1, violationBMessages.size());
    assertTrue("Returned check message not as expected",
        expectedMessageB.equals(violationBMessages.get(0).getDefaultMessage()));
  }
}
