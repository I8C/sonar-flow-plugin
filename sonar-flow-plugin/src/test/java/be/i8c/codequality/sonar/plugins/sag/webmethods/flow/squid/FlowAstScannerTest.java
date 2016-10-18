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

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.QualifiedNameCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.SavePipelineCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.TryCatchCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid.FlowAstScanner;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitors.SimpleMetricVisitor;

public class FlowAstScannerTest {

	final static Logger logger = LoggerFactory.getLogger(FlowAstScannerTest.class);
	
	@Test
	  public void scanFile() {
		logger.debug("Scanning file");
		FlowAstScanner.scanSingleFile(new File("src/test/resources/ns/MyPackage/flow/myService/flow.xml"), new SimpleMetricVisitor());
	}
	
	@Test
	  public void tryCatchCheck() {
		FlowAstScanner.scanSingleFile(new File("src/test/resources/ns/MyPackage/flow/myService/flow.xml"), new TryCatchCheck());
	}
	
	@Test
	  public void savePipelineCheck() {
		FlowAstScanner.scanSingleFile(new File("src/test/resources/ns/MyPackage/flow/myService/flow.xml"), new SavePipelineCheck());
	}
	
	@Test
	  public void qualifiedNameCheck() {
		FlowAstScanner.scanSingleFile(new File("src/test/resources/ns/MyPackage/flow/myService/flow.xml"), new QualifiedNameCheck());
	}
}
