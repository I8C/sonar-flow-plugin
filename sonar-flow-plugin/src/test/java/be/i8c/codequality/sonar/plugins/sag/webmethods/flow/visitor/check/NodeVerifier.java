package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import com.google.common.io.Files;
import java.util.List;
import java.util.Map.Entry;

import org.sonar.api.batch.fs.InputFile;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.impl.Parser;
import com.sonarsource.checks.verifier.CommentParser;
import com.sonarsource.checks.verifier.SingleFileVerifier;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.NodeParser;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.Issue;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.NodeCheck;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.NodeVisitorContext;

public class NodeVerifier {

  public static void verify(InputFile file, NodeCheck check) {
    createVerifier(file, check, true).assertOneOrMoreIssues();
  }

  public static void verifyNoIssue(InputFile file, NodeCheck check) {
    createVerifier(file, check, true).assertNoIssues();
  }

  public static void verifyNoIssueIgnoringExpected(InputFile file, NodeCheck check) {
    createVerifier(file, check, false).assertNoIssues();
  }

  public static void verifySingleIssueOnFile(InputFile file, NodeCheck check, String expectedIssueMessage, Integer lineNr) {
    List<Issue> issues = check.scanFileForIssues(createContext(file));
    assertEquals(issues.size(), 1);
    assertEquals(issues.get(0).line(), lineNr);
    assertEquals(issues.get(0).message(), expectedIssueMessage);
  }
  
  public static void verifyMultipleIssueOnFile(InputFile file, NodeCheck check, List<Entry<String,Integer>> expectedIssues) {
    List<Issue> issues = check.scanFileForIssues(createContext(file));
    assertEquals(issues.size(), expectedIssues.size());
    for(Entry<String,Integer> expectedIssue : expectedIssues) {
      boolean found = false;
      for(Issue issue: issues) {
        if (issue.message().equals(expectedIssue.getKey()) && issue.line().equals(expectedIssue.getValue())) {
          found=true;
        }
      }
      assertTrue(found);
        
    }
  }

  private static SingleFileVerifier createVerifier(InputFile file, NodeCheck check, boolean addCommentsAsExpectedIssues) {
    SingleFileVerifier verifier = SingleFileVerifier.create(((File) file).toPath(), UTF_8);

    NodeVisitorContext context = createContext(file);

    for (Issue issue : check.scanFileForIssues(context)) {
      SingleFileVerifier.IssueBuilder issueBuilder = verifier.reportIssue(issue.message());
      Integer line = issue.line();
      SingleFileVerifier.Issue verifierIssue;
      if (line != null) {
        verifierIssue = issueBuilder.onLine(line);
      } else {
        verifierIssue = issueBuilder.onFile();
      }
      verifierIssue.withGap(issue.cost());
    }

    if (addCommentsAsExpectedIssues) {
      CommentParser commentParser = CommentParser.create().addSingleLineCommentSyntax("//");
      commentParser.parseInto(((File) file).toPath(), verifier);
    }

    return verifier;
  }

  private static NodeVisitorContext createContext(InputFile file) {
    Parser<Grammar> parser = NodeParser.create(UTF_8);
    String fileContent;
    try {
      fileContent = Files.toString((File) file, UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read " + file, e);
    }
    NodeVisitorContext context;
    try {
      context = new NodeVisitorContext(fileContent, parser.parse((File) file), file);
    } catch (RecognitionException e) {
      context = new NodeVisitorContext(fileContent, null, e, null);
    }
    return context;
}
}
