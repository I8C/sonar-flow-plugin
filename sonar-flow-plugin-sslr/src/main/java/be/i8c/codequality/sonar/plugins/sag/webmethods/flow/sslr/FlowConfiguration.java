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

import java.nio.charset.Charset;

import org.sonar.squidbridge.api.SquidConfiguration;

public class FlowConfiguration extends SquidConfiguration {

	private boolean ignoreNodeNdf;

	public FlowConfiguration(Charset encoding) {
		super(encoding);
	}

	public boolean getIgnoreNodeNdf() {
		return ignoreNodeNdf;
	}

	public void setIgnoreNodeNdf(boolean ignoreNodeNdf) {
		this.ignoreNodeNdf = ignoreNodeNdf;
	}

}
