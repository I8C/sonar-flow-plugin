package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import org.junit.Test;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.TestFile;

public class NodeMetricsVisitorTest {

  @Test
  public void metricsTest() {
    NodeMetricsVerifier.verifyMetricsOnFile(
        new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkNodeMetrics/node.ndf"));
  }
  
}
