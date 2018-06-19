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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.InterfaceCommentsCheck;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterfaceCommentsCheckTest {

  static final Logger logger = LoggerFactory.getLogger(InterfaceCommentsCheckTest.class);


  @Test
  public void interfaceCommentsCheckInvalid() {
    java.util.List<java.util.Map.Entry<String,Integer>> expectedIssues= new java.util.ArrayList<>();
    Entry<String,Integer> issue1=new SimpleEntry<>("Add comment",19);
    Entry<String,Integer> issue2=new SimpleEntry<>("Add comment", 58);
    expectedIssues.add(issue1);
    expectedIssues.add(issue2);
    
    NodeVerifier.verifyMultipleIssueOnFile(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkInterfaceCommentsInvalid/node.ndf"), new InterfaceCommentsCheck(), expectedIssues);
  }

  @Test
  public void interfaceCommentsCheckInvalid2() {
    java.util.List<java.util.Map.Entry<String,Integer>> expectedIssues= new java.util.ArrayList<>();
    Entry<String,Integer> issue1=new SimpleEntry<>("Add comment",20);
    Entry<String,Integer> issue2=new SimpleEntry<>("Add comment", 51);
    Entry<String,Integer> issue3=new SimpleEntry<>("Add comment",67);
    Entry<String,Integer> issue4=new SimpleEntry<>("Add comment", 85);
    expectedIssues.add(issue1);
    expectedIssues.add(issue2);
    expectedIssues.add(issue3);
    expectedIssues.add(issue4);
    
    NodeVerifier.verifyMultipleIssueOnFile(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkInterfaceCommentsInvalid2/node.ndf"), new InterfaceCommentsCheck(), expectedIssues);
  }
  
  @Test
  public void interfaceCommentsCheckWs() {
    NodeVerifier.verifyNoIssue(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkInterfaceCommentsWs/node.ndf"), new InterfaceCommentsCheck());
    
  }
  
  @Test
  public void interfaceCommentsCheckValid() {
    NodeVerifier.verifyNoIssue(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkInterfaceCommentsValid/node.ndf"), new InterfaceCommentsCheck());
  }
}
