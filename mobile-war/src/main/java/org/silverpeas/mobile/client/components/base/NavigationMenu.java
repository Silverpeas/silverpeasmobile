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

package org.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.config.ConfigApp;
import org.silverpeas.mobile.client.apps.navigation.events.app.external
    .NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.navigation.pages.widgets.NavigationItem;
import org.silverpeas.mobile.client.apps.profile.ProfileApp;
import org.silverpeas.mobile.client.apps.profile.events.ProfileEvents;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.events.page.AbstractPageEvent;
import org.silverpeas.mobile.client.components.base.events.page.DataLoadedEvent;
import org.silverpeas.mobile.client.components.base.events.page.LoadingDataFinishEvent;
import org.silverpeas.mobile.client.components.base.events.page.MoreDataLoadedEvent;
import org.silverpeas.mobile.client.components.base.events.page.PageEvent;
import org.silverpeas.mobile.client.components.base.events.page.PageEventHandler;
import org.silverpeas.mobile.client.components.base.widgets.AvatarUpload;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.StatusDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.helpers.ApplicationsHelper;

import java.util.List;

public class NavigationMenu extends Composite implements PageEventHandler {

  private static NavigationMenuUiBinder uiBinder = GWT.create(NavigationMenuUiBinder.class);

  @UiField HTMLPanel container, user;
  @UiField Anchor home, disconnect, updateStatus, help, config, calendar, notifications, shares;
  @UiField SpanElement status, iconHelp, iconSettings, iconHome, iconLogout, iconUserCalendar, iconInbox, iconShareBox;
  @UiField TextBox search;
  @UiField AvatarUpload avatar;

  @UiField
  AnchorElement searchButton;

  @UiField(provided = true) protected ApplicationMessages msg = null;

  @UiField UnorderedList listApplications;

  private boolean personalAppsInitialized = false;

  private ApplicationResources resources = GWT.create(ApplicationResources.class);

  public boolean isPersonalAppsInitialized() {
    return personalAppsInitialized;
  }

  interface NavigationMenuUiBinder extends UiBinder<Widget, NavigationMenu> {
  }

  public NavigationMenu() {
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("silverpeas-navmenu-panel");
    listApplications.getElement().setId("personals-apps");
    user.getElement().setId("user");
    String url = ResourcesManager.getParam("help.url");
    if (url != null && !url.isEmpty()) {
      String target = ResourcesManager.getParam("help.target");
      help.setHref(url);
      if (target != null && !target.isEmpty()) {
        help.setTarget(target);
      } else {
        help.setTarget("_blank");
      }
    }
    iconSettings.setInnerHTML(resources.settings().getText());
    iconHelp.setInnerHTML(resources.help().getText());
    iconLogout.setInnerHTML(resources.logout().getText());
    iconHome.setInnerHTML(resources.home().getText());
    iconUserCalendar.setInnerHTML(resources.usercalendar().getText());
    iconInbox.setInnerHTML(resources.inbox().getText());
    iconShareBox.setInnerHTML(resources.sharebox().getText());
    searchButton.setInnerHTML(resources.search().getText());
    EventBus.getInstance().addHandler(AbstractPageEvent.TYPE, this);

    Event.sinkEvents(searchButton, Event.ONCLICK);

    Event.setEventListener(searchButton, new EventListener() {
      @Override
      public void onBrowserEvent(Event event) {
        if (event.getTypeInt() == Event.ONCLICK) {
          SpMobil.search(search.getText());
          event.preventDefault();
        }
      }
    });
  }

  public void setPersonalApps(List<ApplicationInstanceDTO> applicationInstanceDTOS) {
    listApplications.clear();
    for (ApplicationInstanceDTO app : applicationInstanceDTOS) {
      if (ApplicationsHelper.isSupportedApp(app)) {
        if (app.getType().equals(Apps.kmelia.name())) {
          app.setLabel(msg.myDocuments());
        }
        NavigationItem item = new NavigationItem();
        item.getElement().getFirstChildElement().removeClassName("icon-app");
        item.setData(app);
        listApplications.add(item);
      }
    }
    personalAppsInitialized = true;
  }

  @Override
  public void receiveEvent(PageEvent event) {
    if (event.getSender() instanceof ProfileApp && event.getName().equals(ProfileEvents.POSTED.toString())) {
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
      container.getElement().getStyle().setVisibility(Style.Visibility.VISIBLE);
      container.removeStyleName("ui-panel-close");
      container.addStyleName("ui-panel-open");

    } else {
      closeMenu();
    }
  }

  public void closeMenu() {
    container.removeStyleName("ui-panel-open");
    container.addStyleName("ui-panel-close");
    Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
      @Override
      public boolean execute() {
        container.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
        return false;
      }
    }, 400);

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

  @UiHandler("home")
  protected void goHome(ClickEvent event) {
    goHome();
    SpMobil.getMainPage().closeMenu();
  }

  static void goHome() {
    String url = Window.Location.getHref();
    SpMobil.getMainPage().getHeader().clearActions();
    if (url.contains("shortcutContentType")) {
      String homeUrl = url.substring(0,url.indexOf("?"));
      Window.Location.replace(homeUrl);
    } else {
      PageHistory.getInstance().goBackToFirst();
    }
  }

  @UiHandler("shares")
  protected void goSharesBox(ClickEvent event) {
    ContentDTO content = new ContentDTO();
    content.setType(ContentsTypes.SharesBox.toString());
    EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));
    SpMobil.getMainPage().closeMenu();
  }

  @UiHandler("notifications")
  protected void goNotificationsBox(ClickEvent event) {
    ContentDTO content = new ContentDTO();
    content.setType(ContentsTypes.NotificationsBox.toString());
    EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));
    SpMobil.getMainPage().closeMenu();
  }

  @UiHandler("help")
  protected void goHelp(ClickEvent event) {
    SpMobil.getMainPage().closeMenu();
  }

  @UiHandler("config")
  protected void goConfig(ClickEvent event) {
    App app = new ConfigApp();
    app.start();
    SpMobil.getMainPage().closeMenu();
  }

  @UiHandler("updateStatus")
  protected void updateStatus(ClickEvent event) {
    App app = new ProfileApp();
    app.start();
    SpMobil.getMainPage().closeMenu();
  }

  @UiHandler("disconnect")
  protected void disconnect(ClickEvent event) {
    SpMobil.getMainPage().closeMenu();
    AuthentificationManager.getInstance().logout();
  }

  @UiHandler("calendar")
  protected void calendar(ClickEvent event) {
    ApplicationInstanceDTO app = new ApplicationInstanceDTO();
    app.setPersonnal(true);
    app.setId("userCalendar" + SpMobil.getUser().getId() + "_PCI");
    app.setType(Apps.userCalendar.name());
    EventBus.getInstance().fireEvent(new NavigationAppInstanceChangedEvent(app));
    SpMobil.getMainPage().closeMenu();
  }

  public void setUser(DetailUserDTO currentUser) {
    avatar.setUser(currentUser,resources);
    InlineHTML html = new InlineHTML(" " + currentUser.getFirstName() + " " + currentUser.getLastName());
    html.getElement().setId("userName");
    user.addAndReplaceElement(html, "userName");
    status.setInnerHTML(currentUser.getStatus());

    if (currentUser.isNotificationBox()) {
      notifications.setVisible(true);
      iconInbox.getStyle().setDisplay(Style.Display.BLOCK);
    } else {
      notifications.setVisible(false);
      iconInbox.getStyle().setDisplay(Style.Display.NONE);
    }
  }
}
