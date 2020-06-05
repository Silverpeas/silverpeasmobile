/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.client.apps.contacts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.silverpeas.mobile.client.apps.contacts.events.app.AbstractContactsAppEvent;
import org.silverpeas.mobile.client.apps.contacts.events.app.ContactsAppEventHandler;
import org.silverpeas.mobile.client.apps.contacts.events.app.ContactsFilteredLoadEvent;
import org.silverpeas.mobile.client.apps.contacts.events.app.ContactsLoadEvent;
import org.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import org.silverpeas.mobile.client.apps.contacts.pages.ContactsPage;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.contact.ContactFilters;

import java.util.ArrayList;
import java.util.List;

public class ContactsApp extends App implements ContactsAppEventHandler, NavigationEventHandler {

  private ApplicationMessages msg;

  public ContactsApp() {
    super();
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractContactsAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start() {
    // no "super.start(lauchingPage);" this apps is used in another apps
  }

  @Override
  public void stop() {
    // never stop
  }

  @Override
  public void loadContacts(final ContactsLoadEvent event) {
    AsyncCallbackOnlineOrOffline action =
        new AsyncCallbackOnlineOrOffline<List<DetailUserDTO>>(getOfflineAction(event)) {

          @Override
          public void attempt() {
            Notification.activityStart();
            ServicesLocator.getServiceContact()
                .getContacts(event.getType(), event.getFilter(), event.getPageSize(),
                    event.getStartIndex(), this);
          }

          @Override
          public void onSuccess(List<DetailUserDTO> result) {
            super.onSuccess(result);
            // Store in local storage
            storeInLocalStorage(result, event);
            // Notify view
            EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
          }
        };
    action.attempt();
  }

  @Override
  public void loadContactsFiltered(final ContactsFilteredLoadEvent event) {
    ContactsPage page = (ContactsPage) getMainPage();
    if (page != null) {
      page.stop();
    }
    page = new ContactsPage();
    setMainPage(page);

    page.setContactsVisible(false);
    page.setPersonnalContactsVisible(false);
    page.init(true);

    AsyncCallbackOnlineOrOffline action =
        new AsyncCallbackOnlineOrOffline<List<DetailUserDTO>>(getOfflineAction(event)) {

          @Override
          public void attempt() {
            Notification.activityStart();
            ServicesLocator.getServiceContact()
                .getContactsFiltered(event.getType(), event.getFilter(), this);
          }

          @Override
          public void onSuccess(List<DetailUserDTO> result) {
            super.onSuccess(result);
            // Store in local storage
            storeInLocalStorage(result, event);
            // Notify view
            ContactsLoadedEvent pageEvent = new ContactsLoadedEvent(result);
            EventBus.getInstance().fireEvent(pageEvent);
            ContactsApp.super.start();
          }
        };
    action.attempt();
  }

  private Command getOfflineAction(final ContactsFilteredLoadEvent event) {
    Command offlineAction = new Command() {

      @Override
      public void execute() {
        String key = "contact" + event.getType() + "_" + event.getFilter();
        List<DetailUserDTO> result = LocalStorageHelper.load(key, List.class);
        if (result == null) {
          result = new ArrayList<DetailUserDTO>();
        }
        // Notify view
        ContactsLoadedEvent pageEvent = new ContactsLoadedEvent(result);
        EventBus.getInstance().fireEvent(pageEvent);
        ContactsApp.super.start();
      }
    };

    return offlineAction;
  }

  private void storeInLocalStorage(final List<DetailUserDTO> result,
      final ContactsFilteredLoadEvent event) {
    String key = "contact" + event.getType() + "_" + event.getFilter();
    LocalStorageHelper.store(key, List.class, result);
  }

  private Command getOfflineAction(final ContactsLoadEvent event) {
    Command offlineAction = new Command() {

      @Override
      public void execute() {
        String key =
            "contact" + event.getType() + "_" + event.getFilter() + "_" + event.getPageSize() +
                "_" + event.getStartIndex();
        List<DetailUserDTO> result = LocalStorageHelper.load(key, List.class);
        if (result == null) {
          result = new ArrayList<DetailUserDTO>();
        }
        EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
      }
    };

    return offlineAction;
  }

  private void storeInLocalStorage(final List<DetailUserDTO> result,
      final ContactsLoadEvent event) {
    String key =
        "contact" + event.getType() + "_" + event.getFilter() + "_" + event.getPageSize() + "_" +
            event.getStartIndex();
    LocalStorageHelper.store(key, List.class, result);
  }

  @Override
  public void appInstanceChanged(
      final NavigationAppInstanceChangedEvent event) { /* only one instance */ }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.Contacts.toString())) {
      ServicesLocator.getServiceContact().hasContacts(new AsyncCallback<ContactFilters>() {
        @Override
        public void onFailure(final Throwable throwable) {
          Notification.activityStop();
          ContactsPage page = (ContactsPage) getMainPage();
          if (page != null) {
            page.stop();
          }
          page = new ContactsPage();
          setMainPage(page);
          page.setContactsVisible(true);
          page.init(false);
          ContactsApp.super.start();
        }

        @Override
        public void onSuccess(final ContactFilters result) {
          Notification.activityStop();
          ContactsPage page = (ContactsPage) getMainPage();
          if (page != null) {
            page.stop();
          }
          page = new ContactsPage();
          setMainPage(page);
          page.setContactsVisible(result.hasContacts());
          page.setPersonnalContactsVisible(result.hasPersonnalContacts());
          page.init(false);
          ContactsApp.super.start();
        }
      });
    }
  }
}
