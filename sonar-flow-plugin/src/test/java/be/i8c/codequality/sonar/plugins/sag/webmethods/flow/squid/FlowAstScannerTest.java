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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceFile;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.*;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.FlowAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitors.SimpleMetricVisitor;

public class FlowAstScannerTest {

	final static Logger logger = LoggerFactory.getLogger(FlowAstScannerTest.class);
	
	//File flowFile = new File("src/test/resources/WmPackage/ns/WmPackage/flows/myService/flow.xml");
	
	@Test
	public void debug(){
		List<Class> sd = CheckList.getChecks();
		for (Class class1 : sd) {
			logger.debug(sd.toString());
			sd.toString();
		}
	}
	
	@Test
	  public void gitFolderTest() {
		logger.debug("Scanning file");
		FlowAstScanner.scanSingleFile( new File("src/test/resources/WmPackage/.git/someFile") , new SimpleMetricVisitor());

	}
	
	@Test
	  public void scanFile() {
		logger.debug("Scanning file");
		FlowAstScanner.scanSingleFile( new File("src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/flows/subProcess/flow.xml") , new SimpleMetricVisitor());

	}
	
	@Test
	  public void tryCatchCheck() {
		
		//check valid flow
		String validFlowPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkTryCatchValid/flow.xml";
	
		SourceFile sfCorrect = FlowAstScanner.scanSingleFile( new File(validFlowPath) , new TryCatchCheck());
		Set<CheckMessage> scmCorrect = sfCorrect.getCheckMessages();
		assertEquals(0, scmCorrect.size());
		
		
		// check invalid flow
		String invalidFlowPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkTryCatchInvalid/flow.xml";
		String expectedMessage = "Create try-catch sequence";
		
		SourceFile sfViolation = FlowAstScanner.scanSingleFile( new File(invalidFlowPath) , new TryCatchCheck());
		List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(sfViolation.getCheckMessages());
		assertEquals(1, violationMessages.size());
		assertTrue("Returned check message not as expected", expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));

	}
	
	@Test
	  public void savePipelineCheck() {
		String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkSavePipelineInvalid/flow.xml";
		String expectedMessage = "Remove service pub.flow:savePipeline";
		
		SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), new SavePipelineCheck());
		List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(sfViolation.getCheckMessages());
		assertEquals(1, violationMessages.size());
		assertTrue("Returned check message not as expected", expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));

	}
	
	@Test
	  public void disabledCheck() {
		String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkDisabledInvalid/flow.xml";
		String expectedMessage = "Remove disabled code";
		
		SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), new DisabledCheck());
		List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(sfViolation.getCheckMessages());
		assertEquals(1, violationMessages.size());
		assertTrue("Returned check message not as expected", expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));
	}
	
	@Test
	  public void interfaceCommentsCheck() {
		String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkInterfaceCommentsInvalid/flow.xml";
		
		FlowAstScanner.scanSingleFile( new File(invalidPath) , new InterfaceCommentsCheck());
		// Todo functional validation
	}
	
	@Test
	  public void qualifiedNameCheck() {
		String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkQualityNameInvalid/flow.xml";
		
		FlowAstScanner.scanSingleFile(new File(invalidPath), new QualifiedNameCheck());
		// Todo	functional validation
	}
	
	@Test
	  public void exitCheck() {
		String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkExitStepInvalid/flow.xml";
		
		SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), new ExitCheck());
		List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(sfViolation.getCheckMessages());
		assertEquals(2, violationMessages.size());
		// Todo check both violation messages
	}
	
	@Test 
	  public void emptyMapCheck() {
		String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkEmptyMapInvalid/flow.xml";
		String expectedMessage = "This map step in the flow is empty, create content or remove the map.";
		
		SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), new EmptyMapCheck());
		List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(sfViolation.getCheckMessages());
		assertEquals(1, violationMessages.size());
		assertTrue("Returned check message not as expected", expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));
	}
	
	@Test
	  public void emptyFlowCheck() {
		String invalidPath = "src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkEmptyFlowInvalid/flow.xml";
		String expectedMessage = "Service doesn't contain any flow steps. Remove service or add flow steps.";
		
		SourceFile sfViolation = FlowAstScanner.scanSingleFile(new File(invalidPath), new EmptyFlowCheck());
		List<CheckMessage> violationMessages = new ArrayList<CheckMessage>(sfViolation.getCheckMessages());
		assertEquals(1, violationMessages.size());
		assertTrue("Returned check message not as expected", expectedMessage.equals(violationMessages.get(0).getDefaultMessage()));
	}
	
	@Test
	public void branchPropertiesCheck() {
		String expectedMessageA = "Both switch and evaluate labels are defined in properties of BRANCH";
		String expectedMessageB = "Evaluate labels must be true when no switch parameter is defined in BRANCH";
		
		// Check correct flow
		SourceFile sfCorrect = FlowAstScanner.scanSingleFile( new File("src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkBranchPropertiesValid/flow.xml"), new BranchPropertiesCheck());
		Set<CheckMessage> scmCorrect = sfCorrect.getCheckMessages();
		assertEquals(0, scmCorrect.size());
		
		// Check violation flow A: both switch and evaluate labels defined
		SourceFile sfViolationA = FlowAstScanner.scanSingleFile( new File("src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkBranchPropertiesInvalidA/flow.xml"), new BranchPropertiesCheck());
		List<CheckMessage> violationAMessages = new ArrayList<CheckMessage>(sfViolationA.getCheckMessages());
		assertEquals(1, violationAMessages.size());
		assertTrue("Returned check message not as expected",expectedMessageA.equals(violationAMessages.get(0).getDefaultMessage()));
		
		// Check violation flow B: neither switch nor evaluate labels defined
		SourceFile sfViolationB = FlowAstScanner.scanSingleFile( new File("src/test/resources/WmPackage/ns/I8cFlowSonarPluginTest/pub/checkBranchPropertiesInvalidB/flow.xml"), new BranchPropertiesCheck());
		List<CheckMessage> violationBMessages = new ArrayList<CheckMessage>(sfViolationB.getCheckMessages());
		assertEquals(1, violationBMessages.size());
		assertTrue("Returned check message not as expected", expectedMessageB.equals(violationBMessages.get(0).getDefaultMessage()));
	}
}
