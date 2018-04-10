package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types;

import java.util.Arrays;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum FlowAttIdentifierTypes implements TokenType {
  SIGNATURE("name","svc_sig"), SIGN_IN("name","sig_in"), SIGN_OUT("name","sig_out"), REC_FIELDS("name","rec_fields");

  private String attName;
  private String attValue;

  public static boolean isInEnum(String name, String value) {
    return Arrays.stream(FlowAttIdentifierTypes.values())
        .anyMatch(e -> (e.getAttName().equalsIgnoreCase(name) && e.getAttValue().equalsIgnoreCase(value)));
  }

  public static FlowAttIdentifierTypes getEnum(String name, String value) {
    return Arrays.stream(FlowAttIdentifierTypes.values())
        .filter(e -> (e.getAttName().equalsIgnoreCase(name) && e.getAttValue().equalsIgnoreCase(value))).findFirst().get();
  }

  private FlowAttIdentifierTypes(String name, String value) {
    this.attName = name;
    this.attValue = value;
  }

  public String getAttName() {
    return this.attName;
  }
  
  public String getAttValue() {
    return this.attValue;
  }

  @Override
  public String getName() {
    return name();
  }

  @Override
  public String getValue() {
    return null;
  }

  @Override
  public boolean hasToBeSkippedFromAst(AstNode node) {
    return false;
  }
}
