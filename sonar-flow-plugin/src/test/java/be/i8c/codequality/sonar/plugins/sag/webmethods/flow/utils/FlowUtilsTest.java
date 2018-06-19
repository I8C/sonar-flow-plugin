package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.utils;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FlowUtilsTest {

  static final Logger logger = LoggerFactory.getLogger(FlowUtilsTest.class);

  @Test
  public void getQualifiedNameValid() {
    Assert.assertEquals(FlowUtils.getQualifiedName(new File(
        "D:\\temp\\SoftwareAG\\IntegrationServer\\instances\\default\\packages"
        + "\\myTestPackage\\ns\\myTestPackage\\new_flowservice\\flow.xml")),
        "myTestPackage:new_flowservice");
  }
  
  @Test
  public void getQualifiedNameValid2() {
    Assert.assertEquals(FlowUtils.getQualifiedName(new File(
        "src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkQualityNameInvalid/flow.xml")),"I8cFlowSonarPluginTest.pub:checkQualityNameInvalid");
  }
  
  @Test
  public void getQualifiedNameValid3() {
    Assert.assertEquals(FlowUtils.getQualifiedName(new File(
        "/C:/Data/git/sonar-flow-plugin/sonar-flow-plugin/src/test/resources/"
        + "WmTestPackage/ns/I8cFlowSonarPluginTest/pub/checkQualityNameInvalid/flow.xml").toURI()), "I8cFlowSonarPluginTest.pub:checkQualityNameInvalid");
  }
  
}
