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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import com.sonar.sslr.api.AstNode;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.type.TopLevelCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowLexer.FlowAttTypes;

@Rule(key="S00006",name = "In the EXIT step, \"Exit from \" property must be defined and "
		+ "the \"Failure message\" must be defined if the \"signal\" property is FAILURE", 
		priority = Priority.MINOR, tags = {Tags.DEBUG_CODE, Tags.BAD_PRACTICE})
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("2min")
public class ExitCheck extends TopLevelCheck{

	final static Logger logger = LoggerFactory.getLogger(ExitCheck.class);
	
	@Override
	public void init() {
		logger.debug("++ Initializing {} ++", this.getClass().getName());
		subscribeTo(FlowGrammar.EXIT);
	}

	@Override
	public void visitNode(AstNode astNode) {
		AstNode exitNode = astNode.getFirstChild(FlowGrammar.ATTRIBUTES);
		if (exitNode != null){
			logger.debug("++ Exit interface element found. ++");
			String exitFrom = getExitFrom(exitNode);
			if (exitFrom == null || exitFrom.trim().equals("")) {
				logger.debug("++ \"Exit from\" property found to be empty! ++");
				getContext().createLineViolation(this, "++ The \"Exit from\" "
				+ "property must be defined for the interface element 'EXIT'. ++", exitNode);
			}
			if (hasSignalSetToFailure(exitNode)) {
				String exitFailureMessage = getExitFailureMessage(exitNode);
				if (exitFailureMessage == null || exitFailureMessage.trim().equals("")) {
					logger.debug("++ Failure message has not been set even though"
							+ " the signal status has been set to failure! ++");
					getContext().createLineViolation(this, "Create a Failure message"
							+ " for the interface element 'EXIT'." , exitNode);
				}
			}
		}
	}

	private String getExitFrom(AstNode exitNode) {
		if (exitNode != null) {
			AstNode fromAtt = exitNode.getFirstChild(FlowAttTypes.FROM);
			if (fromAtt != null) {
				String fromType = fromAtt.getToken().getOriginalValue();
				logger.debug("++ From field found! ++");
				if ( fromType != null) {
					return fromType;
				}
			}
		}
		return null;
	}
	
	private String getExitFailureMessage(AstNode exitNode) {
		if (exitNode != null) {
			AstNode failureMessageAtt = exitNode.getFirstChild(FlowAttTypes.FAILUREMESSAGE);
			if (failureMessageAtt != null) {
				logger.debug("++ Failure message field found! ++");
				return failureMessageAtt.getToken().getOriginalValue();
			}			
		}
		return null;
	}
	
	private Boolean hasSignalSetToFailure(AstNode exitNode) {
		if (exitNode != null) {
			AstNode signalAtt = exitNode.getFirstChild(FlowAttTypes.SIGNAL);
			if (signalAtt != null){
				logger.debug("++ Signal found ++");
				String signalValue = signalAtt.getToken().getOriginalValue();
				if (signalValue != null){
					if (signalValue.equalsIgnoreCase("FAILURE")){
						logger.debug("++ Signal is set to FAILURE! ++");
						return true;
					}
				}
			}
		}
		return false;
	}
	
}