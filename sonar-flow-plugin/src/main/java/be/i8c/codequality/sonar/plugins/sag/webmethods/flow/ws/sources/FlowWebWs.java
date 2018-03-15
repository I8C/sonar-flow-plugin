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

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.ws.sources;

import org.sonar.api.server.ws.WebService;

/**
 * Webservice that returns flow as graphical HTML.
 * 
 * @author DEWANST
 *
 */
public class FlowWebWs implements WebService {

  private FlowActionHandler flowActionHandler;

  @Override
  public void define(Context context) {
    NewController controller = context.createController("api/flowsources").setSince("1.0")
        .setDescription("Get details on flow files.");

    flowActionHandler = new FlowActionHandler(context);

    WebService.NewAction action = controller.createAction("flow")
        .setDescription(
            "Get source code as flow html. Require 'See Source Code' permission on file")
        .setSince("1.0").setHandler(flowActionHandler);

    action.createParam("key").setRequired(true).setDescription("File key")
      .setExampleValue("my_project:src/foo/node.xml");
    action.createParam("type").setRequired(true)
      .setDescription("output type, could be json or html")
      .setPossibleValues("json","html");
    action.createParam("full").setPossibleValues("true","false")
      .setRequired(false).setDescription("Output complete html page or only body contents");
    action.createParam("branch").setInternal(true);

    controller.done();
  }
}
