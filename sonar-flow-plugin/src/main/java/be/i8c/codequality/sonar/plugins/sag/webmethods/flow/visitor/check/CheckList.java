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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.NodeCheck;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Helper class that holds all checks.
 * @author DEWANST
 *
 */
public class CheckList {
  
  /**
   * Returns checks that apply to nodes.
   * @return
   */
  public static List<Class<? extends NodeCheck>> getNodeChecks() {
    return ImmutableList.<Class<? extends NodeCheck>>builder()
        .add(InterfaceCommentsCheck.class)
        .add(StatelessFlagCheck.class)
        .build();
  }
  
  /**
   * Returns checks that apply to top-level flows.
   * @return
   */
  public static List<Class<? extends FlowCheck>> getTopLevelChecks() {
    return ImmutableList.<Class<? extends FlowCheck>>builder()
        .add(TryCatchCheck.class).build();
  }

  /**
   * Returns checks that apply to flows.
   * @return
   */
  public static List<Class<? extends FlowCheck>> getFlowChecks() {
    return ImmutableList.<Class<? extends FlowCheck>>builder()
        .add(QualifiedNameCheck.class)
        .add(SavePipelineCheck.class)
        .add(DisabledCheck.class)
        .add(ExitCheck.class)
        .add(EmptyMapCheck.class)
        .add(BranchPropertiesCheck.class)
        .add(EmptyFlowCheck.class)
        .build();
  }
}
