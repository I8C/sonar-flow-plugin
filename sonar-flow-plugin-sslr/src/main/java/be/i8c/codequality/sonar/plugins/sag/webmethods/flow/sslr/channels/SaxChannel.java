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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.channels;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.channels.sax.FlowContentHandler;

import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Lexer;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sonar.sslr.channel.Channel;
import org.sonar.sslr.channel.CodeReader;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Channel that implemnts the SAX Parser.
 * It parses the file in one go.
 * @author DEWANST
 *
 */
public class SaxChannel extends Channel<Lexer> {

  private final Token.Builder tokenBuilder = Token.builder();
  public static final char BOM_CHAR = '\ufeff';

  @Override
  public boolean consume(CodeReader code, Lexer lex) {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setNamespaceAware(true);
    SAXParser saxParser;
    try {
      if (code.peek(1).equals(BOM_CHAR)) {
        // consume the BOM
        code.pop();
      }
      saxParser = spf.newSAXParser();
      XMLReader xmlReader = saxParser.getXMLReader();
      xmlReader.setContentHandler(new FlowContentHandler(lex, tokenBuilder));
      xmlReader.parse(new InputSource(
          new ByteArrayInputStream(new StringBuilder(code).toString().getBytes("UTF-8"))));
      int length = code.length();
      for (int j = 0; j < length; j++) {
        code.pop();
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Error(e);
      // return false;
    }
  }

}
