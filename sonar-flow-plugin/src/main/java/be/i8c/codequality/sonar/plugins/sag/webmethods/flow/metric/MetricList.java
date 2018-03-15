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
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowDependencyVisitor;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowLinesOfCodeVisitor;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.type.FlowVisitor;

import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Grammar;

import java.util.List;

import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.metrics.CommentsVisitor;
import org.sonar.squidbridge.metrics.ComplexityVisitor;
import org.sonar.squidbridge.metrics.CounterVisitor;
import org.sonar.squidbridge.metrics.LinesVisitor;

/**
 * This is a helper class that holds different lists of @see SquidAstVisitor.
 * These vistors are all metric-type visitors.
 * @author DEWANST
 */
public class MetricList {

  private static final AstNodeType[] complexityAstNodeType = new AstNodeType[] { FlowGrammar.LOOP,
      FlowGrammar.BRANCH, FlowGrammar.SEQUENCE, FlowGrammar.RETRY };

  public static List<SquidAstVisitor<Grammar>> getVisitors() {
    return ImmutableList.<SquidAstVisitor<Grammar>>builder().addAll(getFlowVisitors())
        .addAll(getDefaultVisitors()).build();
  }

  /**
   * This static method returns the list of available flow visitors.
   * @return List of available flow visitors
   */
  public static List<FlowVisitor<Grammar>> getFlowVisitors() {
    return ImmutableList.<FlowVisitor<Grammar>>builder()
        .add(new FlowLinesOfCodeVisitor<Grammar>(FlowMetric.LINES_OF_CODE))
        .add(new FlowDependencyVisitor<Grammar>()).build();
  }
  
  /**
   * This static method returns the list of the required default visitors.
   * @return List of available required default visitors
   */
  public static List<SquidAstVisitor<Grammar>> getDefaultVisitors() {
    return ImmutableList.<SquidAstVisitor<Grammar>>builder()
        .add(ComplexityVisitor.<Grammar>builder().setMetricDef(FlowMetric.COMPLEXITY)
            .subscribeTo(complexityAstNodeType).build())
        .add(CommentsVisitor.<Grammar>builder().withCommentMetric(FlowMetric.COMMENT_LINES)
            .withNoSonar(true).build())
        .add(CounterVisitor.<Grammar>builder().setMetricDef(FlowMetric.MAPS)
            .subscribeTo(FlowGrammar.MAP).build())
        .add(new LinesVisitor<Grammar>(FlowMetric.LINES)).build();
  }

}
