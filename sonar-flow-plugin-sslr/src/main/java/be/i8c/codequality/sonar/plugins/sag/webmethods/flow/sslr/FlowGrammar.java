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

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static com.sonar.sslr.api.GenericTokenType.LITERAL;

import com.sonar.sslr.api.Grammar;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowAttIdentifierTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowAttTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowTypes;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;




/**
 * Enum of the different flow grammar components.
 * It is build up of FlowTypes.
 * 
 * @author DEWANST
 *
 */
public enum FlowGrammar implements GrammarRuleKey {

  FLOW, SEQUENCE, INVOKE, MAP, LOOP, BRANCH, EXIT, RETRY,

  CONTENT, COMMENT,

  MAPSOURCE, MAPTARGET, MAPDELETE, MAPPING, MAPSET, MAPCOPY, MAPINVOKE,

  DATA, VALUES, RECORD, VALUE, NUMBER, ARRAY,

  UNDEF_ATT, ATTRIBUTES, REC_FIELDS;

  /**
   * Creates the flow grammar.
   * @return
   */
  public static Grammar create() {

    LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();

    b.rule(FLOW).is(
        FlowTypes.START_FLOW, ATTRIBUTES, b.optional(COMMENT), CONTENT, FlowTypes.STOP_FLOW, EOF);
    b.rule(SEQUENCE).is(
        FlowTypes.START_SEQUENCE,
        ATTRIBUTES, b.optional(COMMENT), CONTENT,
        FlowTypes.STOP_SEQUENCE);
    b.rule(INVOKE).is(
        FlowTypes.START_INVOKE,
        ATTRIBUTES, b.optional(COMMENT), b.zeroOrMore(MAP),
        FlowTypes.STOP_INVOKE);
    b.rule(BRANCH).is(
        FlowTypes.START_BRANCH, ATTRIBUTES, b.optional(COMMENT), CONTENT, FlowTypes.STOP_BRANCH);
    b.rule(MAP).is(
        FlowTypes.START_MAP, ATTRIBUTES, b.optional(COMMENT), MAPPING, FlowTypes.STOP_MAP);
    b.rule(LOOP).is(
        FlowTypes.START_LOOP, ATTRIBUTES, b.optional(COMMENT), CONTENT, FlowTypes.STOP_LOOP);
    b.rule(EXIT).is(
        FlowTypes.START_EXIT, ATTRIBUTES, b.optional(COMMENT), FlowTypes.STOP_EXIT);
    b.rule(RETRY).is(
        FlowTypes.START_RETRY, ATTRIBUTES, b.optional(COMMENT), CONTENT, FlowTypes.STOP_RETRY);

    b.rule(COMMENT).is(
        FlowTypes.START_COMMENT, b.zeroOrMore(FlowTypes.ELEMENT_VALUE), FlowTypes.STOP_COMMENT);
    b.rule(CONTENT).is(
        b.zeroOrMore(b.firstOf(SEQUENCE, INVOKE, MAP, BRANCH, LOOP, EXIT, RETRY)));

    b.rule(MAPPING).is(
        b.optional(MAPTARGET), b.optional(MAPSOURCE),
        b.zeroOrMore(b.firstOf(MAPINVOKE, MAPDELETE, MAPSET, MAPCOPY)));
    b.rule(MAPTARGET).is(
        FlowTypes.START_MAPTARGET, VALUES, FlowTypes.STOP_MAPTARGET);
    b.rule(MAPSOURCE).is(
        FlowTypes.START_MAPSOURCE, VALUES, FlowTypes.STOP_MAPSOURCE);
    b.rule(MAPDELETE).is(
        FlowTypes.START_MAPDELETE, ATTRIBUTES, b.optional(COMMENT), FlowTypes.STOP_MAPDELETE);
    b.rule(MAPSET).is(
        FlowTypes.START_MAPSET, 
        ATTRIBUTES, b.optional(COMMENT), b.optional(DATA), 
        FlowTypes.STOP_MAPSET);
    b.rule(MAPCOPY).is(
        FlowTypes.START_MAPCOPY, ATTRIBUTES, b.optional(COMMENT), FlowTypes.STOP_MAPCOPY);
    b.rule(MAPINVOKE).is(
        FlowTypes.START_MAPINVOKE, 
        ATTRIBUTES, b.optional(COMMENT), b.oneOrMore(MAP), 
        FlowTypes.STOP_MAPINVOKE);
    b.rule(DATA).is(
        FlowTypes.START_DATA, ATTRIBUTES, b.optional(VALUES), FlowTypes.STOP_DATA);
    b.rule(VALUES).is(
        FlowTypes.START_VALUES, 
        ATTRIBUTES, b.zeroOrMore(b.firstOf(VALUE, ARRAY, RECORD, NUMBER)), 
        FlowTypes.STOP_VALUES);
    b.rule(RECORD).is(
        FlowTypes.START_RECORD,
        ATTRIBUTES, b.zeroOrMore(b.firstOf(VALUE, ARRAY, RECORD, NUMBER, REC_FIELDS)), 
        FlowTypes.STOP_RECORD);
    b.rule(REC_FIELDS).is(
        FlowTypes.START_ARRAY, 
        FlowAttIdentifierTypes.REC_FIELDS,ATTRIBUTES,
        b.zeroOrMore(b.firstOf(VALUE, ARRAY, RECORD)), 
        FlowTypes.STOP_ARRAY);
    b.rule(VALUE).is(
        FlowTypes.START_VALUE, 
        ATTRIBUTES, b.zeroOrMore(FlowTypes.ELEMENT_VALUE), 
        FlowTypes.STOP_VALUE);
    b.rule(NUMBER).is(
        FlowTypes.START_NUMBER, 
        ATTRIBUTES, b.zeroOrMore(FlowTypes.ELEMENT_VALUE), 
        FlowTypes.STOP_NUMBER);
    b.rule(ARRAY).is(
        FlowTypes.START_ARRAY, 
        ATTRIBUTES, b.zeroOrMore(b.firstOf(VALUE, ARRAY, RECORD)),
        FlowTypes.STOP_ARRAY);

    b.rule(UNDEF_ATT).is(
        b.sequence(IDENTIFIER, LITERAL));
    b.rule(ATTRIBUTES).is(
        b.zeroOrMore(b.firstOf(FlowAttTypes.SERVICE, FlowAttTypes.EXITON, FlowAttTypes.MODE, 
            FlowAttTypes.NAME, FlowAttTypes.DISABLED, FlowAttTypes.FROM,
            FlowAttTypes.FAILUREMESSAGE, FlowAttTypes.SIGNAL, FlowAttTypes.SWITCH, 
            FlowAttTypes.LABELEXPRESSIONS, UNDEF_ATT)));

    b.setRootRule(FLOW);

    return b.build();
  }
}
