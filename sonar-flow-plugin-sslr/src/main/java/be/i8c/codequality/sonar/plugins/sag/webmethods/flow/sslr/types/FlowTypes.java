package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types;

import java.util.Arrays;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum FlowTypes implements TokenType {
  START_FLOW, STOP_FLOW, START_SEQUENCE, STOP_SEQUENCE, START_INVOKE, STOP_INVOKE,

  START_LOOP, STOP_LOOP, START_BRANCH, STOP_BRANCH, START_EXIT, STOP_EXIT,

  START_RETRY, STOP_RETRY, START_MAP, STOP_MAP, START_MAPTARGET, STOP_MAPTARGET,

  START_MAPSOURCE, STOP_MAPSOURCE, START_MAPDELETE, STOP_MAPDELETE, START_MAPSET, STOP_MAPSET,

  START_MAPCOPY, STOP_MAPCOPY, START_MAPINVOKE, STOP_MAPINVOKE, START_DATA, STOP_DATA,

  START_VALUES, STOP_VALUES, START_RECORD, STOP_RECORD, START_VALUE, STOP_VALUE,

  START_NUMBER, STOP_NUMBER, START_ARRAY, STOP_ARRAY, START_BOOLEAN, STOP_BOOLEAN,

  ELEMENT_VALUE(false), START_COMMENT, STOP_COMMENT;

  public static boolean isInEnum(String value) {
    return Arrays.stream(FlowTypes.values()).anyMatch(e -> e.name().equals(value));
  }

  private boolean skip = true;

  private FlowTypes() {
  }

  private FlowTypes(boolean skip) {
    this.skip = skip;
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
    return skip;
  }
}
