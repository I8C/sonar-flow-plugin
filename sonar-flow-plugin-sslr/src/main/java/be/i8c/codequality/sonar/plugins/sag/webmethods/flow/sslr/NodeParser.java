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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;

import java.io.File;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;

/**
 * Parser for node files.
 * @author DEWANST
 *
 */
public class NodeParser {
  private static final Parser<Grammar> P = NodeParser.create();
  private static FlowConfiguration conf;

  private NodeParser() {
  }

  public static Parser<Grammar> create(FlowConfiguration conf) {
    NodeParser.conf = conf;
    return Parser.builder(NodeGrammar.create()).withLexer(FlowLexer.create(conf)).build();
  }
  
  /**
   * Creates this parser.
   * @return
   */
  public static Parser<Grammar> create() {
    NodeParser.conf = new FlowConfiguration(Charset.defaultCharset());
    return Parser.builder(NodeGrammar.create()).withLexer(FlowLexer.create(NodeParser.conf))
        .build();
  }
  
  /**
   * Parses a file and returns the AstNode tree.
   * @param filePath
   * path to the file.
   * @return
   */
  public static AstNode parseFile(String filePath) {
    AstNode astNode;
    File file = FileUtils.getFile(filePath);
    if (file == null || !file.exists()) {
      throw new AssertionError("The file \"" + filePath + "\" does not exist.");
    }
    astNode = P.parse(file);
    return astNode;
  }

  public static AstNode parseString(String source) {
    return P.parse(source);
  }

}
