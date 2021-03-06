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
package flashscope;

import juzu.FlashScoped;
import juzu.impl.common.Tools;

import javax.inject.Named;
import java.io.Serializable;
import java.util.UUID;

/** @author Julien Viet */
@FlashScoped
@Named("FOO")
public class Flash implements Serializable {

  /** . */
  public String value;

  @Override
  public String toString() {
    return "Flash[" + value + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else if (obj instanceof Flash) {
      Flash that = (Flash)obj;
      return Tools.safeEquals(value, that.value);
    } else {
      return false;
    }
  }
}
