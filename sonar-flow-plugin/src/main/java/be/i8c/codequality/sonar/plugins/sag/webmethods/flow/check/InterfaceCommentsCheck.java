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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.type.NodeCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowLexer.FlowTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.NodeGrammar;

@Rule(key = "S00004", name = "Interfaces of services should contain comments", priority = Priority.MINOR, tags = {
		Tags.BAD_PRACTICE })
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("2min")
public class InterfaceCommentsCheck extends NodeCheck{

	final static Logger logger = LoggerFactory.getLogger(InterfaceCommentsCheck.class);
	
	@Override
	public void init() {
		logger.debug("++ Initializing " + this.getClass().getName() + " ++");
		subscribeTo(NodeGrammar.VALUE);
	}

	@Override
	public void visitNode(AstNode astNode) {
		for(AstNode attr:astNode.getChildren(NodeGrammar.ATTRIBUTES)){
			if(attr.getTokenValue().equals("NODE_COMMENT")){
				logger.debug("++ Comment found ++");
				if(astNode.getChildren(FlowTypes.ELEMENT_VALUE).size()<=0){
					logger.debug("++ Comment VIOLATION found ++");
					getContext().createLineViolation(this, "Add comment", astNode);
				}
			}
		}
	}

}