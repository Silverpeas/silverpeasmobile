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

package com.silverpeas.mobile.client.apps.config.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.silverpeas.mobile.client.apps.config.events.app.LoadConfigEvent;
import com.silverpeas.mobile.client.apps.config.events.app.UpdateConfigEvent;
import com.silverpeas.mobile.client.apps.config.events.pages.AbstractConfigPagesEvent;
import com.silverpeas.mobile.client.apps.config.events.pages.ConfigLoadedEvent;
import com.silverpeas.mobile.client.apps.config.events.pages.ConfigPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.configuration.Config;

/**
 * @author: svu
 */
public class ConfigPage extends PageContent implements ConfigPagesEventHandler {


  private static ConfigPageUiBinder uiBinder = GWT.create(ConfigPageUiBinder.class);
  @UiField
  HTMLPanel container;
  @UiField
  CheckBox newsDisplay;
  @UiField
  CheckBox lastPublicationsDisplay;
  @UiField
  CheckBox favoritesDisplay;
  private Config config;
  public ConfigPage() {
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractConfigPagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new LoadConfigEvent());
  }

  @Override
  public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
    super.onPreviewNativeEvent(event);
    if (event.getTypeInt() == Event.ONCLICK || event.getTypeInt() == Event.ONKEYUP) {
      save();
    }
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractConfigPagesEvent.TYPE, this);

  }

  @Override
  public void onConfigLoaded(ConfigLoadedEvent event) {
    config = event.getConfig();
    newsDisplay.setValue(config.isNewsDisplay());
    lastPublicationsDisplay.setValue(config.isLastPublicationsDisplay());
    favoritesDisplay.setValue(config.isFavoritesDisplay());
  }

  private void save() {
    config.setNewsDisplay(newsDisplay.getValue());
    config.setFavoritesDisplay(favoritesDisplay.getValue());
    config.setLastPublicationsDisplay(lastPublicationsDisplay.getValue());
    EventBus.getInstance().fireEvent(new UpdateConfigEvent(config));
  }

  interface ConfigPageUiBinder extends UiBinder<HTMLPanel, ConfigPage> {}
}