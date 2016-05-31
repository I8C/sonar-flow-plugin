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

import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.measures.MetricDef;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;

public class FlowLinesOfCodeVisitor<GRAMMAR extends Grammar> extends SquidAstVisitor<GRAMMAR> {

	private final MetricDef metric;

	  public FlowLinesOfCodeVisitor(MetricDef metric) {
	    this.metric = metric;
	  }

	  @Override
	  public void init() {
	    subscribeTo(
		    FlowGrammar.SEQUENCE,
		    FlowGrammar.INVOKE,
		    FlowGrammar.BRANCH,
		    FlowGrammar.MAP,
		    FlowGrammar.LOOP,
		    FlowGrammar.EXIT
	    	);
	  }

	  @Override
	  public void visitNode(AstNode astNode) {
		  if(astNode.getType().equals(FlowGrammar.MAP) 
				  && (astNode.getParent().getType().equals(FlowGrammar.INVOKE)
						  || astNode.getParent().getType().equals(FlowGrammar.MAPINVOKE)))
			  return;
		  else
	    	getContext().peekSourceCode().add(metric, 1);
	  }

}
