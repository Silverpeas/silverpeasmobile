/*
 * Copyright (C) 2000 - 2022 Silverpeas
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.apps.config.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.config.events.app.LoadConfigEvent;
import org.silverpeas.mobile.client.apps.config.events.app.UpdateConfigEvent;
import org.silverpeas.mobile.client.apps.config.events.pages.AbstractConfigPagesEvent;
import org.silverpeas.mobile.client.apps.config.events.pages.ConfigLoadedEvent;
import org.silverpeas.mobile.client.apps.config.events.pages.ConfigPagesEventHandler;
import org.silverpeas.mobile.client.apps.config.resources.ConfigMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.userselection.widgets.events.ChangeEvent;
import org.silverpeas.mobile.shared.dto.configuration.Config;

import javax.print.Doc;

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
  CheckBox lastPublicationsDisplay, lastEventsDisplay;
  @UiField
  CheckBox favoritesDisplay, shortCutsDisplay, shortCutsToolsDisplay;

  @UiField
  RadioButton standard, grayscale, sepia, inverse;

  @UiField
  InputElement fontSize;

  @UiField(provided = true)
  protected ConfigMessages msg;

  private Config config;
  public ConfigPage() {
    msg = GWT.create(ConfigMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractConfigPagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new LoadConfigEvent());
    setPageTitle(msg.title());
    fontSize.setAttribute("min", "9");
    fontSize.setAttribute("max", "17");
    fontSize.setAttribute("step", "1");
    fontSize.setAttribute("value", String.valueOf(SpMobil.getConfiguration().getFontSize()));
    addListenerInput(fontSize, this);
  }

  public native void addListenerInput(Element range, ConfigPage page) /*-{
    range.addEventListener('input', function () {
      page.@org.silverpeas.mobile.client.apps.config.pages.ConfigPage::updateFontSize(I)(range.value);
    }, false);
  }-*/;

  private void updateFontSize(final int value) {
    fontSize.setAttribute("value", String.valueOf(value));
    SpMobil.setFontSize(value);
    save();
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

  @UiHandler("standard")
  protected void standard(ClickEvent event) {
    Config c = new Config();
    c.setStandard(true);
    SpMobil.setFilter(c);
  }
  @UiHandler("grayscale")
  protected void grayscale(ClickEvent event) {
    Config c = new Config();
    c.setGrayscale(true);
    SpMobil.setFilter(c);
  }

  @UiHandler("sepia")
  protected void sepia(ClickEvent event) {
    Config c = new Config();
    c.setSepia(true);
    SpMobil.setFilter(c);
  }

  @UiHandler("inverse")
  protected void inverse(ClickEvent event) {
    Config c = new Config();
    c.setInverse(true);
    SpMobil.setFilter(c);
  }

  @Override
  public void onConfigLoaded(ConfigLoadedEvent event) {
    config = event.getConfig();
    newsDisplay.setValue(config.isNewsDisplay());
    lastPublicationsDisplay.setValue(config.isLastPublicationsDisplay());
    favoritesDisplay.setValue(config.isFavoritesDisplay());
    lastEventsDisplay.setValue(config.isLastEventsDisplay());
    shortCutsDisplay.setValue(config.isShortCutsDisplay());
    shortCutsToolsDisplay.setValue(config.isShortCutsToolsDisplay());
    standard.setValue(config.isStandard());
    grayscale.setValue(config.isGrayscale());
    sepia.setValue(config.isSepia());
    inverse.setValue(config.isInverse());
  }

  private void save() {
    config.setNewsDisplay(newsDisplay.getValue());
    config.setFavoritesDisplay(favoritesDisplay.getValue());
    config.setLastPublicationsDisplay(lastPublicationsDisplay.getValue());
    config.setLastEventsDisplay(lastEventsDisplay.getValue());
    config.setShortCutsDisplay(shortCutsDisplay.getValue());
    config.setShortCutsToolsDisplay(shortCutsToolsDisplay.getValue());
    config.setFontSize(Integer.parseInt(fontSize.getAttribute("value")));
    config.setStandard(standard.getValue());
    config.setGrayscale(grayscale.getValue());
    config.setSepia(sepia.getValue());
    config.setInverse(inverse.getValue());
    EventBus.getInstance().fireEvent(new UpdateConfigEvent(config));
  }

  interface ConfigPageUiBinder extends UiBinder<HTMLPanel, ConfigPage> {}
}