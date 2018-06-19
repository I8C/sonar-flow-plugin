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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.channels.SaxChannel;

import java.nio.charset.Charset;

import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;

/**
 * This lexer defines the tags of the flow xml file.
 * @author DEWANST
 *
 */
public class FlowLexer {

  private FlowLexer() {
  }
  
  /**
   * Creates this lexer.
   * @param conf
   * Configuration for this lexer.
   * @return
   */
  public static Lexer create() {
    return Lexer.builder()
        .withFailIfNoChannelToConsumeOneCharacter(true).withChannel(new SaxChannel())
        .withChannel(new BlackHoleChannel(".*")).build();
  }

  public static Lexer create(Charset charset) {
    return Lexer.builder()
        .withFailIfNoChannelToConsumeOneCharacter(true).withChannel(new SaxChannel())
        .withChannel(new BlackHoleChannel(".*"))
        .withCharset(charset).build();
  }

}
