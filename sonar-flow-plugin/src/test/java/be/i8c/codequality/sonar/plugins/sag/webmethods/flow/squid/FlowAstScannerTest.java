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
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceFile;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.BranchSwitchAndEvaluateLabelsCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.SavePipelineCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.TryCatchCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.FlowAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitors.SimpleMetricVisitor;

public class FlowAstScannerTest {

	final static Logger logger = LoggerFactory.getLogger(FlowAstScannerTest.class);
	
	@Test
	  public void scanFile() {
		logger.debug("Scanning file");
		FlowAstScanner.scanSingleFile(new File("src/test/resources/WmPackage/ns/WmPackage/flows/testFlow/flow.xml"), new SimpleMetricVisitor());
	}
	
	@Test
	  public void tryCatchCheck() {
		FlowAstScanner.scanSingleFile(new File("src/test/resources/WmPackage/ns/WmPackage/flows/testFlow/flow.xml"), new TryCatchCheck());
	}
	
	@Test
	  public void savePipelineCheck() {
		FlowAstScanner.scanSingleFile(new File("src/test/resources/WmPackage/ns/WmPackage/flows/testFlow/flow.xml"), new SavePipelineCheck());
	}
	
	@Test
	public void branchEvaluateLabelsCheck() {
		
		String expectedMessageA = "Both switch and evaluate labels are defined in properties of BRANCH";
		String expectedMessageB = "Evaluate labels must be true when no switch parameter is defined in BRANCH";
		
		SourceFile sf = FlowAstScanner.scanSingleFile( new File("src/test/resources/WmPackage/ns/WmPackage/flows/violationBranchEvaluateLabels/flow.xml"), new BranchSwitchAndEvaluateLabelsCheck());
		Set<CheckMessage> scm = sf.getCheckMessages();

		// check expected number of checkMessages
		assertEquals(2, scm.size());
		
		// check if both violations were encountered
		for (CheckMessage checkMessage : scm) {
			assertTrue( "Expected returned check messages were not found", (expectedMessageA.equals(checkMessage.getDefaultMessage() ) || expectedMessageB.equals(checkMessage.getDefaultMessage()) ) );
		}
	}
	
}
