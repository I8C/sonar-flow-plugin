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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowAttTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowTypes;

/**
 * This visitor checks the AST of the flow file and collects metrics on it.
 * 
 * @author DEWANST
 *
 */
public class FlowMetricsVisitor extends FlowVisitor {

  private Set<Integer> linesOfComments = new HashSet<Integer>();
  private Set<Integer> linesOfCode = new HashSet<Integer>();
  private Map<Integer, String> dependencies = new HashMap<Integer, String>();
  
  List<FlowGrammar> linesOfCommentsSubscr = Arrays.asList(FlowGrammar.COMMENT);
  List<FlowGrammar> linesOfCodeSubscr = Arrays.asList(FlowGrammar.SEQUENCE, FlowGrammar.INVOKE, 
      FlowGrammar.BRANCH, FlowGrammar.MAP, FlowGrammar.LOOP, FlowGrammar.EXIT);
  List<FlowGrammar> dependenciesSubscr = Arrays.asList(FlowGrammar.INVOKE);

  @Override
  public List<AstNodeType> subscribedTo() {
    return Stream.concat(linesOfCommentsSubscr.stream(), linesOfCodeSubscr.stream())
        .collect(Collectors.toList());
  }

  @Override
  public void visitNode(AstNode astNode) {
    if(linesOfCommentsSubscr.contains(astNode.getType())) {
      checkLinesOfComments(astNode);
    }else if(linesOfCodeSubscr.contains(astNode.getType())) {
      checkLinesOfCode(astNode);
<<<<<<< HEAD
    }
    if(dependenciesSubscr.contains(astNode.getType())) {
=======
    }else if(dependenciesSubscr.contains(astNode.getType())) {
>>>>>>> refs/remotes/origin/dev03_refactor
      checkDependency(astNode);
    }
    
  }

  private void checkDependency(AstNode astNode) {
    String service = astNode.getFirstChild(FlowGrammar.ATTRIBUTES)
        .getFirstChild(FlowAttTypes.SERVICE).getTokenOriginalValue();
    dependencies.put(astNode.getTokenLine(), service);
  }

  private void checkLinesOfComments(AstNode astNode) {
    if (astNode.getParent().getType().equals(FlowGrammar.MAP)) {
      AstNode map = astNode.getParent();
      if (map.hasParent(FlowGrammar.INVOKE) || map.hasParent(FlowGrammar.MAPINVOKE)) {
        return;
      }
    } else {
      if (astNode.hasDirectChildren(FlowTypes.ELEMENT_VALUE)) {
        AstNode value = astNode.getFirstChild(FlowTypes.ELEMENT_VALUE);
        if (!value.getTokenOriginalValue().isEmpty()) {
          linesOfComments.add(value.getTokenLine());
        }
      }
    }
  }
  
  private void checkLinesOfCode(AstNode astNode) {
    if (astNode.getType().equals(FlowGrammar.MAP)
        && (astNode.getParent().getType().equals(FlowGrammar.INVOKE)
            || astNode.getParent().getType().equals(FlowGrammar.MAPINVOKE))) {
      return;
    } else {
      linesOfCode.add(astNode.getTokenLine());
    }
  }

  public Set<Integer> getLinesOfComments() {
    return linesOfComments;
  }
  
  public Set<Integer> getLinesOfCode() {
    return linesOfCode;
  }

  public Map<Integer, String> getDependencies() {
    return dependencies;
  }
}
