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
import org.sonar.check.RuleProperty;

/**
 * Checks for pipeline related flow services.
 * e.g. pub.flow:savePipeline
 * @author DEWANST
 *
 */
@Rule(key = "S00002",
    name = "No invokes of savePipeline or restorePipeline should be in code",
    priority = Priority.MAJOR,
    tags = {Tags.DEBUG_CODE, Tags.BAD_PRACTICE })
@CheckRemediation (func = "Constant", constantCost= "2min")
@CheckRuleType (ruletype = RuleType.CODE_SMELL)
public class SavePipelineCheck extends FlowCheck {

  static final Logger logger = LoggerFactory.getLogger(SavePipelineCheck.class);

  private static final String DEFAULT_PS = "pub.flow:savePipeline,pub.flow:savePipelineToFile,"
      + "pub.flow:restorePipeline,pub.flow:restorePipelineFromFile";

  @RuleProperty(key = "pipelineServices",
      description = "Comma-seperated list of pipeline services",
      defaultValue = "" + DEFAULT_PS)
  private String pipelineServices = DEFAULT_PS;


  @Override
  public void visitNode(AstNode astNode) {
    String service = astNode.getFirstChild(FlowGrammar.ATTRIBUTES)
        .getFirstChild(FlowAttTypes.SERVICE).getToken().getOriginalValue();
    logger.debug("Invoke of service found: " + service);
    if (Arrays.asList(pipelineServices.split(",")).contains(service)) {
      addIssue("Remove service " + service, astNode);
    }
  }

  @Override
  public List<AstNodeType> subscribedTo() {
    return new ArrayList<AstNodeType>(Arrays.asList(FlowGrammar.INVOKE));
  }
}