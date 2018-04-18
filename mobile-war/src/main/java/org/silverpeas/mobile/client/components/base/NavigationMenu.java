/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.config.ConfigApp;
import org.silverpeas.mobile.client.apps.status.StatusApp;
import org.silverpeas.mobile.client.apps.status.events.StatusEvents;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.base.events.page.AbstractPageEvent;
import org.silverpeas.mobile.client.components.base.events.page.DataLoadedEvent;
import org.silverpeas.mobile.client.components.base.events.page.LoadingDataFinishEvent;
import org.silverpeas.mobile.client.components.base.events.page.MoreDataLoadedEvent;
import org.silverpeas.mobile.client.components.base.events.page.PageEvent;
import org.silverpeas.mobile.client.components.base.events.page.PageEventHandler;
import org.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.StatusDTO;

public class NavigationMenu extends Composite implements PageEventHandler {

  private static NavigationMenuUiBinder uiBinder = GWT.create(NavigationMenuUiBinder.class);

  @UiField HTMLPanel container, user;
  @UiField Anchor home, disconnect, updateStatus, searchButton, help, config, tchat;
  @UiField SpanElement status;
  @UiField TextBox search;

  @UiField(provided = true) protected ApplicationMessages msg = null;

  private ApplicationResources resources = GWT.create(ApplicationResources.class);

  interface NavigationMenuUiBinder extends UiBinder<Widget, NavigationMenu> {
  }

  public NavigationMenu() {
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("silverpeas-navmenu-panel");
    container.getElement().getStyle().setHeight(Window.getClientHeight(), Unit.PX);
    user.getElement().setId("user");
    String url = ResourcesManager.getParam("help.url");
    if (url != null && !url.isEmpty()) {
      help.setHref(url);
      help.setTarget("_blank");
    }
    tchat.setVisible(Boolean.parseBoolean(ResourcesManager.getParam("chat.enable")));

    EventBus.getInstance().addHandler(AbstractPageEvent.TYPE, this);
  }

  @Override
  public void receiveEvent(PageEvent event) {
    if (event.getSender() instanceof StatusApp && event.getName().equals(StatusEvents.POSTED.toString())) {
      StatusDTO newStatus = (StatusDTO) event.getData();
      status.setInnerHTML(newStatus.getDescription());
    }
  }

  @Override
  public void finishLoadingData(final LoadingDataFinishEvent loadingDataFinishEvent) {
  }

  @Override
  public void loadedDataEvent(final DataLoadedEvent dataLoadedEvent) {

  }

  @Override
  public void loadedMoreDataEvent(final MoreDataLoadedEvent moreDataLoadedEvent) {

  }

  public void toogleMenu() {
    if (container.getStyleName().contains("ui-panel-close")) {
      container.removeStyleName("ui-panel-close");
      container.addStyleName("ui-panel-open");
    } else {
      closeMenu();
    }
  }

  public void closeMenu() {
    container.removeStyleName("ui-panel-open");
    container.addStyleName("ui-panel-close");
  }

  public void resetSearchField() {
    search.setText("");
    search.setFocus(false);
  }

  @UiHandler("search")
  protected void search(KeyDownEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
      SpMobil.search(search.getText());
    }
  }

  @UiHandler("searchButton")
  protected void searchIos(ClickEvent event) {
    SpMobil.search(search.getText());
  }

  @UiHandler("home")
  protected void goHome(ClickEvent event) {
    PageHistory.getInstance().goBackToFirst();
    closeMenu();
  }

  @UiHandler("help")
  protected void goHelp(ClickEvent event) {
    closeMenu();
  }

  @UiHandler("config")
  protected void goConfig(ClickEvent event) {
    App app = new ConfigApp();
    app.start();
    closeMenu();
  }

  @UiHandler("updateStatus")
  protected void updateStatus(ClickEvent event) {
    App app = new StatusApp();
    app.start();
    closeMenu();
  }

  @UiHandler("disconnect")
  protected void disconnect(ClickEvent event) {
    closeMenu();
    AuthentificationManager.getInstance().clearLocalStorage();
    PageHistory.getInstance().clear();
    ServicesLocator.getServiceNavigation().logout(new AsyncCallback<Void>() {
      @Override
      public void onFailure(final Throwable throwable) {
        Notification.activityStop();
      }
      @Override
      public void onSuccess(final Void aVoid) {
        Notification.activityStop();
        ConnexionPage connexionPage = new ConnexionPage();
        RootPanel.get().clear();
        RootPanel.get().add(connexionPage);
        SpMobil.destroyMainPage();
      }
    });
  }

  public void setUser(DetailUserDTO currentUser) {
    if (currentUser.getAvatar().isEmpty()) {
      Image avatar = new Image(resources.avatar());
      avatar.getElement().removeAttribute("height");
      avatar.getElement().removeAttribute("width");
      user.addAndReplaceElement(avatar, "avatar");
    } else {
      user.addAndReplaceElement(new Image(currentUser.getAvatar()), "avatar");
    }
    user.addAndReplaceElement(
        new InlineHTML(" " + currentUser.getFirstName() + " " + currentUser.getLastName()),
        "userName");
    status.setInnerHTML(currentUser.getStatus());
  }
}
