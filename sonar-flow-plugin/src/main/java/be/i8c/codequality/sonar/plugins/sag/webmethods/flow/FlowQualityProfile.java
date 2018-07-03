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

import static be.i8c.codequality.sonar.plugins.sag.webmethods.flow.rule.FlowRulesDefinition.REPO_KEY;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.rule.FlowRulesDefinition;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

/**
 * This class is the default quality profile that comes with this plugin.
 * @author DEWANST
 */
public class FlowQualityProfile implements BuiltInQualityProfilesDefinition {

  public static final String SONAR_WAY_PROFILE_PATH = "be/i8c/l10n/flow/rules/i8c_way_profile.json";
  public static final String I8C_FLOW_PROFILE = "i8c flow profile";

  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(FlowQualityProfile.I8C_FLOW_PROFILE, FlowLanguage.KEY);
    profile.setDefault(true);
    // ACTIVATE RULES
    for (String key : FlowRulesDefinition.getRuleKeys()) {
      profile.activateRule(REPO_KEY, key);
    }
    profile.done();
  }

}
