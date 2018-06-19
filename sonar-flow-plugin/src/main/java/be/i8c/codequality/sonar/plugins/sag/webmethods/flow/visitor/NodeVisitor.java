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

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;

/**
 * @author DEWANST
 * 
 * Abstract class which provides methods that scans the AST node 
 * and calls the visitNode and/or visitToken for subscribed AST nodes 
 */
public abstract class NodeVisitor {
  
  private NodeVisitorContext context;

  private Set<AstNodeType> subscribedKinds = null;

  public abstract List<AstNodeType> subscribedTo();

  private Set<AstNodeType> subscribedKinds() {
    if (subscribedKinds == null) {
      subscribedKinds = ImmutableSet.copyOf(subscribedTo());
    }
    return subscribedKinds;
  }

  public void visitFile(@Nullable AstNode node) {
  }

  public void leaveFile(@Nullable AstNode node) {
  }

  public void visitNode(AstNode node) {
  }

  public void leaveNode(AstNode node) {
  }

  public void visitToken(Token token) {
  }

  public NodeVisitorContext getContext() {
    return context;
  }

  public void scanFile(NodeVisitorContext context2) {
    this.context = context2;
    AstNode tree = context2.rootTree();
    visitFile(tree);
    if (tree != null) {
      scanNode(tree);
    }
    leaveFile(tree);
  }

  public void scanNode(AstNode node) {
    boolean isSubscribedType = subscribedKinds().contains(node.getType());

    if (isSubscribedType) {
      visitNode(node);
    }

    List<AstNode> children = node.getChildren();
    if (children.isEmpty()) {
      node.getTokens().forEach(this::visitToken);
    } else {
      children.forEach(this::scanNode);
    }

    if (isSubscribedType) {
      leaveNode(node);
    }
  }
}
