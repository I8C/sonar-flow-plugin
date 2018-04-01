package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.ws.sources;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.internal.apachecommons.io.IOUtils;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Request.StringParam;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.Response.Stream;
import org.sonar.api.server.ws.WebService.Action;
import org.sonar.api.server.ws.WebService.Context;
import org.sonar.api.server.ws.WebService.Controller;

public class FlowActionHandlerTest {

  static final Logger logger = LoggerFactory.getLogger(FlowActionHandlerTest.class);

  @Test
  public void handleTest() throws Exception {
    Context wsContext = mock(Context.class);
    Controller wsController = mock(Controller.class);
    Action rawAction = mock(Action.class);
    RequestHandler rawActionHandler = mock(RequestHandler.class);
    Mockito.when(wsContext.controller("api/sources")).thenReturn(wsController);
    Mockito.when(wsController.action("raw")).thenReturn(rawAction);
    Mockito.when(rawAction.handler()).thenReturn(rawActionHandler);

    Mockito.doAnswer((Answer<Void>) invocation -> {
      Response response = invocation.getArgument(1);
      OutputStream os = response.stream().output();
      InputStream is = this.getClass().getResourceAsStream(
          "/WmTestPackage/ns/I8cFlowSonarPluginTest/pub/checkAllDebugFlowsInvalid/flow.xml");
      IOUtils.copy(is, os);
      return null;
    }).when(rawActionHandler).handle(Mockito.any(Request.class), Mockito.any(Response.class));
    Request request = mock(Request.class);
    StringParam full = mock(StringParam.class);
    Mockito.when(full.getValue()).thenReturn("true");
    Mockito.when(request.getParam("full")).thenReturn(full);
    Mockito.when(request.mandatoryParam("key"))
      .thenReturn("WmTestPackage/ns/I8cFlowSonarPluginTest/pub/checkAllDebugFlowsInvalid/flow.xml");
    Mockito.when(request.mandatoryParam("type"))
      .thenReturn("html");
    Response response = mock(Response.class);
    Stream stream = mock(Stream.class);
    OutputStream os = new ByteArrayOutputStream();
    Mockito.when(response.stream()).thenReturn(stream);
    Mockito.when(stream.output()).thenReturn(os);
    FlowActionHandler fah = new FlowActionHandler(wsContext);
    fah.handle(request, response);
    //System.out.println(os);
  }
}
