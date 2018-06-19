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

import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Configuration;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.FlowLanguage;

public class StatelessFlagCheckTest {

  static final Logger logger = LoggerFactory.getLogger(StatelessFlagCheckTest.class);


  @Test
  public void statelessFlagCheckValid() {
    Configuration cfg = mock(Configuration.class);
    Mockito.when(cfg.get(StatelessFlagCheck.FILTER_ENABLE_KEY)).thenReturn(Optional.of("false"));
    Mockito.when(cfg.get(StatelessFlagCheck.FILTER_REGEX_KEY)).thenReturn(Optional.of(StatelessFlagCheck.FILTER_REGEX_DEFVALUE));
    FlowLanguage.setConfig(cfg);
    
    NodeVerifier.verifyNoIssue(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkStatelessFlagValid/node.ndf"), new StatelessFlagCheck());
  }

  @Test
  public void statelessFlagCheckInValid() {
    Configuration cfg = mock(Configuration.class);
    Mockito.when(cfg.get(StatelessFlagCheck.FILTER_ENABLE_KEY)).thenReturn(Optional.of("false"));
    Mockito.when(cfg.get(StatelessFlagCheck.FILTER_REGEX_KEY)).thenReturn(Optional.of(StatelessFlagCheck.FILTER_REGEX_DEFVALUE));
    FlowLanguage.setConfig(cfg);
    
    NodeVerifier.verifySingleIssueOnFile(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkStatelessFlagInvalid/node.ndf"), new StatelessFlagCheck(), "Set stateless flag to \"true\"", 88);
  }
  
}
