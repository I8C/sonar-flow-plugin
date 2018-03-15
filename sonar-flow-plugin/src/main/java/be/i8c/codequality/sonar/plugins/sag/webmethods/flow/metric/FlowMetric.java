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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric;

import org.sonar.squidbridge.measures.CalculatedMetricFormula;
import org.sonar.squidbridge.measures.MetricDef;

/**
 * An enum of flow metrics.
 * @author DEWANST
 */
public enum FlowMetric implements MetricDef {

  FLOWS, 
  INVOKES, 
  SEQUENCES, 
  LOOPS, 
  BRANCHES, 
  MAPS, 
  LINES, 
  FILES, 
  LINES_OF_CODE, 
  COMPLEXITY, 
  COMMENT_LINES, 
  STATEMENTS, 
  DEPENDENCIES, 
  IS_TOP_LEVEL;

  public boolean aggregateIfThereIsAlreadyAValue() {
    return true;
  }

  public CalculatedMetricFormula getCalculatedMetricFormula() {
    return null;
  }

  public String getName() {
    return name();
  }

  public boolean isCalculatedMetric() {
    return false;
  }

  public boolean isThereAggregationFormula() {
    return true;
  }

}
