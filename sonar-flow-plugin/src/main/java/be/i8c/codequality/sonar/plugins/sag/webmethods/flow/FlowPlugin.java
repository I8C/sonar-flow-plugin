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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowMetrics;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.rule.FlowRulesDefinition;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.rule.NodeRulesDefinition;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.settings.FlowLanguageProperties;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.web.FlowWebPageDefinition;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.ws.sources.FlowWebWs;

import org.sonar.api.Plugin;
import org.sonar.api.internal.google.common.collect.ImmutableList;

/**
 * This class represents the Flow plugin.
 * It holds references (extensions) to the FlowLanguage, its properties,
 * the sensor and rule definitions.
 * @author DEWANST
 */
public class FlowPlugin implements Plugin {

  @Override
  public void define(Context context) {
    ImmutableList.Builder<Object> builder = ImmutableList.builder();
    
    // add language with default profile
    builder.add(FlowLanguage.class, FlowQualityProfile.class);
    // add language settings
    builder.addAll(FlowLanguageProperties.getProperties());
    // add metrics
    builder.add(FlowMetrics.class);
    // add rules
    builder.add(FlowRulesDefinition.class, NodeRulesDefinition.class, FlowSquidSensor.class);
    // add rules properties
    builder.addAll(FlowRulesDefinition.getProperties());
    builder.addAll(NodeRulesDefinition.getProperties());
    // add web extensions
    builder.add(FlowWebPageDefinition.class);
    builder.add(FlowWebWs.class);
    
    context.addExtensions(builder.build());
  }

}
