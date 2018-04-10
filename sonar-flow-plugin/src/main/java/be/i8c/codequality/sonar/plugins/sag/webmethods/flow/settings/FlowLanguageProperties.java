/*
 * i8c
 * Copyright (C) 2018 i8c NV
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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.settings;

import java.util.List;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.internal.google.common.collect.ImmutableList;
import org.sonar.api.resources.Qualifiers;

/**
 * Holds only static methods to retrieve properties for this plugin.
 * @author DEWANST
 *
 */
public class FlowLanguageProperties {

  public static final String FLOW_CATEGORY = "flow";
  public static final String FLOW_SUBCATEGORY_FLOW = "Flow files";
  public static final String FLOW_SUBCATEGORY_NODE = "Node files";
  public static final String FLOW_SUBCATEGORY_SCANNER = "Scanner";
  public static final String FLOW_SUBCATEGORY_CHECKS = "Cheks";
  
  public static final String FLOW_FILE_SUFFIXES_KEY = "sonar.flow.file.suffixes";
  public static final String FLOW_FILE_SUFFIXES_DEFVALUE = "xml";
  
  public static final String FLOW_FILE_FILTER_KEY = "sonar.flow.file.filter";
  public static final String FLOW_FILE_FILTER_DEFVALUE = "**/*flow.xml";
  
  public static final String NODE_FILE_SUFFIXES_KEY = "sonar.node.file.suffixes";
  public static final String NODE_FILE_SUFFIXES_DEFVALUE = "ndf";
  
  public static final String NODE_FILE_FILTER_KEY = "sonar.node.file.filter";
  public static final String NODE_FILE_FILTER_DEFVALUE = "**/*node.ndf";
  
  public static final String IGNORE_TOPLEVEL_KEY = "sonar.flow.ignore.toplevel";
  public static final String IGNORE_TOPLEVEL_DEFVALUE = "false";
  
  public static final String FAIL_ON_SCANERROR = "sonar.flow.fail.scanerror";
  public static final String FAIL_ON_SCANERROR_DEFVALUE = "true";

  private FlowLanguageProperties() {
    // only statics
  }

  /**
   * Get the list of language properties.
   * @return list of properties
   */
  public static List<Object> getProperties() {
    ImmutableList.Builder<Object> props = ImmutableList.builder();
    props.add(
        PropertyDefinition.builder(FLOW_FILE_SUFFIXES_KEY).defaultValue(FLOW_FILE_SUFFIXES_DEFVALUE)
            .name("Flow file suffixes")
            .type(PropertyType.STRING)
            .category(FLOW_CATEGORY)
            .subCategory(FLOW_SUBCATEGORY_FLOW)
            .description("Comma-separated list extensions for flow files to analyze.")
            .onQualifiers(Qualifiers.PROJECT).build())
        .add(
        PropertyDefinition.builder(NODE_FILE_SUFFIXES_KEY).defaultValue(NODE_FILE_SUFFIXES_DEFVALUE)
            .name("Node file suffixes")
            .type(PropertyType.STRING)
            .category(FLOW_CATEGORY)
            .subCategory(FLOW_SUBCATEGORY_NODE)
            .description("Comma-separated list of extensions for node files to analyze.")
            .onQualifiers(Qualifiers.PROJECT).build())
        .add(
        PropertyDefinition.builder(FLOW_FILE_FILTER_KEY).defaultValue(FLOW_FILE_FILTER_DEFVALUE)
            .name("Flow file pattern")
            .type(PropertyType.REGULAR_EXPRESSION)
            .category(FLOW_CATEGORY)
            .subCategory(FLOW_SUBCATEGORY_FLOW)
            .description("Regex pattern for flow files to analyze.")
            .onQualifiers(Qualifiers.PROJECT).build())
        .add(
        PropertyDefinition.builder(NODE_FILE_FILTER_KEY).defaultValue(NODE_FILE_FILTER_DEFVALUE)
            .name("Node file patterns")
            .type(PropertyType.REGULAR_EXPRESSION)
            .category(FLOW_CATEGORY)
            .subCategory(FLOW_SUBCATEGORY_NODE)
            .description("Regex patterns for node files to analyze.")
            .onQualifiers(Qualifiers.PROJECT).build(),
        PropertyDefinition.builder(IGNORE_TOPLEVEL_KEY).defaultValue(IGNORE_TOPLEVEL_DEFVALUE)
            .name("Ignore top-level services")
            .type(PropertyType.BOOLEAN)
            .category(FLOW_CATEGORY)
            .subCategory(FLOW_SUBCATEGORY_CHECKS)
            .description("Ignore top-level service checks")
            .onQualifiers(Qualifiers.PROJECT).build())
        .add(
        PropertyDefinition.builder(FAIL_ON_SCANERROR).defaultValue(FAIL_ON_SCANERROR_DEFVALUE)
            .name("Fail on scan error")
            .type(PropertyType.BOOLEAN)
            .category(FLOW_CATEGORY)
            .subCategory(FLOW_SUBCATEGORY_SCANNER)
            .description("Scanning process fails when a scanning a single file fails")
            .onQualifiers(Qualifiers.PROJECT).build());
    return props.build();
  }
}
