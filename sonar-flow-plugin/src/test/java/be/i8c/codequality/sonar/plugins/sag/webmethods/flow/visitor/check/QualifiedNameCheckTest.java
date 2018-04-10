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
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.FlowAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.SimpleMetricVisitor;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.QualifiedNameCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheck;

import com.sonar.sslr.api.Grammar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Configuration;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceFile;

public class QualifiedNameCheckTest {

  static final Logger logger = LoggerFactory.getLogger(QualifiedNameCheckTest.class);


  @Test
  public void qualifiedNameCheckValid() {
    Configuration cfg = mock(Configuration.class);
    Mockito.when(cfg.get(QualifiedNameCheck.QUALIFIED_NAME_KEY)).thenReturn(Optional.of(QualifiedNameCheck.QUALIFIED_NAME_DEFVALUE));
    FlowLanguage.setConfig(cfg);
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    QualifiedNameCheck qnc = new QualifiedNameCheck();
    checks.add(qnc);
    String validPath = "src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkQualityNameInvalid/flow.xml";

    SourceFile sfCorrect = FlowAstScanner.scanSingleFile(new File(validPath), checks, metrics);
    Set<CheckMessage> scmCorrect = sfCorrect.getCheckMessages();
    assertEquals(0, scmCorrect.size());  
  }

  @Test
  public void qualifiedNameCheckInvalid() {
    Configuration cfg = mock(Configuration.class);
    Mockito.when(cfg.get(QualifiedNameCheck.QUALIFIED_NAME_KEY)).thenReturn(Optional.of("test.*"));
    FlowLanguage.setConfig(cfg);
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new SimpleMetricVisitor());
    List<FlowCheck> checks = new ArrayList<FlowCheck>();
    QualifiedNameCheck qnc = new QualifiedNameCheck();
    checks.add(qnc);
    String validPath = "src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkQualityNameInvalid/flow.xml";

    SourceFile sfCorrect = FlowAstScanner.scanSingleFile(new File(validPath), checks, metrics);
    Set<CheckMessage> scmCorrect = sfCorrect.getCheckMessages();
    assertEquals(1, scmCorrect.size());  
  }
  
}
