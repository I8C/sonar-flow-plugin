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

import javax.annotation.Nullable;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputPath;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.squidbridge.api.CheckMessage;

/**
 * A helper class to create issues for flow files.
 * @author DEWANST
 */
public class FlowIssue {

  private final NewIssue newIssue;

  public FlowIssue(NewIssue newIssue) {
    this.newIssue = newIssue;
  }

  public static FlowIssue create(SensorContext context, RuleKey ruleKey,
      @Nullable Double effortToFix) {
    NewIssue newIssue = context.newIssue().forRule(ruleKey).gap(effortToFix);
    return new FlowIssue(newIssue);
  }

  public FlowIssue setPrimaryLocationOnFile(InputPath file, String message) {
    newIssue.at(newIssue.newLocation().on(file).message(message));
    return this;
  }

  /**
   * Sets the primary location.
   * @param file
   * Reference to the file
   * @param message
   * Message that was raised
   * @return this flowIssue
   */
  public FlowIssue setPrimaryLocation(InputFile file, CheckMessage message) {
    NewIssueLocation newIssueLocation;
    newIssueLocation = newIssue.newLocation().on(file).at(file.selectLine(message.getLine()))
        .message(message.getDefaultMessage());
    newIssue.at(newIssueLocation);
    return this;
  }

  /**
   * Sets the primary location.
   * @param file
   * Reference to the file
   * @param message
   * Message that was raised
   * @param startLine
   * line to start
   * @param startLineOffset
   * offset on startline
   * @param endLine
   * line to end
   * @param endLineOffset
   * offset on endline
   * @return this flowIssue
   */
  public FlowIssue setPrimaryLocation(InputFile file, String message, int startLine,
      int startLineOffset, int endLine, int endLineOffset) {
    NewIssueLocation newIssueLocation;
    if (startLineOffset == -1) {
      newIssueLocation = newIssue.newLocation().on(file).at(file.selectLine(startLine))
          .message(message);
    } else {
      newIssueLocation = newIssue.newLocation().on(file)
          .at(file.newRange(startLine, startLineOffset, endLine, endLineOffset)).message(message);
    }
    newIssue.at(newIssueLocation);
    return this;
  }
  
  /**
   * Sets the secondary location.
   * @param file
   * Reference to the file
   * @param message
   * Message that was raised
   * @param startLine
   * line to start
   * @param startLineOffset
   * offset on startline
   * @param endLine
   * line to end
   * @param endLineOffset
   * offset on endline
   * @return this flowIssue
   */
  public FlowIssue addSecondaryLocation(InputFile file, String message, int startLine,
      int startLineOffset, int endLine, int endLineOffset) {
    newIssue.addLocation(newIssue.newLocation().on(file)
        .at(file.newRange(startLine, startLineOffset, endLine, endLineOffset)).message(message));
    return this;
  }

  public void save() {
    newIssue.save();
  }
}
