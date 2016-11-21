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

import java.util.List;

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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.type.TopLevelCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowLexer.FlowAttTypes;

@Rule(key="S00007",name = "Interface should not contain empty map steps.", 
		priority = Priority.MINOR, tags = {Tags.DEBUG_CODE, Tags.BAD_PRACTICE})
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("2min")
public class EmptyMapCheck extends SquidCheck<Grammar>{

	final static Logger logger = LoggerFactory.getLogger(EmptyMapCheck.class);
	
	@Override
	public void init() {
		logger.debug("++ Initializing {} ++", this.getClass().getName());
		subscribeTo(FlowGrammar.MAP);
	}

	@Override
	public void visitNode(AstNode astNode) {
		String mode = astNode.getFirstChild(FlowGrammar.ATTRIBUTES).getFirstChild(FlowAttTypes.MODE).getToken().getOriginalValue();
		if (mode.equalsIgnoreCase("STANDALONE")) {
			logger.debug("++ Map step found in the flow ++");
			boolean isEmptyMap = true;
			List<AstNode> children = astNode.getChildren();
			for (AstNode child:children) {
				if (child.getTokenOriginalValue().equalsIgnoreCase("INVOKEINPUT")
						|| child.getTokenOriginalValue().equalsIgnoreCase("INVOKEOUTPUT")
						|| child.getTokenOriginalValue().equalsIgnoreCase("MAPDELETE")
						|| child.getTokenOriginalValue().equalsIgnoreCase("MAPCOPY")) {
					logger.debug("++ This is not an empty map ++");
					isEmptyMap = false;
				}
			}
			if (isEmptyMap) {
				logger.debug("++ This map step in the flow is empty, create content or remove the map. ++");
				getContext().createLineViolation(this, "This map step in the flow is empty, "
						+ "create content or remove the map.", astNode);
			}
		}
	}
}
