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

import java.util.Arrays;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.channels.SaxChannel;

public class FlowLexer {
	 
	private FlowLexer() {
	  }

	  public static enum FlowTypes implements TokenType {

	    START_FLOW, STOP_FLOW,
	    START_SEQUENCE, STOP_SEQUENCE,
	    START_INVOKE, STOP_INVOKE,
	    START_LOOP, STOP_LOOP,
	    START_BRANCH, STOP_BRANCH,
	    START_EXIT, STOP_EXIT,
	    START_RETRY, STOP_RETRY,
	    
	    START_MAP, STOP_MAP,
	    START_MAPTARGET, STOP_MAPTARGET,
	    START_MAPSOURCE, STOP_MAPSOURCE,
	    START_MAPDELETE, STOP_MAPDELETE,
	    START_MAPSET, STOP_MAPSET,
	    START_MAPCOPY, STOP_MAPCOPY,
	    START_MAPINVOKE, STOP_MAPINVOKE,
	    
	    START_DATA, STOP_DATA,
	    START_VALUES, STOP_VALUES,
	    START_RECORD, STOP_RECORD,
	    START_VALUE, STOP_VALUE,
	    START_NUMBER, STOP_NUMBER,
	    START_ARRAY, STOP_ARRAY,
	    START_BOOLEAN, STOP_BOOLEAN,
	    
	    ELEMENT_VALUE,
	    
	    START_COMMENT, STOP_COMMENT;
	    
	    

	    public static boolean isInEnum(String value) {
	         return Arrays.stream(FlowTypes.values()).anyMatch(e -> e.name().equals(value));
	    }
	    
	    private FlowTypes() {

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
	      return true;
	    }

	  }

	public static enum FlowAttTypes implements TokenType {	    
		SERVICE,EXITON("EXIT-ON"),MODE,NAME,DISABLED,FROM,SIGNAL,FAILUREMESSAGE("FAILURE-MESSAGE"),SWITCH,
		LABELEXPRESSIONS;

		private String attName;
		  
		    public static boolean isInEnum(String value) {
		         return Arrays.stream(FlowAttTypes.values()).anyMatch(e -> e.getAttName().equalsIgnoreCase(value));
		    }
		    
		    public static FlowAttTypes getEnum(String value) {
		        return Arrays.stream(FlowAttTypes.values()).filter(e -> e.getAttName().equalsIgnoreCase(value)).findFirst().get();
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

	  public static Lexer create(FlowConfiguration conf) {
	    return Lexer.builder()
	    	.withCharset(conf.getCharset())
	        .withFailIfNoChannelToConsumeOneCharacter(true)
	        .withChannel(new SaxChannel())
	        .withChannel(new BlackHoleChannel(".*"))
	        .build();
	  }

}
