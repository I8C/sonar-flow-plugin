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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.CheckForNull;

import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;
import org.sonar.api.utils.text.XmlWriter;

public class PipeResponse implements Response {

  private ByteStream bs;
  private Map<String, String> headers = new HashMap<String, String>();

  public PipeResponse() {
    bs = new ByteStream();
  }

  @Override
  public JsonWriter newJsonWriter() {
    return null;
  }

  @Override
  public XmlWriter newXmlWriter() {
    return null;
  }

  @Override
  public Response noContent() {
    stream().setStatus(HttpURLConnection.HTTP_NO_CONTENT);
    try {
      bs.output().close();
    } catch (IOException e) {
      // close quietly
    }
    ;
    return this;
  }

  @Override
  public Response setHeader(String name, String value) {
    headers.put(name, value);
    return this;
  }

  @Override
  public Collection<String> getHeaderNames() {
    return headers.keySet();
  }

  @Override
  public String getHeader(String name) {
    return headers.get(name);
  }

  @Override
  public ByteStream stream() {
    return bs;
  }

  public class ByteStream implements Stream {

    private ByteArrayOutputStream bas;
    private String mediaType;
    private int status;

    public ByteStream() {
      bas = new ByteArrayOutputStream();
    }

    @CheckForNull
    public String mediaType() {
      return mediaType;
    }

    @Override
    public Stream setMediaType(String s) {
      this.mediaType = s;
      return this;
    }

    public int status() {
      return status;
    }

    @Override
    public Stream setStatus(int i) {
      this.status = i;
      return this;
    }

    @Override
    public OutputStream output() {
      return bas;
    }

  }

}
