package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types;

import java.util.Arrays;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum FlowAttTypes implements TokenType {
  SERVICE, EXITON("EXIT-ON"), MODE, NAME, DISABLED, FROM, SIGNAL, FAILUREMESSAGE(
      "FAILURE-MESSAGE"), SWITCH, LABELEXPRESSIONS;

  private String attName;

  public static boolean isInEnum(String value) {
    return Arrays.stream(FlowAttTypes.values())
        .anyMatch(e -> e.getAttName().equalsIgnoreCase(value));
  }

  public static FlowAttTypes getEnum(String value) {
    return Arrays.stream(FlowAttTypes.values())
        .filter(e -> e.getAttName().equalsIgnoreCase(value)).findFirst().get();
  }

  private FlowAttTypes() {
    this.attName = name();
  }

  private FlowAttTypes(String name) {
    this.attName = name;
  }

  public String getAttName() {
    return this.attName;
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
