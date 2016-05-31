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

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.squidbridge.annotations.AnnotationBasedProfileBuilder;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.check.CheckList;

public class FlowProfile extends ProfileDefinition {

	private final RuleFinder ruleFinder;

	  public FlowProfile(RuleFinder ruleFinder) {
	    this.ruleFinder = ruleFinder;
	  }

	  @Override
	  public RulesProfile createProfile(ValidationMessages validation) {
	    AnnotationBasedProfileBuilder annotationBasedProfileBuilder = new AnnotationBasedProfileBuilder(ruleFinder);
	    return annotationBasedProfileBuilder.build(
	        CheckList.REPOSITORY_KEY,
	        CheckList.SONAR_WAY_PROFILE,
	        FlowLanguage.KEY,
	        CheckList.getChecks(),
	        validation);
	  }

}
