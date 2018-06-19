package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;

public class TestFile extends File implements InputFile{
  private static final long serialVersionUID = 1L;

  public TestFile(String pathname) {
    super(pathname);
    // TODO Auto-generated constructor stub
  }


  @Override
  public URI uri() {
    return super.toURI();
  }

  @Override
  public String filename() {
    return super.getName();
  }

  @Override
  public String key() {
    return "test";
  }

  @Override
  public String relativePath() {
    return super.getPath();
  }

  @Override
  public String absolutePath() {
    return super.getAbsolutePath();
  }

  @Override
  public File file() {
    return (File)this;
  }

  @Override
  public Path path() {
    return super.toPath();
  }

  @Override
  public String language() {
    return "";
  }

  @Override
  public Type type() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public InputStream inputStream() throws IOException {
    return new FileInputStream((File)this);
  }

  @Override
  public String contents() throws IOException {
    return new String(Files.readAllBytes(this.path()));
  }

  @Override
  public Status status() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int lines() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isEmpty() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public TextPointer newPointer(int line, int lineOffset) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TextRange newRange(TextPointer start, TextPointer end) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TextRange newRange(int startLine, int startLineOffset, int endLine, int endLineOffset) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TextRange selectLine(int line) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Charset charset() {
    // TODO Auto-generated method stub
    return null;
  }



 

}
