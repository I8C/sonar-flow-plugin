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
package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowLexer.FlowAttTypes;

@Rule(key = "S00002", name = "No invokes of savePipeline or restorePipeline should be in code", priority = Priority.MAJOR, tags = {
		Tags.DEBUG_CODE, Tags.BAD_PRACTICE })
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("2min")
public class SavePipelineCheck extends SquidCheck<Grammar>{

	final static Logger logger = LoggerFactory.getLogger(SavePipelineCheck.class);
	
	private static final String DEFAULT_PS = "pub.flow:savePipeline,pub.flow:savePipelineToFile,pub.flow:restorePipeline,pub.flow:restorePipelineFromFile";

	@RuleProperty(
			key = "pipelineServices",
			description = "Comma-seperated list of pipeline services",
			defaultValue = "" + DEFAULT_PS)
	private String pipelineServices = DEFAULT_PS;
	  
	@Override
	public void init() {
		logger.debug("++ Initializing " + this.getClass().getName() + " ++");
		subscribeTo(FlowGrammar.INVOKE);
	}

	@Override
	public void visitNode(AstNode astNode) {
		String service = astNode.getFirstChild(FlowGrammar.ATTRIBUTES).getFirstChild(FlowAttTypes.SERVICE).getToken().getOriginalValue();
		logger.debug("Invoke of service found: " + service);
		if(Arrays.asList(pipelineServices.split(",")).contains(service)){
			getContext().createLineViolation(this, "Remove service " + service, astNode);
		}
	}

}