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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowCommentLinesVisitor;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowDependencyVisitor;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowLinesOfCodeVisitor;

import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Grammar;

import java.util.List;

import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.metrics.ComplexityVisitor;
import org.sonar.squidbridge.metrics.LinesVisitor;

/**
 * This is a helper class that holds different lists of @see SquidAstVisitor.
 * These vistors are all metric-type visitors.
 * @author DEWANST
 */
public class MetricList {

  private static final AstNodeType[] complexityAstNodeType = new AstNodeType[] { FlowGrammar.LOOP,
      FlowGrammar.BRANCH, FlowGrammar.SEQUENCE, FlowGrammar.RETRY };

  /**
   * This static method returns the list of available flow visitors.
   * @return List of available flow visitors
   */
  public static List<SquidAstVisitor<Grammar>> getFlowVisitors() {
    return ImmutableList.<SquidAstVisitor<Grammar>>builder()
        .addAll(getDefaultFlowVisitors())
        .add(new FlowLinesOfCodeVisitor<Grammar>(FlowMetric.LINES_OF_CODE))
        .add(new FlowCommentLinesVisitor<Grammar>(FlowMetric.COMMENT_LINES))
        .add(new FlowDependencyVisitor<Grammar>()).build();
  }
  
  /**
   * This static method returns the list of the required default flow visitors.
   * @return List of available required default flow visitors
   */
  public static List<SquidAstVisitor<Grammar>> getDefaultFlowVisitors() {
    return ImmutableList.<SquidAstVisitor<Grammar>>builder()
        .add(ComplexityVisitor.<Grammar>builder().setMetricDef(FlowMetric.COMPLEXITY)
            .subscribeTo(complexityAstNodeType).build())
        .add(new LinesVisitor<Grammar>(FlowMetric.LINES)).build();
  }

  /**
   * This static method returns the list of available node visitors.
   * @return List of available node visitors
   */
  public static List<SquidAstVisitor<Grammar>> getNodeVisitors() {
    return ImmutableList.<SquidAstVisitor<Grammar>>builder()
        .addAll(getDefaultNodeVisitors()).build();
  }
  
  /**
   * This static method returns the list of the required default node visitors.
   * @return List of available required default node visitors
   */
  public static List<SquidAstVisitor<Grammar>> getDefaultNodeVisitors() {
    return ImmutableList.<SquidAstVisitor<Grammar>>builder()
        .add(ComplexityVisitor.<Grammar>builder().setMetricDef(FlowMetric.COMPLEXITY)
            .subscribeTo(complexityAstNodeType).build())
        .add(new LinesVisitor<Grammar>(FlowMetric.LINES)).build();
  }
}
