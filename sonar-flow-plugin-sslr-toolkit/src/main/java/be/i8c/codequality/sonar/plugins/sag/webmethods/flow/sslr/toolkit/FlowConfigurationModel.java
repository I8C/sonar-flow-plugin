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
package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.toolkit;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.internal.google.common.annotations.VisibleForTesting;
import org.sonar.api.internal.google.common.collect.ImmutableList;
import org.sonar.colorizer.Tokenizer;
import org.sonar.sslr.toolkit.ConfigurationModel;
import org.sonar.sslr.toolkit.ConfigurationProperty;
import org.sonar.sslr.toolkit.Validators;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowConfiguration;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowParser;

public class FlowConfigurationModel implements ConfigurationModel {

	private static final Logger LOG = LoggerFactory.getLogger(FlowConfigurationModel.class);

	  private static final String CHARSET_PROPERTY_KEY = "sonar.sourceEncoding";

	  @VisibleForTesting
	  ConfigurationProperty charsetProperty = new ConfigurationProperty("Charset", CHARSET_PROPERTY_KEY,
	    getPropertyOrDefaultValue(CHARSET_PROPERTY_KEY, "UTF-8"),
	    Validators.charsetValidator());

	  @Override
	  public Charset getCharset() {
	    return Charset.forName(charsetProperty.getValue());
	  }

	  @Override
	  public List<ConfigurationProperty> getProperties() {
	    return ImmutableList.of(charsetProperty);
	  }

	  @Override
	  public Parser<Grammar> getParser() {
	    return FlowParser.create(getConfiguration());
	  }

	  @Override
	  public List<Tokenizer> getTokenizers() {
	    return ImmutableList.of(
	    		// TODO Add tokenizers
	    );
	  }

	  @VisibleForTesting
	  FlowConfiguration getConfiguration() {
	    return new FlowConfiguration(Charset.forName(charsetProperty.getValue()));
	  }

	  @VisibleForTesting
	  static String getPropertyOrDefaultValue(String propertyKey, String defaultValue) {
	    String propertyValue = System.getProperty(propertyKey);

	    if (propertyValue == null) {
	      LOG.info("The property \"" + propertyKey + "\" is not set, using the default value \"" + defaultValue + "\".");
	      return defaultValue;
	    } else {
	      LOG.info("The property \"" + propertyKey + "\" is set, using its value \"" + propertyValue + "\".");
	      return propertyValue;
	    }
	  }

	@Override
	public void setUpdatedFlag() {
		// TODO Auto-generated method stub
		
	}
}
