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

import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.PropertyType;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.rules.RuleType;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

import com.sonar.sslr.api.AstNode;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.FlowLanguage;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.settings.FlowLanguageProperties;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.NodeGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.utils.FlowUtils;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheckProperty;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheckRuleType;

/**
 * Checks if the stateless flag is set to true.
 * 
 * @author DEWANST
 *
 */
@Rule(key = "S00010", name = "Flow services should have stateless flag set to true", 
    priority = Priority.MINOR,
    tags = {Tags.BAD_PRACTICE })
@SqaleConstantRemediation("1min")
@FlowCheckProperty(category = FlowLanguageProperties.FLOW_CATEGORY,
    defaultValue = "false", 
    description = "enable filter", 
    key = StatelessFlagCheck.FILTER_ENABLE_KEY, 
    name = "Stateless flag check skip filter", 
    onQualifiers = Qualifiers.PROJECT, 
    subCategory = FlowLanguageProperties.FLOW_SUBCATEGORY_CHECKS, 
    type = PropertyType.BOOLEAN)
@FlowCheckProperty(category = FlowLanguageProperties.FLOW_CATEGORY,
    defaultValue = StatelessFlagCheck.FILTER_REGEX_DEFVALUE, 
    description = "QN that comply to this regex, skip this filter", 
    key = StatelessFlagCheck.FILTER_REGEX_KEY, 
    name = "Stateless flag check skip filter", 
    onQualifiers = Qualifiers.PROJECT, 
    subCategory = FlowLanguageProperties.FLOW_SUBCATEGORY_CHECKS, 
    type = PropertyType.REGULAR_EXPRESSION)
@FlowCheckRuleType (ruletype = RuleType.CODE_SMELL)
public class StatelessFlagCheck extends FlowCheck {

  static final Logger logger = LoggerFactory.getLogger(StatelessFlagCheck.class);

  static final String FILTER_ENABLE_KEY = "sonar.flow.check.stateless.filter.enable";
  static final String FILTER_REGEX_KEY = "sonar.flow.check.stateless.filter.regex";
  static final String FILTER_REGEX_DEFVALUE 
      = "[A-Z][a-z0-9]*[A-Z0-9][a-z0-9]+[A-Z.a-z0-9]*:[a-z]+[A-Z0-9][a-z0-9]+[A-Za-z0-9]*";
  
  private String filterEnabled;
  private String skipFilter;
  private Pattern pattern;
  
  @Override
  public void init() {
    logger.debug("++ Initializing {} ++", this.getClass().getName());
    subscribeTo(NodeGrammar.STATELESS);
  }
  
  @Override
  public void visitFile(@Nullable AstNode astNode) {
    filterEnabled = FlowLanguage.getConfig().get(FILTER_ENABLE_KEY).get();
    skipFilter = FlowLanguage.getConfig().get(FILTER_REGEX_KEY).get();
    pattern = Pattern.compile(skipFilter);
  }
  
  @Override
  public void visitNode(AstNode astNode) {
    String service = FlowUtils.getQualifiedName(this.getContext().getFile());
    logger.debug("++ CHECKING Stateless flag: " + filterEnabled + " ++");
    if (!Boolean.valueOf(filterEnabled) || !pattern.matcher(service).matches()) {
      logger.debug("++ Stateless flag Filter passed++");
      if(astNode.getFirstChild(FlowTypes.ELEMENT_VALUE).getTokenOriginalValue().trim().equals("no")) {
        logger.debug("++ Stateless flag VIOLATION found: " + astNode.getTokenLine() + " ++");
        getContext().createLineViolation(this, "Set stateless flag to \"true\"", astNode);
      }
    }

  }

  @Override
  public boolean isFlowCheck() {
    return false;
  }

  @Override
  public boolean isNodeCheck() {
    return true;
  }

  @Override
  public boolean isTopLevelCheck() {
    return false;
  }
}