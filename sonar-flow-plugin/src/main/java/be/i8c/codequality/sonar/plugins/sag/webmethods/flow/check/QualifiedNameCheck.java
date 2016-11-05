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

import java.util.regex.Pattern;

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
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.utils.FlowUtils;

@Rule(key = "S00005", name = "Flow assets should follow the predefined naming convention", priority = Priority.MAJOR, tags = {
		Tags.BAD_PRACTICE })
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("1min")
public class QualifiedNameCheck extends SquidCheck<Grammar>{

	final static Logger logger = LoggerFactory.getLogger(QualifiedNameCheck.class);
	
	private static final String DEFAULT_NC = "([A-Z]([A-Z0-9]*[a-z][a-z0-9]*[A-Z]|[a-z0-9]*[A-Z][A-Z0-9]*[a-z])[A-Za-z0-9]*)(\\.([a-z]+))+:[a-z]+[A-Z0-9][a-z0-9]*";

	@RuleProperty(
			key = "Naming convention",
			description = "Regular expression defining the naming convention of flow assets",
			defaultValue = "" + DEFAULT_NC)
	private String namingConvention = DEFAULT_NC;
	private Pattern p;
	
	@Override
	public void init() {
		logger.debug("++ Initializing " + this.getClass().getName() + " ++");
		p = Pattern.compile(namingConvention);
		subscribeTo(FlowGrammar.FLOW);
	}

	@Override
	public void visitNode(AstNode astNode) {
		String service = FlowUtils.getQualifiedName(this.getContext().getFile());
		logger.debug("Service found: " + service);
		if(!p.matcher(service).matches()){
			getContext().createLineViolation(this, "Flow name " + service + " does not conform to the naming convention", astNode);
		}
	}

}