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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowMetric;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowLexer.FlowAttTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.type.FlowVisitor;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This visitor keeps a list of flow services it depends on.
 * These services are stored in the FlowMetric.DEPENDENCIES
 * 
 * @author DEWANST
 *
 * @param <G>
 * Grammar on which it applies
 */
public class FlowDependencyVisitor<G extends Grammar> extends FlowVisitor<G> {

  static final Logger logger = LoggerFactory.getLogger(FlowDependencyVisitor.class);

  public FlowDependencyVisitor() {
  }

  @Override
  public void init() {
    subscribeTo(FlowGrammar.INVOKE);
  }

  @Override
  public void visitNode(AstNode astNode) {
    String service = astNode.getFirstChild(FlowGrammar.ATTRIBUTES)
        .getFirstChild(FlowAttTypes.SERVICE).getTokenOriginalValue();
    @SuppressWarnings("unchecked")
    ArrayList<String> dependencies = (ArrayList<String>) getContext().peekSourceCode()
        .getData(FlowMetric.DEPENDENCIES);
    if (dependencies == null) {
      dependencies = new ArrayList<String>();
    }
    dependencies.add(service);
    getContext().peekSourceCode().addData(FlowMetric.DEPENDENCIES, dependencies);
    logger.debug("**** Dependency found: " + service);
  }

}
