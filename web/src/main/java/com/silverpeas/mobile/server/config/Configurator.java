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

package com.silverpeas.mobile.server.config;

import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Permet de configurer la connexion avec les éléments présents dans le fichier website.properties
 * @author svuillet
 */
public class Configurator {

  /**
   * The controller configuration
   */
  private static Properties conf = null;

  private static final String APP_CONFIG_FILENAME = "website.properties";

  private static final String BUNDLE_FILE_NAME = "resources";
  private static Hashtable<Locale, ResourceBundle> bundles =
      new Hashtable<Locale, ResourceBundle>();

  static {
    URL configFile = Configurator.class.getClassLoader().getResource(APP_CONFIG_FILENAME);
    if (configFile == null) {
      throw new RuntimeException("Impossible de trouver le fichier de configuration principal");
    } else {
      try {
        conf = new AutoRefreshingProperties(null, configFile.getPath());
      } catch (IOException e) {
        throw new RuntimeException("Impossible de charger le fichier de configuration principal");
      }
    }
  }

  public static String getConfigValue(String key) {
    return conf.getProperty(key);
  }

  public static int getConfigIntValue(String key) {
    return Integer.valueOf(conf.getProperty(key)).intValue();
  }

  public static String getBundle(String language, String key) {
    Locale locale = new Locale(language);
    if (!bundles.containsKey(locale)) {
      bundles.put(locale, ResourceBundle.getBundle(BUNDLE_FILE_NAME, locale));
    }
    return bundles.get(locale).getString(key);
  }
}
