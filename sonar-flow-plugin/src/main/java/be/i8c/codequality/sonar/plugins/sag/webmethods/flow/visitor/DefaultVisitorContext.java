package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor;

import javax.annotation.CheckForNull;

import org.sonar.api.batch.fs.InputFile;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.RecognitionException;

public class DefaultVisitorContext {

  private final String fileContent;
  private final AstNode rootTree;
  private final RecognitionException parsingException;
  private final InputFile inputFile;
  
  public DefaultVisitorContext(String fileContent, AstNode tree, InputFile inputFile) {
    this(fileContent, tree, null, inputFile);
  }

  protected DefaultVisitorContext(String fileContent, AstNode rootTree, RecognitionException parsingException, InputFile inputFile) {
    this.fileContent = fileContent;
    this.rootTree = rootTree;
    this.parsingException = parsingException;
    this.inputFile = inputFile;
  }

  @CheckForNull
  public AstNode rootTree() {
    return rootTree;
  }

  public RecognitionException parsingException() {
    return parsingException;
  }

  public String fileContent() {
    return fileContent;
  }

  public InputFile getInputFile() {
    return inputFile;
  }
  
}
