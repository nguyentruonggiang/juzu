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

package org.juzu.impl.application;

import org.juzu.impl.spi.template.TemplateStub;
import org.juzu.template.TemplateRenderContext;
import org.juzu.text.Printer;

import java.util.Locale;
import java.util.Map;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class ApplicationTemplateRenderContext extends TemplateRenderContext
{

   /** . */
   private final InternalApplicationContext applicationContext;

   public ApplicationTemplateRenderContext(InternalApplicationContext applicationContext, Printer printer, Map<String, ?> attributes, Locale locale)
   {
      super(printer, attributes, locale);

      //
      this.applicationContext = applicationContext;
   }

   @Override
   public TemplateStub resolveTemplate(String path)
   {
      return applicationContext.resolveTemplateStub(path);
   }

   @Override
   public Object resolveBean(String name)
   {
      return applicationContext.resolveBean(name);
   }
}
