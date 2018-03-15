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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.settings.FlowLanguageProperties;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

/**
 * This class is the Flow Language definition. It is just a holder for some language specific
 * config.
 * 
 * @author DEWANST
 */
public class FlowLanguage extends AbstractLanguage {

  public static final String KEY = "flow";
  public static final String NAME = "flow";

  private static Configuration config;

  public FlowLanguage(Configuration config) {
    super(KEY, NAME);
    FlowLanguage.config = config;
  }

  @Override
  public String[] getFileSuffixes() {
    String[] suffixes = (String[]) ArrayUtils.addAll(getFlowFileSuffixes(), getNodeFileSuffixes());
    return suffixes;
  }

  private String[] getFlowFileSuffixes() {
    String[] suffixes = filterEmptyStrings(
        config.getStringArray(FlowLanguageProperties.FLOW_FILE_SUFFIXES_KEY));
    if (suffixes.length == 0) {
      suffixes = StringUtils.split(FlowLanguageProperties.FLOW_FILE_SUFFIXES_DEFVALUE, ",");
    }
    return suffixes;
  }

  private String[] getNodeFileSuffixes() {
    String[] suffixes = filterEmptyStrings(
        config.getStringArray(FlowLanguageProperties.NODE_FILE_SUFFIXES_KEY));
    if (suffixes.length == 0) {
      suffixes = StringUtils.split(FlowLanguageProperties.NODE_FILE_SUFFIXES_DEFVALUE, ",");
    }
    return suffixes;
  }

  /**
   * This methods returns the configured suffixes for flow files.
   * 
   * @return Array of suffixes for flow files
   */
  public static String[] getFlowFilePatterns() {
    String[] suffixes = filterEmptyStrings(
        config.getStringArray(FlowLanguageProperties.FLOW_FILE_FILTER_KEY));
    if (suffixes.length == 0) {
      suffixes = StringUtils.split(FlowLanguageProperties.FLOW_FILE_FILTER_DEFVALUE, ",");
    }
    return suffixes;
  }

  /**
   * This methods returns the configured suffixes for node files.
   * 
   * @return Array of suffixes for node files
   */
  public static String[] getNodeFilePatterns() {
    String[] suffixes = filterEmptyStrings(
        config.getStringArray(FlowLanguageProperties.NODE_FILE_FILTER_KEY));
    if (suffixes.length == 0) {
      suffixes = StringUtils.split(FlowLanguageProperties.NODE_FILE_FILTER_DEFVALUE, ",");
    }
    return suffixes;
  }

  private static String[] filterEmptyStrings(String[] stringArray) {
    List<String> nonEmptyStrings = new ArrayList<>();
    for (String string : stringArray) {
      if (StringUtils.isNotBlank(string.trim())) {
        nonEmptyStrings.add(string.trim());
      }
    }
    return nonEmptyStrings.toArray(new String[nonEmptyStrings.size()]);
  }

}
