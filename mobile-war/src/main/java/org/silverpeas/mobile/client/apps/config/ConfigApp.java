/*
 * Copyright (C) 2000 - 2022 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.apps.config;

import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.config.events.app.AbstractConfigAppEvent;
import org.silverpeas.mobile.client.apps.config.events.app.ConfigAppEventHandler;
import org.silverpeas.mobile.client.apps.config.events.app.LoadConfigEvent;
import org.silverpeas.mobile.client.apps.config.events.app.UpdateConfigEvent;
import org.silverpeas.mobile.client.apps.config.events.pages.ConfigLoadedEvent;
import org.silverpeas.mobile.client.apps.config.pages.ConfigPage;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.shared.dto.configuration.Config;

public class ConfigApp extends App implements ConfigAppEventHandler {

  private static ConfigApp instance = null;

  public ConfigApp() {
    super();
    EventBus.getInstance().addHandler(AbstractConfigAppEvent.TYPE, this);
  }

  public void start() {
    setMainPage(new ConfigPage());
    super.start();
  }


  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractConfigAppEvent.TYPE, this);
    super.stop();
  }


  public static ConfigApp getInstance() {
    if (instance == null) {
      instance = new ConfigApp();
    }
    return instance;
  }

  @Override
  public void updateConfig(UpdateConfigEvent event) {
    Config conf = event.getConfig();
    LocalStorageHelper.store("config", Config.class, conf);
  }

  @Override
  public void loadConfig(LoadConfigEvent event) {
    Config conf = SpMobil.getConfiguration();
    EventBus.getInstance().fireEvent(new ConfigLoadedEvent(conf));
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {

  }
}
