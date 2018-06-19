package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import java.util.HashMap;

import org.junit.Test;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.TestFile;

public class FlowMetricsVisitorTest {

  @Test
  public void metricsTest() {
    HashMap<Integer, String> dependencies = new HashMap<Integer,String>();
    FlowMetricsVerifier.verifyMetricsOnFile(
        new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/commentLinesVisitor/flow.xml"), 2, 13, 1, dependencies);
  }
  
  @Test
  public void noCommentLinesTest() {
    HashMap<Integer, String> dependencies = new HashMap<Integer,String>();
    FlowMetricsVerifier.verifyMetricsOnFile(
        new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/commentLinesVisitor2/flow.xml"), 0, 1, 0, dependencies);
  }
  
}
