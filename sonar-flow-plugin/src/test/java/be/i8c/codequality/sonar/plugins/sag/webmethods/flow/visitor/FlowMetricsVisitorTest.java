package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import java.util.HashMap;

import org.junit.Test;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.TestFile;

public class FlowMetricsVisitorTest {

  @Test
  public void metricsTest() {
    HashMap<Integer, String> dependencies = new HashMap<Integer,String>();
    dependencies.put(195,"pub.flow:clearPipeline");
    dependencies.put(726,"fram.admin.ama.utilities:buildAssetState");
    dependencies.put(471,"pub.flow:clearPipeline");
    dependencies.put(969,"pub.flow:clearPipeline");
    dependencies.put(714,"pub.art.connection:queryConnectionState");
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
