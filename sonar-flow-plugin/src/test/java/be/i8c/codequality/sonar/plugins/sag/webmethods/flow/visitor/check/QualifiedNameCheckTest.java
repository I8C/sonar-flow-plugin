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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.FlowLanguage;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.QualifiedNameCheck;

import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Configuration;

public class QualifiedNameCheckTest {

  static final Logger logger = LoggerFactory.getLogger(QualifiedNameCheckTest.class);


  @Test
  public void qualifiedNameCheckValid() {
    Configuration cfg = mock(Configuration.class);
    Mockito.when(cfg.get(QualifiedNameCheck.QUALIFIED_NAME_KEY)).thenReturn(Optional.of(QualifiedNameCheck.QUALIFIED_NAME_DEFVALUE));
    FlowLanguage.setConfig(cfg);
    FlowVerifier.verifyNoIssue(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkQualityNameInvalid/flow.xml"), new QualifiedNameCheck());
  }

  @Test
  public void qualifiedNameCheckInvalid() {
    Configuration cfg = mock(Configuration.class);
    Mockito.when(cfg.get(QualifiedNameCheck.QUALIFIED_NAME_KEY)).thenReturn(Optional.of("test.*"));
    FlowLanguage.setConfig(cfg);
    FlowVerifier.verifySingleIssueOnFile(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkQualityNameInvalid/flow.xml"), new QualifiedNameCheck(),
        "Flow name I8cFlowSonarPluginTest.pub:checkQualityNameInvalid does not conform to the naming convention \"test.*\"", 3);
  }
  
}
