/**
 * Copyright (C) 2000 - 2011 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.rebind;

import java.io.PrintWriter;
import java.util.Calendar;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;
import com.silverpeas.mobile.server.config.Configurator;

/**
 * Properties generator.
 * @author svuillet
 */
public class PropertiesGenerator extends Generator {

  /**
   * Generate a properties provider.
   * @param logger
   * @param context
   * @param typeName class name to substitute
   * @return created class name
   * @throws UnableToCompleteException
   */
  @SuppressWarnings("deprecation")
  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    ClassSourceFileComposerFactory composer = null;
    SourceWriter out = null;
    try {
      // Retreive properties list
      // ArrayList<Plugin> pluginsAvailables = new ArrayList<Plugin>();

      // Retreive all properties values

      // Generate the source file
      TypeOracle oracle = context.getTypeOracle();
      JClassType type = oracle.getType(typeName);
      PropertyOracle propertyOracle = context.getPropertyOracle();
      String packageName = type.getPackage().getName();
      String simpleName =
          type.getSimpleSourceName() + "_Generated_" +
              propertyOracle.getPropertyValue(logger, "user.agent") + "_" +
              Calendar.getInstance().getTimeInMillis();
      composer = new ClassSourceFileComposerFactory(packageName, simpleName);
      // Define imports
      composer.addImport("java.util.*");
      // Define implements
      composer.addImplementedInterface(ConfigurationProvider.class.getCanonicalName());
      // Write "getDESKey" method
      PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
      out = composer.createSourceWriter(context, printWriter);
      out.println("public String getDESKey() {");
      out.indent();
      out.println("return \"" + Configurator.getConfigValue("des.key") + "\";");
      out.outdent();
      out.println("}");
      out.outdent();
      out.commit(logger);
    } catch (Throwable t) {
      t.printStackTrace();
      throw new UnableToCompleteException();
    }

    return composer.getCreatedClassName();
  }
}
