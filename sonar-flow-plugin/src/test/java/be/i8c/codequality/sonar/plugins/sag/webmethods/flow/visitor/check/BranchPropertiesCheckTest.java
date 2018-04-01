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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.FlowAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.SimpleMetricVisitor;
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


public class BranchPropertiesCheckTest {

  static final Logger logger = LoggerFactory.getLogger(BranchPropertiesCheckTest.class);

  @Test
  public void branchPropertiesCheckValid() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new BranchPropertiesCheck());

    // Check correct flow
    SourceFile sfCorrect = FlowAstScanner
        .scanSingleFile(new File("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
            + "/pub/checkBranchPropertiesValid/flow.xml"), checks, metrics);
    Set<CheckMessage> scmCorrect = sfCorrect.getCheckMessages();
    assertEquals(0, scmCorrect.size());
  }

  @Test
  public void branchPropertiesCheckInvalidA() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new BranchPropertiesCheck());

    final String expectedMessageA 
        = "Both switch and evaluate labels are defined in properties of BRANCH";

    // Check violation flow A: both switch and evaluate labels defined
    SourceFile sfViolationA = FlowAstScanner
        .scanSingleFile(new File("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
            + "/pub/checkBranchPropertiesInvalidA/flow.xml"), checks, metrics);
    List<CheckMessage> violationAMessages = new ArrayList<CheckMessage>(
        sfViolationA.getCheckMessages());
    assertEquals(1, violationAMessages.size());
    assertTrue("Returned check message not as expected",
        expectedMessageA.equals(violationAMessages.get(0).getDefaultMessage()));
  }

  @Test
  public void branchPropertiesCheckInvalidB() {
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    checks.add(new BranchPropertiesCheck());

    final String expectedMessageB 
        = "Evaluate labels must be true when no switch parameter is defined in BRANCH";

    // Check violation flow B: neither switch nor evaluate labels defined
    SourceFile sfViolationB = FlowAstScanner
        .scanSingleFile(new File("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
            + "/pub/checkBranchPropertiesInvalidB/flow.xml"), checks, metrics);
    List<CheckMessage> violationBMessages = new ArrayList<CheckMessage>(
        sfViolationB.getCheckMessages());
    assertEquals(1, violationBMessages.size());
    assertTrue("Returned check message not as expected",
        expectedMessageB.equals(violationBMessages.get(0).getDefaultMessage()));
  }
}
