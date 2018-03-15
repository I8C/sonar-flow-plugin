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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.internal.SensorStorage;
import org.sonar.api.batch.sensor.issue.IssueLocation;
import org.sonar.api.batch.sensor.issue.internal.DefaultIssue;
import org.sonar.api.rule.RuleKey;

public class FlowIssueTest {

  static final Logger logger = LoggerFactory.getLogger(FlowIssueTest.class);

  @Test
  public void testIssueCreation() {
    TestInputFileBuilder tifb = new TestInputFileBuilder("module", "relPath");
    tifb.setLines(3);
    tifb.setOriginalLineOffsets(new int[] { 0, 10, 15 });
    tifb.setLastValidOffset(25);
    DefaultInputFile file = tifb.build();
    RuleKey ruleKey = RuleKey.of("squid", "ruleKey");
    SensorContext sensorContext = mock(SensorContext.class);
    SensorStorage storage = mock(SensorStorage.class);
    DefaultIssue newIssueOnFile = new DefaultIssue(storage);
    DefaultIssue newIssueOnLine = new DefaultIssue(storage);
    Mockito.when(sensorContext.newIssue()).thenReturn(newIssueOnFile, newIssueOnLine);

    // issue on file
    FlowIssue flowIssue = FlowIssue.create(sensorContext, ruleKey, null);
    flowIssue.setPrimaryLocationOnFile(file, "file message");
    flowIssue.save();

    Mockito.verify(storage, Mockito.times(1)).store(newIssueOnFile);
    assertThat(newIssueOnFile.ruleKey(),is(equalTo(ruleKey)));
    IssueLocation location = newIssueOnFile.primaryLocation();
    assertThat(location.inputComponent(),is(equalTo(file)));
    assertThat(location.textRange(),is(nullValue()));
    assertThat(location.message(),is(equalTo("file message")));

    // issue on entire line
    flowIssue = FlowIssue.create(sensorContext, ruleKey, null);
    flowIssue.setPrimaryLocation(file, "line message", 2, -1, 2, -1);
    flowIssue.save();

    Mockito.verify(storage, Mockito.times(1)).store(newIssueOnLine);
    assertLocation(newIssueOnLine.primaryLocation(), file, "line message", 2, 0, 2, 4);
  }

  private static void assertLocation(IssueLocation location, InputFile file, 
      String message, int startLine, int startOffset, int endLine, int endOffset) {
    assertThat(location.inputComponent(),is(equalTo(file)));
    assertThat(location.message(),is(equalTo(message)));
    TextRange textRange = location.textRange();
    TextPointer start = textRange.start();
    assertThat(start.line(),is(equalTo(startLine)));
    assertThat(start.lineOffset(),is(equalTo(startOffset)));
    TextPointer end = textRange.end();
    assertThat(end.line(),is(equalTo(endLine)));
    assertThat(end.lineOffset(),is(equalTo(endOffset)));
  }
}
