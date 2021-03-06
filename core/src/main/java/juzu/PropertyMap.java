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

package juzu;

import juzu.impl.common.Tools;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A map that holds properties as expressed by {@link PropertyType}. The map can delegate to an optional
 * another properties (thus forming a chain) in order to provide a modified version of an existing map
 * by wrapping it with a map.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class PropertyMap implements Iterable<PropertyType<?>> {

  /** . */
  private static final Object[] NO_VALUES = new Object[0];

  private static class Values implements Iterable<Object> {

    /** . */
    private Object[] objects = NO_VALUES;

    /** . */
    private int size = 0;

    /** . */
    private final PropertyMap owner;

    private Values(PropertyMap owner) {
      this.owner = owner;
    }

    private Values(PropertyMap owner, Values that) {
      this.owner = owner;
      this.objects = that.objects.clone();
      this.size = that.size;
    }

    public Iterator<Object> iterator() {
      return Tools.iterator(0, size, objects);
    }

    void addValue(Object value) {
      if (value == null) {
        throw new NullPointerException("Value can't be null");
      }
      if (size >= objects.length) {
        Object[] copy = new Object[size + 4];
        System.arraycopy(objects, 0, copy, 0, objects.length);
        objects = copy;
      }
      objects[size++] = value;
    }

    void clear() {
      if (size > 0) {
        for (int i = size - 1;i >= 0;i--) {
          objects[i] = null;
        }
        size = 0;
      }
    }
  }

  /** The current existing values. */
  private HashMap<PropertyType<?>, Values> map;

  /** An optional delegate. */
  private PropertyMap delegate;

  public PropertyMap() {
  }

  public PropertyMap(PropertyMap delegate) {
    this.delegate = delegate;
  }

  private <T> Values get(PropertyType<T> property, boolean recurse, boolean modifiable) {
    Values values = null;

    //
    if (map != null) {
      values = map.get(property);
    }

    //
    if (values == null && recurse && delegate != null) {
      values = delegate.get(property, true, false);
    }

    //
    if (modifiable) {
      if (values == null) {
        if (map == null) {
          map = new HashMap<PropertyType<?>, Values>();
        }
        map.put(property, values = new Values(this));
      }
      else if (values.owner != this) {
        if (map == null) {
          map = new HashMap<PropertyType<?>, Values>();
        }
        map.put(property, values = new Values(this, values));
      }
    }
    else if (values != null && values.size == 0) {
      values = null;
    }

    //
    return values;
  }

  public Iterator<PropertyType<?>> iterator() {
    return map != null ? map.keySet().iterator() : Tools.<PropertyType<?>>emptyIterator();
  }

  public <T> T getValue(PropertyType<T> property) {
    T value = null;
    Values values = get(property, true, false);
    if (values != null && values.size > 0) {
      value = property.cast(values.objects[0]);
    }
    return value;
  }

  public <T> Iterable<T> getValues(PropertyType<T> property) throws NullPointerException {
    return (Iterable<T>)get(property, true, false);
  }

  public <T> void setValue(PropertyType<T> property, T value) throws NullPointerException {
    if (value == null) {
      remove(property);
    }
    else {
      Values existing = get(property, false, true);
      existing.clear();
      existing.addValue(value);
    }
  }

  public <T> void setValues(PropertyType<T> property, T... values) throws NullPointerException {
    Values existing = get(property, false, true);
    existing.clear();
    for (T value : values) {
      existing.addValue(value);
    }
  }

  public <T> void setValues(PropertyType<T> property, Iterable<? extends T> values) throws NullPointerException {
    Values existing = get(property, false, true);
    existing.clear();
    for (T value : values) {
      existing.addValue(value);
    }
  }

  public <T> void addValue(PropertyType<T> property, T value) throws NullPointerException {
    get(property, true, true).addValue(value);
  }

  public <T> void addValues(PropertyType<T> property, T... values) throws NullPointerException {
    Values existing = get(property, true, true);
    for (T value : values) {
      existing.addValue(value);
    }
  }

  public <T> void addValues(PropertyType<T> property, Iterable<? extends T> values) throws NullPointerException {
    Values existing = get(property, true, true);
    for (T value : values) {
      existing.addValue(value);
    }
  }

  public <T> void remove(PropertyType<T> property) throws NullPointerException {
    Values values = get(property, false, delegate != null);
    if (values != null) {
      values.clear();
    }
  }

  public <T> boolean contains(PropertyType<T> property) throws NullPointerException {
    Values values = get(property, true, false);
    return values != null && values.size > 0;
  }
}
