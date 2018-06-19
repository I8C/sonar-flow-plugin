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

public class TryCatchCheckTest {

  static final Logger logger = LoggerFactory.getLogger(TryCatchCheckTest.class);


  @Test
  public void tryCatchCheckValid() {
    FlowVerifier.verifyNoIssue(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkTryCatchValid/flow.xml"), new TryCatchCheck());
  }
  
  @Test
  public void tryCatchCheckInvalid() {
    FlowVerifier.verifySingleIssueOnFile(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkTryCatchInvalid/flow.xml"), new TryCatchCheck(),
        "Create try-catch sequence", 3);

  }

}
