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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;

public abstract class FlowCheck extends FlowVisitor {

  private List<Issue> issues = new ArrayList<>();

  public List<Issue> scanFileForIssues(FlowVisitorContext context) {
    issues = new ArrayList<>();
    scanFile(context);
    return Collections.unmodifiableList(issues);
  }

  public void addIssueAtLine(String message, int line) {
    issues.add(Issue.lineIssue(line, message));
  }

  public void addIssue(String message, AstNode node) {
    addIssueAtLine(message, node.getTokenLine());
  }

  public void addIssue(String message, Token token) {
    addIssueAtLine(message, token.getLine());
  }

  public void addFileIssue(String message) {
    issues.add(Issue.fileIssue(message));
  }

  public void addIssueWithCost(String message, AstNode node, double cost) {
    issues.add(Issue.lineIssue(node.getTokenLine(), message, cost));
  }
  
  public boolean isTopLevel() {
    return false;
  }
  
}
