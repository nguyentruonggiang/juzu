/*
 * Copyright (C) 2011 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.juzu.test.protocol.mock;

import org.juzu.impl.application.ApplicationException;
import org.juzu.impl.spi.inject.InjectBuilder;
import org.juzu.impl.spi.request.RequestBridge;
import org.juzu.impl.utils.JSON;
import org.juzu.impl.utils.Tools;
import org.juzu.metadata.ApplicationDescriptor;
import org.juzu.impl.application.InternalApplicationContext;
import org.juzu.request.ApplicationContext;
import org.juzu.impl.application.ApplicationBootstrap;
import org.juzu.impl.spi.fs.ReadFileSystem;
import org.juzu.impl.spi.fs.disk.DiskFileSystem;
import org.juzu.test.AbstractTestCase;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class MockApplication<P>
{

   /** . */
   private final InjectBuilder bootstrap;

   /** . */
   private final ReadFileSystem<P> classes;

   /** . */
   final ClassLoader classLoader;

   /** . */
   private final Set<String> declarations;

   /** . */
   private final Map<Class<?>, Object> bindings;

   /** . */
   InternalApplicationContext context;

   /** . */
   private ApplicationBootstrap boot;

   /** . */
   private final ApplicationDescriptor descriptor;


   public MockApplication(ReadFileSystem<P> classes, ClassLoader classLoader, InjectBuilder bootstrap) throws Exception
   {
      P f = classes.getPath(Arrays.asList("org", "juzu", "config.json"));
      if (f == null)
      {
         throw new Exception("Cannot find config properties");
      }

      //
      URL url = classes.getURL(f);
      String s = Tools.read(url);
      JSON props = (JSON)JSON.parse(s);

      //
      if (props.names().size() != 1)
      {
         throw AbstractTestCase.failure("Could not find an application to start " + props);
      }
      String name = props.names().iterator().next();
      String fqn = props.getString(name);
      Class<?> clazz = classLoader.loadClass(fqn);
      Field field = clazz.getDeclaredField("DESCRIPTOR");
      ApplicationDescriptor descriptor = (ApplicationDescriptor)field.get(null);

      //
      this.classes = classes;
      this.classLoader = classLoader;
      this.bootstrap = bootstrap;
      this.declarations = new HashSet<String>();
      this.bindings = new HashMap<Class<?>, Object>();
      this.descriptor = descriptor;
   }

   public MockApplication<P> init() throws Exception
   {
      DiskFileSystem libs = new DiskFileSystem(new File(ApplicationBootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI()));

      //
      bootstrap.addFileSystem(classes);
      bootstrap.addFileSystem(libs);
      bootstrap.setClassLoader(classLoader);

      //
      for (String declaration : declarations)
      {
         Class<?> beanClazz = classLoader.loadClass(declaration);
         bootstrap.declareBean(beanClazz, null);
      }

      //
      for (Map.Entry<Class<?>, Object> binding : bindings.entrySet())
      {
         Class type = binding.getKey();
         Object bean = binding.getValue();
         bootstrap.bindBean(type, null, bean);
      }

      //
      ApplicationBootstrap boot = new ApplicationBootstrap(bootstrap, descriptor);

      //
      boot.start();

      //
      this.context = boot.getContext();
      this.boot = boot;

      //
      return this;
   }

   public ApplicationDescriptor getDescriptor()
   {
      return descriptor;
   }

   public void declareBean(String className)
   {
      declarations.add(className);
   }
   
   public <T> void bindBean(Class<T> type, T bean)
   {
      bindings.put(type, bean);
   }

   public ApplicationContext getContext()
   {
      return context;
   }

   void invoke(RequestBridge bridge) throws ApplicationException
   {
      this.context.invoke(bridge);
   }

   public MockClient client()
   {
      return new MockClient(this);
   }
}
