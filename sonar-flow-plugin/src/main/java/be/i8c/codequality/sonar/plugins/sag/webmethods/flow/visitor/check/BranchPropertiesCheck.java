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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowAttTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.annotations.CheckRemediation;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.annotations.CheckRuleType;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rules.RuleType;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

/**
 * Checks if branch component is set correctly when using switch flag.
 * 
 * @author DEWANST
 *
 */
@Rule(key = "S00009",
    name = "Checks \"evaluate labels\" and \"switch\" properties of a branch",
    priority = Priority.MAJOR,
    tags = {Tags.DEBUG_CODE, Tags.BAD_PRACTICE })
@CheckRuleType (ruletype = RuleType.BUG)
@CheckRemediation (func = "Constant", constantCost= "3min")
public class BranchPropertiesCheck extends FlowCheck {

  static final Logger logger = LoggerFactory.getLogger(BranchPropertiesCheck.class);

  @Override
  public void visitNode(AstNode astNode) {
    AstNode attributesNode = astNode.getFirstChild(FlowGrammar.ATTRIBUTES);

    AstNode switchAttribute = attributesNode.getFirstChild(FlowAttTypes.SWITCH);
    Boolean switchDefined = (switchAttribute == null
        || "".equals(switchAttribute.getTokenOriginalValue())) ? false : true;

    AstNode labelExpressions = attributesNode.getFirstChild(FlowAttTypes.LABELEXPRESSIONS);
    Boolean evaluateLabelsDefined = (labelExpressions == null
        || "false".equals(labelExpressions.getTokenOriginalValue())
        || "".equals(labelExpressions.getTokenOriginalValue())) ? false : true;

    if (switchDefined && evaluateLabelsDefined) {
      addIssue("Both switch and evaluate labels are defined in properties of BRANCH", astNode);
    }

    if (!switchDefined && !evaluateLabelsDefined) {
      addIssue("Evaluate labels must be true when no switch parameter is defined in BRANCH", astNode);
    }
  }

  @Override
  public List<AstNodeType> subscribedTo() {
    return new ArrayList<AstNodeType>(Arrays.asList(FlowGrammar.BRANCH));
  }

}
