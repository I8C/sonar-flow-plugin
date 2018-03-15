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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.channels.sax;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowLexer;

import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Lexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

public class FlowContentHandler extends DefaultHandler {

  final Logger logger = LoggerFactory.getLogger(getClass());
  private Lexer lex;
  private Token.Builder tokenBuilder;
  private Locator locator;
  private boolean ignoreWs = true;
  private StringBuilder chars = new StringBuilder();
  private int charsLine = 0;
  private int charsColumn = 0;

  public FlowContentHandler(Lexer lex, Token.Builder tokenBuilder) {
    this.lex = lex;
    this.tokenBuilder = tokenBuilder;
  }

  public void setDocumentLocator(Locator locator) {
    this.locator = locator;
  }

  @Override
  public void startElement(String uri, String name, String qualifiedName, Attributes atts) {
    int line = locator.getLineNumber();
    int column = locator.getColumnNumber();
    if (FlowLexer.FlowTypes.isInEnum("START_" + name.toUpperCase())) {
      logger.debug("Start element: " + qualifiedName + "[" + line + "," + column + "]" + "[START_"
          + name.toUpperCase() + "]");
      Token token = tokenBuilder.setType(FlowLexer.FlowTypes.valueOf("START_" + name.toUpperCase()))
          .setValueAndOriginalValue(name.toUpperCase(), name).setURI(lex.getURI()).setLine(line)
          .setColumn(0).build();
      lex.addToken(token);
      // CHECK THE ATTRIBUTES
      for (int i = 0; i < atts.getLength(); i++) {
        if (FlowLexer.FlowAttTypes.isInEnum(atts.getQName(i).toUpperCase())) {
          token = tokenBuilder
              .setType(FlowLexer.FlowAttTypes.getEnum(atts.getQName(i).toUpperCase()))
              .setValueAndOriginalValue(atts.getValue(i).toUpperCase(), atts.getValue(i))
              .setURI(lex.getURI()).setLine(line).setColumn(0).build();
          lex.addToken(token);
          logger.debug(
              "TOKEN " + token.getValue() + "[" + token.getLine() + "," + token.getColumn() + "]");
        } else {
          token = tokenBuilder.setType(GenericTokenType.IDENTIFIER)
              .setValueAndOriginalValue(atts.getQName(i)).setURI(lex.getURI()).setLine(line)
              .setColumn(0).build();
          lex.addToken(token);
          logger.debug("IDENTIFIER " + token.getValue() + "[" + token.getLine() + ","
              + token.getColumn() + "]");
          token = tokenBuilder.setType(GenericTokenType.LITERAL)
              .setValueAndOriginalValue(atts.getValue(i)).setURI(lex.getURI()).setLine(line)
              .setColumn(0).build();
          lex.addToken(token);
          logger.debug("LITERAL " + token.getValue() + "[" + token.getLine() + ","
              + token.getColumn() + "]");
        }
      }
    }
  }
  
  @Override
  public void endElement(String uri, String name, String qualifiedName) {
    int line = locator.getLineNumber();
    int column = locator.getColumnNumber();
    if (chars.length() > 0) {
      String value = chars.toString();
      if (ignoreWs && !value.trim().replace("\n", "").replace("\r", "").equals("")) {
        logger.debug("element value" + "[" + line + "," + column + "]");
        Token token = tokenBuilder.setType(FlowLexer.FlowTypes.ELEMENT_VALUE)
            .setValueAndOriginalValue(value).setURI(lex.getURI()).setLine(charsLine)
            .setColumn(charsColumn).build();
        lex.addToken(token);
      }
      chars = new StringBuilder();
    }
    if (FlowLexer.FlowTypes.isInEnum("STOP_" + name.toUpperCase())) {
      logger.debug("Stop element: " + qualifiedName + "[" + line + "," + column + "]");
      Token token = tokenBuilder.setType(FlowLexer.FlowTypes.valueOf("STOP_" + name.toUpperCase()))
          .setValueAndOriginalValue(name.toUpperCase(), name).setURI(lex.getURI()).setLine(line)
          .setColumn(column).build();
      lex.addToken(token);
    }
  }
  
  @Override
  public void characters(char[] ch, int start, int length) {
    charsLine = locator.getLineNumber();
    charsColumn = locator.getColumnNumber();
    chars.append(ch, start, length);

  }

}
