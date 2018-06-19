package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import static org.junit.Assert.assertNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import org.sonar.api.batch.fs.InputFile;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.impl.Parser;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.NodeFileMetrics;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.NodeParser;

public class NodeMetricsVerifier {

  public static void verifyMetricsOnFile(InputFile file) {
    NodeVisitorContext context = createContext(file);
    NodeFileMetrics metrics = new NodeFileMetrics(context);
    assertNotNull(metrics);
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
