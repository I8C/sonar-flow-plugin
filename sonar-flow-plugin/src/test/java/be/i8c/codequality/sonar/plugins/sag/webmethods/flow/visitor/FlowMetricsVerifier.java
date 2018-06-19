package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;
import java.util.Map;

import org.sonar.api.batch.fs.InputFile;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.impl.Parser;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowFileMetrics;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowParser;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowVisitorContext;

public class FlowMetricsVerifier {

  public static void verifyMetricsOnFile(InputFile file, int linesOfComments, int linesOfCode, int complexity, Map<Integer, String> dependencies) {
    FlowVisitorContext context = createContext(file);
    FlowFileMetrics metrics = new FlowFileMetrics(context);
    
    assertEquals(metrics.commentLines().size(), linesOfComments);
    assertEquals(metrics.linesOfCode().size(), linesOfCode);
    assertEquals(metrics.getComplexity(), Integer.valueOf(complexity));
    
    Map<Integer, String> depsFound = metrics.dependencies();
    for(int key : dependencies.keySet()) {
      boolean found = false;
      if(depsFound.containsKey(key) && depsFound.get(key).equals(dependencies.get(key))) {
        found=true;
      }
      assertTrue(found);
        
    }
  }

  private static FlowVisitorContext createContext(InputFile file) {
    Parser<Grammar> parser = FlowParser.create(UTF_8);
    String fileContent;
    try {
      fileContent = Files.toString((File) file, UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read " + file, e);
    }
    FlowVisitorContext context;
    try {
      context = new FlowVisitorContext(fileContent, parser.parse((File) file), file);
    } catch (RecognitionException e) {
      context = new FlowVisitorContext(fileContent, null, e, null);
    }
    return context;
}
}
