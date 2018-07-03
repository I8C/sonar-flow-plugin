package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric;

import java.util.Objects;

import com.sonar.sslr.api.AstNode;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.NodeMetricsVisitor;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.NodeVisitorContext;

public class NodeFileMetrics {

  private final NodeMetricsVisitor fileLinesVisitor = new NodeMetricsVisitor();

  /**
   * Creates a NodeFileMetrics Object, which contains metrics that are linked with this file. It
   * contains metrics that can be evaluated by inspecting the AST tree or by scanning the file.
   * Scanning the file is used when more complex compositions are needed to calculate a metric.
   * 
   * @param context
   */
  public NodeFileMetrics(NodeVisitorContext context) {
    AstNode rootTree = context.rootTree();
    Objects.requireNonNull(rootTree, "Cannot compute metrics without a root tree");
    fileLinesVisitor.scanFile(context);
  }

}
