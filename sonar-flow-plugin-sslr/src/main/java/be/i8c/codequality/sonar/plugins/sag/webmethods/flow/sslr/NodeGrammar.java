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

import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static com.sonar.sslr.api.GenericTokenType.LITERAL;

import com.sonar.sslr.api.Grammar;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowAttIdentifierTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowAttTypes;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.types.FlowTypes;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

public enum NodeGrammar implements GrammarRuleKey {

  DATA, VALUES, SIGNATURE, RECORD, VALUE, ARRAY, NUMBER, BOOLEAN,

  UNDEF_ATT, ATTRIBUTES, SIGNATURE_IN, SIGNATURE_OUT, REC_FIELDS;
  
  /**
   * Enum of the different node grammar components.
   * It is build up of FlowTypes.
   * 
   * @author DEWANST
   *
   */
  public static Grammar create() {

    LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();

    b.rule(DATA).is(
        FlowTypes.START_DATA, 
        ATTRIBUTES, 
        b.optional(VALUES), 
        FlowTypes.STOP_DATA);
    b.rule(VALUES).is(
        FlowTypes.START_VALUES, 
        ATTRIBUTES,
        b.zeroOrMore(b.firstOf(VALUE, ARRAY, SIGNATURE, RECORD, NUMBER, BOOLEAN)), 
        FlowTypes.STOP_VALUES);
    b.rule(RECORD).is(
        FlowTypes.START_RECORD, 
        ATTRIBUTES,
        b.zeroOrMore(b.firstOf(VALUE, ARRAY, RECORD, REC_FIELDS, NUMBER, BOOLEAN)), 
        FlowTypes.STOP_RECORD);
    b.rule(SIGNATURE).is(
        FlowTypes.START_RECORD, 
        FlowAttIdentifierTypes.SIGNATURE,ATTRIBUTES,
        b.zeroOrMore(b.firstOf(SIGNATURE_IN, SIGNATURE_OUT)), 
        FlowTypes.STOP_RECORD);
    b.rule(SIGNATURE_IN).is(
        FlowTypes.START_RECORD, 
        FlowAttIdentifierTypes.SIGN_IN,ATTRIBUTES,
        b.zeroOrMore(b.firstOf(VALUE, REC_FIELDS)), 
        FlowTypes.STOP_RECORD);
    b.rule(SIGNATURE_OUT).is(
        FlowTypes.START_RECORD, 
        FlowAttIdentifierTypes.SIGN_OUT,ATTRIBUTES,
        b.zeroOrMore(b.firstOf(VALUE, REC_FIELDS)), 
        FlowTypes.STOP_RECORD);
    b.rule(REC_FIELDS).is(
        FlowTypes.START_ARRAY, 
        FlowAttIdentifierTypes.REC_FIELDS,ATTRIBUTES,
        b.zeroOrMore(b.firstOf(VALUE, ARRAY, RECORD)), 
        FlowTypes.STOP_ARRAY);
    b.rule(VALUE).is(
        FlowTypes.START_VALUE, 
        ATTRIBUTES,
        b.zeroOrMore(FlowTypes.ELEMENT_VALUE), 
        FlowTypes.STOP_VALUE);
    b.rule(ARRAY).is(
        FlowTypes.START_ARRAY,
        ATTRIBUTES,
        b.zeroOrMore(b.firstOf(VALUE, ARRAY, RECORD)),
        FlowTypes.STOP_ARRAY);
    b.rule(NUMBER).is(
        FlowTypes.START_NUMBER,
        ATTRIBUTES,
        FlowTypes.ELEMENT_VALUE,
        FlowTypes.STOP_NUMBER);
    b.rule(BOOLEAN).is(
        FlowTypes.START_BOOLEAN,
        ATTRIBUTES, 
        FlowTypes.ELEMENT_VALUE, 
        FlowTypes.STOP_BOOLEAN);

    b.rule(UNDEF_ATT).is(
        b.sequence(IDENTIFIER, LITERAL));
    b.rule(ATTRIBUTES).is(
        b.zeroOrMore(b.firstOf(FlowAttTypes.SERVICE, FlowAttTypes.EXITON, 
            FlowAttTypes.MODE, FlowAttTypes.NAME, UNDEF_ATT)));
    b.setRootRule(VALUES);

    return b.build();
  }
}
