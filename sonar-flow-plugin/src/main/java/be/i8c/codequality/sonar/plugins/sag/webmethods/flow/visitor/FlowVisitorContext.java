package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import org.sonar.api.batch.fs.InputFile;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.RecognitionException;

public class FlowVisitorContext extends DefaultVisitorContext{

  public FlowVisitorContext(String fileContent, AstNode rootTree, InputFile inputFile) {
    super(fileContent, rootTree, inputFile);
  }

  public FlowVisitorContext(String fileContent, AstNode rootTree, RecognitionException parsingException, InputFile inputFile) {
    super(fileContent, rootTree, parsingException, inputFile);
  }
  
}
