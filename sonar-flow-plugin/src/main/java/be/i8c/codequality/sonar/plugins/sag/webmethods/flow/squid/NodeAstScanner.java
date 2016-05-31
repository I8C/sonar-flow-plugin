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
package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.squid;

import java.io.File;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.CommentAnalyser;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric.FlowMetric;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowConfiguration;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.NodeParser;

public class NodeAstScanner {

	final static Logger logger = LoggerFactory.getLogger(NodeAstScanner.class);
	
	  private NodeAstScanner() {
	  }

	  /**
	   * Helper method for testing checks without having to deploy them on a Sonar instance.
	   */
	  public static SourceFile scanSingleFile(File file, SquidAstVisitor<Grammar>... visitors) {
	    if (!file.isFile()) {
	      throw new IllegalArgumentException("File '" + file + "' not found.");
	    }

	    AstScanner<Grammar> scanner = create(new FlowConfiguration(Charsets.UTF_8), visitors);
	    scanner.scanFile(file);
	    Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
	    if (sources.size() != 1) {
	      throw new IllegalStateException("Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
	    }
	    return (SourceFile) sources.iterator().next();
	  }

	  public static AstScanner<Grammar> create(FlowConfiguration conf, SquidAstVisitor<Grammar>... visitors) {
		    final SquidAstVisitorContextImpl<Grammar> context = new SquidAstVisitorContextImpl<Grammar>(new SourceProject("Flow Project"));
		    final Parser<Grammar> parser = NodeParser.create(conf);

		    AstScanner.Builder<Grammar> builder = AstScanner.<Grammar>builder(context).setBaseParser(parser);
		    
		    /* Required */
		    builder.setFilesMetric(FlowMetric.FILES);
		    setCommentAnalyser(builder);

		    /* External visitors (typically Check ones) */
		    for (SquidAstVisitor<Grammar> visitor : visitors) {
		      builder.withSquidAstVisitor(visitor);
		    }

		    return builder.build();
	  }
	  
	  private static void setCommentAnalyser(AstScanner.Builder<Grammar> builder) {
		    builder.setCommentAnalyser(
		      new CommentAnalyser() {
		        @Override
		        public boolean isBlank(String line) {
		          return true;
		        }

		        @Override
		        public String getContents(String comment) {
		          return comment;
		        }
		      });
		  }

}
