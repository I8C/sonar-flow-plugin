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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BranchPropertiesCheckTest {

  static final Logger logger = LoggerFactory.getLogger(BranchPropertiesCheckTest.class);

  @Test
  public void branchPropertiesCheckValid() {
    FlowVerifier.verifyNoIssue(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
            + "/pub/checkBranchPropertiesValid/flow.xml"), new BranchPropertiesCheck());
  }

  @Test
  public void branchPropertiesCheckInvalidA() {
    FlowVerifier.verifySingleIssueOnFile(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkBranchPropertiesInvalidA/flow.xml"), new BranchPropertiesCheck(),
        "Both switch and evaluate labels are defined in properties of BRANCH", 519);
  }

  @Test
  public void branchPropertiesCheckInvalidB() {
    FlowVerifier.verifySingleIssueOnFile(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkBranchPropertiesInvalidB/flow.xml"), new BranchPropertiesCheck(),
        "Evaluate labels must be true when no switch parameter is defined in BRANCH", 519);
  }
}
