/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

package org.silverpeas.mobile.client.apps.contacts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.contacts.events.app.*;
import org.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import org.silverpeas.mobile.client.apps.contacts.pages.ContactPage;
import org.silverpeas.mobile.client.apps.contacts.pages.ContactsPage;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.contact.ContactFilters;

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
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<DetailUserDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceContact()
            .getContacts(event.getType(), event.getFilter(), event.getPageSize(),
                event.getStartIndex(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<DetailUserDTO> result) {
        super.onSuccess(method, result);
        // Notify view
        EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
      }
    };
    action.attempt();
  }

  @Override
  public void loadContact(ContactLoadEvent event) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<DetailUserDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceContact().getContact(event.getUserId(), this);
      }

      @Override
      public void onSuccess(Method method, DetailUserDTO detailUserDTO) {
        super.onSuccess(method, detailUserDTO);
        ContactPage page = new ContactPage();
        page.setPageTitle(detailUserDTO.getFirstName() + " " + detailUserDTO.getLastName());
        page.setData(detailUserDTO);
        page.show();
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

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<DetailUserDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceContact()
            .getContactsFiltered(event.getType(), event.getFilter(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<DetailUserDTO> result) {
        super.onSuccess(method, result);
        ContactsApp.super.start();
        // Notify view
        ContactsLoadedEvent pageEvent = new ContactsLoadedEvent(result);
        EventBus.getInstance().fireEvent(pageEvent);
      }
    };
    action.attempt();
  }

  @Override
  public void appInstanceChanged(
      final NavigationAppInstanceChangedEvent event) { /* only one instance */ }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.Contacts.toString())) {

      MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<ContactFilters>() {
        @Override
        public void attempt() {
          super.attempt();
          ServicesLocator.getServiceContact().hasContacts(this);
        }

        @Override
        public void onFailure(final Method method, final Throwable t) {
          super.onFailure(method, t);
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
        public void onSuccess(final Method method, final ContactFilters result) {
          super.onSuccess(method, result);
          ContactsPage page = (ContactsPage) getMainPage();
          if (page != null) {
            page.stop();
          }
          page = new ContactsPage();
          setMainPage(page);
          page.setContactsVisible(result.isHasContacts());
          page.setPersonnalContactsVisible(result.isHasPersonnalContacts());
          page.init(false);
          ContactsApp.super.start();
        }
      };
      action.attempt();
    }
  }
}
