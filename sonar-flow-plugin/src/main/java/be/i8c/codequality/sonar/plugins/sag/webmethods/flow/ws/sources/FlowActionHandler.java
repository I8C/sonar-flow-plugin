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

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.ws.sources.xslt.LocationFilter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService.Action;
import org.sonar.api.server.ws.WebService.Context;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class FlowActionHandler implements RequestHandler {

  private static Action rawAction;
  private static final TransformerFactory factory = TransformerFactory.newInstance();
  private static final Source xslt_html = new StreamSource(
      FlowActionHandler.class.getResourceAsStream("/xslt/convertFlowToHtml.xslt"));
  private static final Source xslt_json = new StreamSource(
      FlowActionHandler.class.getResourceAsStream("/xslt/convertFlowToJson.xslt"));
  private static final Source xslt_json_full = new StreamSource(
      FlowActionHandler.class.getResourceAsStream("/xslt/convertFlowToJsonFull.xslt"));
  private static final Transformer htmlTransformer = getTransFormer("html",true);
  private static final Transformer jsonFullTransformer = getTransFormer("json",true);
  private static final Transformer jsonTransformer = getTransFormer("json",false);

  public FlowActionHandler(Context context) {
    rawAction = (Action) context.controller("api/sources").action("raw");
  }

  @Override
  public void handle(Request request, Response response) throws Exception {
    String key = request.mandatoryParam("key");
    String type = request.mandatoryParam("type");
    boolean full = Boolean.valueOf(request.hasParam("full")
        ? request.getParam("full").getValue() : "false");
    PipeResponse pr = new PipeResponse();
    rawAction.handler().handle(request, pr);
    convertRawToFlow(((ByteArrayOutputStream) pr.stream().output()).toByteArray(),
        response.stream().output(), type, full, key);
    response.stream().setMediaType("application/json");
    response.setHeader("Content-Type", "application/json");
  }

  private void convertRawToFlow(byte[] in, OutputStream out, String type, 
      boolean full, String flowname)
      throws Exception {
    XMLReader xmlReader = XMLReaderFactory.createXMLReader();
    LocationFilter locationFilter = new LocationFilter(xmlReader);
    Transformer transformer = type.equals("html") 
        ? htmlTransformer : (full ? jsonFullTransformer : jsonTransformer);
    transformer.setParameter("full", String.valueOf(full));
    transformer.setParameter("flowname", flowname);
    SAXSource saxSource = new SAXSource(
        locationFilter, new InputSource(new ByteArrayInputStream(in)));
    transformer.transform(saxSource, new StreamResult(out));

  }

  private static Transformer getTransFormer(String type, boolean full) {
    Transformer t;
    try {
      if (type.equals("html")) {
        t = factory.newTransformer(xslt_html);
      } else if (full) {
        t = factory.newTransformer(xslt_json_full);
      } else {
        t = factory.newTransformer(xslt_json);
      }
      return t;
    } catch (TransformerConfigurationException e) {
      return null;
    }
  }
}