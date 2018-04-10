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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.type.FlowVisitor;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

import org.sonar.squidbridge.measures.MetricDef;

/**
 * This visitor counts the lines of flow code that contains comments (SEQUENCE, INVOKE, BRANCH, MAP,
 * LOOP and EXIT) and stores it in the metric that is given on initialization.
 * 
 * @author DEWANST
 *
 * @param <G>
 *          Grammar on which it applies
 */
public class FlowCommentLinesVisitor<G extends Grammar> extends FlowVisitor<G> {

  private final MetricDef metric;

  public FlowCommentLinesVisitor(MetricDef metric) {
    this.metric = metric;
  }

  @Override
  public void init() {
    subscribeTo(FlowGrammar.COMMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {

    if (astNode.getParent().getType().equals(FlowGrammar.MAP)) {
      AstNode map = astNode.getParent();
      if (map.hasParent(FlowGrammar.INVOKE) || map.hasParent(FlowGrammar.MAPINVOKE)) {
        return;
      }
    } else {
      if (astNode.hasDirectChildren(FlowTypes.ELEMENT_VALUE)) {
        AstNode value = astNode.getFirstChild(FlowTypes.ELEMENT_VALUE);
        if (!value.getTokenOriginalValue().isEmpty()) {
          getContext().peekSourceCode().add(metric, 1);
        }
      }
    }
  }

}
