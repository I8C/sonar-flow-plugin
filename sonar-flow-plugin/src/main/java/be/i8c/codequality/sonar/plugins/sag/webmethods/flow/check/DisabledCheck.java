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
import org.sonar.squidbridge.checks.SquidCheck;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowLexer.FlowAttTypes;

@Rule(key = "S00003", name = "No disabled elements should be in code", priority = Priority.MINOR, tags = { 
		Tags.DEBUG_CODE, Tags.BAD_PRACTICE })
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("2min")
public class DisabledCheck extends SquidCheck<Grammar>{

	final static Logger logger = LoggerFactory.getLogger(DisabledCheck.class);
	
	@Override
	public void init() {
		logger.debug("++ Initializing " + this.getClass().getName() + " ++");
		subscribeTo(FlowGrammar.INVOKE,
				FlowGrammar.EXIT,
				FlowGrammar.BRANCH,
				FlowGrammar.LOOP,
				FlowGrammar.MAP,
				FlowGrammar.RETRY,
				FlowGrammar.SEQUENCE);
	}

	@Override
	public void visitNode(AstNode astNode) {
		AstNode disabled = astNode.getFirstChild(FlowGrammar.ATTRIBUTES).getFirstChild(FlowAttTypes.DISABLED);
		if(disabled != null){
			String isDisabled = disabled.getToken().getOriginalValue();
			if(Boolean.valueOf(isDisabled)){
				getContext().createLineViolation(this, "Remove disabled code", astNode);
			}
		}
	}

}