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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.rule;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.FlowLanguage;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.CheckList;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.AnnotationUtils;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheckProperty;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheckRuleType;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.internal.apachecommons.lang.StringUtils;
import org.sonar.api.internal.google.common.annotations.VisibleForTesting;
import org.sonar.api.internal.google.common.collect.Iterables;
import org.sonar.api.internal.google.common.io.Resources;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.squidbridge.annotations.RuleTemplate;

/**
 * This class creates the repository that holds the rules for this language.
 * @author DEWANST
 */
/**
 * @author DEWANST
 *
 */
public class FlowRulesDefinition implements RulesDefinition {

  private static final String RESOURCE_BASE_PATH = "/org/sonar/l10n/flow/rules/flow";

  protected static final String KEY = "i8cFlow";
  protected static final String NAME = "I8cFlow";

  public static final String REPO_KEY = FlowLanguage.KEY + "-" + KEY;
  protected static final String REPO_NAME = FlowLanguage.KEY + "-" + NAME;

  private static List<String> ruleKeys = new ArrayList<String>();
  private static List<Object> ruleProperties = null;
  
  private void defineRulesForLanguage(Context context, String repositoryKey, String repositoryName,
      String languageKey) {
    NewRepository repository = context.createRepository(repositoryKey, languageKey)
        .setName(repositoryName);
    List<Class<? extends FlowCheck>> checks = CheckList.getChecks();
    new RulesDefinitionAnnotationLoader().load(repository, Iterables.toArray(checks, Class.class));
    for (Class<? extends FlowCheck> check : checks) {
      newRule(check, repository);
    }
    repository.done();
  }

  
  /**
   * Converts the check to a rule by reading the params
   * @param check
   * @param repository
   */
  @VisibleForTesting
  protected void newRule(Class<? extends FlowCheck> check, NewRepository repository) {
    // Read the rule annotation of the check
    org.sonar.check.Rule ruleAnnotation = AnnotationUtils.getAnnotation(check,
        org.sonar.check.Rule.class);
    if (ruleAnnotation == null) {
      throw new IllegalArgumentException("No Rule annotation was found on " + check.getName());
    }
    // Read the ruleType annotation of the check
    FlowCheckRuleType ruleTypeAnnotation = AnnotationUtils.getAnnotation(check,
        FlowCheckRuleType.class);
    RuleType rt;
    if (ruleTypeAnnotation == null) {
      rt = RuleType.CODE_SMELL;
    }else {
      rt = ruleTypeAnnotation.ruletype();
    }
    // Add to repo
    String ruleKey = ruleAnnotation.key();
    if (StringUtils.isEmpty(ruleKey)) {
      throw new IllegalArgumentException("No key is defined in Rule annotation of " + check.getName());
    }
    NewRule rule = repository.rule(ruleKey);
    if (rule == null) {
      throw new IllegalStateException(
          "No rule was created for " + check + " in " + repository.key());
    }
    ruleKeys.add(rule.key());
    // Set html template
    addHtmlDescription(rule, ruleKey);
    rule.setTemplate(AnnotationUtils.getAnnotation(check, RuleTemplate.class) != null);
    // Set the type of the rule, instead of working with tags
    rule.setType(rt);
  }

  /**
   * Create properties from annotation and add to the list
   * @param propertyAnnotations
   */
  private static void addRuleProperties(FlowCheckProperty[] propertyAnnotations) {
    for(FlowCheckProperty fcp:propertyAnnotations) {
      ruleProperties.add(
          PropertyDefinition.builder(fcp.key()).defaultValue(fcp.defaultValue())
            .name(fcp.name())
            .type(fcp.type())
            .category(fcp.category())
            .subCategory(fcp.subCategory())
            .description(fcp.description())
            .onQualifiers(fcp.onQualifiers())
          .build());
    }
  }


  private void addHtmlDescription(NewRule rule, String metadataKey) {
    URL resource = FlowRulesDefinition.class
        .getResource(RESOURCE_BASE_PATH + "/" + metadataKey + ".html");
    if (resource != null) {
      rule.setHtmlDescription(readResource(resource));
    }
  }

  private String readResource(URL resource) {
    try {
      return Resources.toString(resource, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read: " + resource, e);
    }
  }

  @Override
  public void define(Context context) {
    defineRulesForLanguage(context, REPO_KEY, REPO_NAME, FlowLanguage.KEY);
  }

  
  /**
   * @return List of keys of the rules in the repo
   */
  public static List<String> getRuleKeys() {
    return ruleKeys;
  }
  
  /**
   * Get the list of rule properties.
   * @return list of properties
   */
  public static List<Object> getProperties() {
    if(ruleProperties==null) {
      ruleProperties = new ArrayList<Object>();
      List<Class<? extends FlowCheck>> checks = CheckList.getChecks();
      for (Class<? extends FlowCheck> check : checks) {
        // Read the property annotations of the check
        FlowCheckProperty[] propertyAnnotations = AnnotationUtils.getAnnotations(check,
            FlowCheckProperty.class);
        if (propertyAnnotations != null) {
          addRuleProperties(propertyAnnotations);
        }
      }
    }
    return ruleProperties;
  }

}
