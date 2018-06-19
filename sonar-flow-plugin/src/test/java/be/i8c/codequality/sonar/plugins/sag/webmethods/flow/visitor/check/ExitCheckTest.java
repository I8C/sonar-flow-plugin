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


import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.ExitCheck;

import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExitCheckTest {

  static final Logger logger = LoggerFactory.getLogger(ExitCheckTest.class);

  @Test
  public void exitCheck() {
    java.util.List<java.util.Map.Entry<String,Integer>> expectedIssues= new java.util.ArrayList<>();
    Entry<String,Integer> issue1=new SimpleEntry<>("The \"Exit from\" property must be defined for the interface element 'EXIT'",448);
    Entry<String,Integer> issue2=new SimpleEntry<>("Create a Failure message for the interface element 'EXIT'.", 448);
    expectedIssues.add(issue1);
    expectedIssues.add(issue2);
    
    FlowVerifier.verifyMultipleIssueOnFile(new TestFile("src/test/resources/WmTestPackage/ns/I8cFlowSonarPluginTest"
        + "/pub/checkExitStepInvalid/flow.xml"), new ExitCheck(), expectedIssues);
  }

}
