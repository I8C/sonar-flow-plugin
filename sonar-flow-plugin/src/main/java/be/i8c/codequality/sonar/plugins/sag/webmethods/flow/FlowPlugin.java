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

import java.util.List;

import org.sonar.api.SonarPlugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import com.google.common.collect.ImmutableList;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.rule.FlowRulesDefinition;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.rule.FlowSquidSensor;

public class FlowPlugin extends SonarPlugin {

  public static final String FILE_SUFFIXES_KEY = "sonar.flow.file.suffixes";
  public static final String FILE_SUFFIXES_DEFVALUE = "xml,ndf";
  public static final String IGNORE_TOPLEVEL_KEY = "sonar.flow.ignore.toplevel";
  public static final String IGNORE_TOPLEVEL_DEFVALUE = "false";


  @Override
  public List<Object> getExtensions() {
    return ImmutableList.of(
      FlowLanguage.class,
      
      FlowProfile.class,
 
      FlowSquidSensor.class,
      
      FlowRulesDefinition.class,
      
      PropertyDefinition.builder(FILE_SUFFIXES_KEY)
        .defaultValue(FILE_SUFFIXES_DEFVALUE)
        .name("File Suffixes")
        .description("Comma-separated list of suffixes for files to analyze.")
        .onQualifiers(Qualifiers.PROJECT)
        .build(),
        
      PropertyDefinition.builder(IGNORE_TOPLEVEL_KEY)
        .defaultValue(IGNORE_TOPLEVEL_DEFVALUE)
        .name("Ignore top-level services")
        .description("Ignore top-level service checks")
        .onQualifiers(Qualifiers.PROJECT)
        .build()


    );
  }

}
