/*
 * Copyright 2013 eXo Platform SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package juzu.impl.bridge.spi.portlet;

import juzu.request.ClientContext;

import javax.portlet.ClientDataRequest;
import java.io.IOException;
import java.io.InputStream;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class PortletClientContext implements ClientContext {

  /** . */
  private final ClientDataRequest request;

  PortletClientContext(ClientDataRequest request) {
    this.request = request;
  }

  public String getContentType() {
    return request.getContentType();
  }

  public String getCharacterEncoding() {
    return request.getCharacterEncoding();
  }

  public int getContentLenth() {
    return request.getContentLength();
  }

  public InputStream getInputStream() throws IOException {
    return request.getPortletInputStream();
  }
}
