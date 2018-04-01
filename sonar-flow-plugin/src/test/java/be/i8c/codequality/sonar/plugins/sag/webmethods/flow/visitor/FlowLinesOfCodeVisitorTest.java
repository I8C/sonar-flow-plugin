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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import static org.junit.Assert.assertEquals;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowMetric;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.FlowAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowLinesOfCodeVisitor;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheck;

import com.sonar.sslr.api.Grammar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.SourceFile;

public class FlowLinesOfCodeVisitorTest {

  static final Logger logger = LoggerFactory.getLogger(FlowLinesOfCodeVisitorTest.class);

  @Test
  public void linesOfCodeTest() {
    logger.debug("Scanning file");
    List<SquidAstVisitor<Grammar>> metrics = new ArrayList<SquidAstVisitor<Grammar>>();
    metrics.add(new FlowLinesOfCodeVisitor<Grammar>(FlowMetric.LINES_OF_CODE));
    List<FlowCheck> checks = new ArrayList<FlowCheck>();

    SourceFile sfCorrect = FlowAstScanner.scanSingleFile(new File(
        "src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkLinesOfCode/flow.xml"),
        checks, metrics);
    int lines = (int) sfCorrect.getDouble(FlowMetric.LINES_OF_CODE);
    assertEquals(13, lines);
  }
  
}
