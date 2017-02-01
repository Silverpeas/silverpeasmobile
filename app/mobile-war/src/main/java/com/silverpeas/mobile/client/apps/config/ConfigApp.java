/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.client.apps.config;

import com.silverpeas.mobile.client.apps.config.events.app.AbstractConfigAppEvent;
import com.silverpeas.mobile.client.apps.config.events.app.ConfigAppEventHandler;
import com.silverpeas.mobile.client.apps.config.events.app.ConfigChangedEvent;
import com.silverpeas.mobile.client.apps.config.events.app.LoadConfigEvent;
import com.silverpeas.mobile.client.apps.config.events.app.UpdateConfigEvent;
import com.silverpeas.mobile.client.apps.config.events.pages.ConfigLoadedEvent;
import com.silverpeas.mobile.client.apps.config.pages.ConfigPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.shared.dto.configuration.Config;

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
    EventBus.getInstance().fireEvent(new ConfigChangedEvent());
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
    Config c = LocalStorageHelper.load("config", Config.class);
    if (c == null) {
      c = Config.getDefaultConfig();
    }
    EventBus.getInstance().fireEvent(new ConfigLoadedEvent(c));
  }

  @Override
  public void configChanged(ConfigChangedEvent event) {

  }
}
