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
package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitors;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.squidbridge.SquidAstVisitor;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowMetric;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowLexer.FlowAttTypes;

public class DependencyVisitor<GRAMMAR extends Grammar> extends SquidAstVisitor<GRAMMAR> {

	final static Logger logger = LoggerFactory.getLogger(DependencyVisitor.class);
	
	public DependencyVisitor() {
	}

	@Override
	public void init() {
		subscribeTo(FlowGrammar.INVOKE);
	}

	@Override
	public void visitNode(AstNode astNode) {
		String service = astNode.getFirstChild(FlowGrammar.ATTRIBUTES).getFirstChild(FlowAttTypes.SERVICE)
				.getTokenOriginalValue();
		ArrayList<String> dependencies = (ArrayList<String>) getContext().peekSourceCode()
				.getData(FlowMetric.DEPENDENCIES);
		if (dependencies == null)
			dependencies = new ArrayList<String>();
		dependencies.add(service);
		getContext().peekSourceCode().addData(FlowMetric.DEPENDENCIES, dependencies);
		logger.debug("**** Dependency found: " + service);
	}

}
