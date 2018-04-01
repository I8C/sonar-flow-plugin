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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowMetric;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.MetricList;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowConfiguration;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.NodeParser;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheck;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.AstScanner.Builder;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;

/**
 * This class will set the visitors that will be called on inspection.
 * 
 * @author DEWANST
 */
public class NodeAstScanner {

  static final Logger logger = LoggerFactory.getLogger(NodeAstScanner.class);

  private NodeAstScanner() {
  }
  
  /**
   * Creates the AstScanner. It will set flow and metric checks, next to the default ones.
   * 
   * @param conf
   *          Configuration
   * @param checks
   *          Additional checks to set
   * @param metrics
   *          Additional metrics to set
   * @return
   */
  public static AstScanner<Grammar> create(FlowConfiguration conf, Collection<FlowCheck> checks,
      @Nullable List<SquidAstVisitor<Grammar>> metrics) {
    final SquidAstVisitorContextImpl<Grammar> context = new SquidAstVisitorContextImpl<Grammar>(
        new SourceProject("Flow Project"));
    final Parser<Grammar> parser = NodeParser.create(conf);

    AstScanner.Builder<Grammar> builder = AstScanner.<Grammar>builder(context)
        .setBaseParser(parser);
    /* Required commentAnalyzer */
    builder.setCommentAnalyser(new CommentAnalyser());
    /* Metrics */
    ArrayList<SquidAstVisitor<Grammar>> metricList = new ArrayList<SquidAstVisitor<Grammar>>();
    metricList.addAll(MetricList.getDefaultNodeVisitors());
    if (metrics != null) {
      metricList.addAll(metrics);
    }
    setMetrics(builder, metricList);
    /* Checks */
    for (FlowCheck flowCheck : checks) {
      builder.withSquidAstVisitor(flowCheck);
    }
    return builder.build();
  }

  private static void setMetrics(Builder<Grammar> builder,
      List<SquidAstVisitor<Grammar>> visitors) {
    builder.withMetrics(FlowMetric.values());
    builder.setFilesMetric(FlowMetric.FILES);
    for (SquidAstVisitor<Grammar> v : visitors) {
      builder.withSquidAstVisitor(v);
    }
  }

  /**
   * Helper method for testing checks without having to deploy them on a Sonar instance.
   */
  public static SourceFile scanSingleFile(File file, Collection<FlowCheck> nodeChecks,
      List<SquidAstVisitor<Grammar>> metrics) {
    if (!file.isFile()) {
      throw new IllegalArgumentException("File '" + file + "' not found.");
    }

    AstScanner<Grammar> scanner = create(new FlowConfiguration(Charsets.UTF_8), nodeChecks,
        metrics);
    scanner.scanFile(file);
    Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
    if (sources.size() != 1) {
      throw new IllegalStateException(
          "Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
    }
    return (SourceFile) sources.iterator().next();
  }
}
