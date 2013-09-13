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
package juzu.impl.compiler;

import juzu.impl.plugin.template.metamodel.TemplateMetaModel;
import juzu.test.AbstractTestCase;
import org.junit.Test;

import java.util.regex.Pattern;

/** @author Julien Viet */
public class MessageTestCase extends AbstractTestCase {

  @Test
  public void testFoo() {


    Pattern a = null;
    Message msg = new Message(TemplateMetaModel.CANNOT_WRITE_APPLICATION, "a\nb");
    String s = msg.format();
    msg = Message.parse(s);
    assertNotNull(msg);

  }

}