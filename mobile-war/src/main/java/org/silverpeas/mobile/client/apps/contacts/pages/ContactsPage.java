/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.client.apps.contacts.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.contacts.events.app.ContactsLoadEvent;
import org.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsPagesEvent;
import org.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import org.silverpeas.mobile.client.apps.contacts.events.pages.ContactsPagesEventHandler;
import org.silverpeas.mobile.client.apps.contacts.events.pages.ContactsStopPagesdEvent;
import org.silverpeas.mobile.client.apps.contacts.pages.widgets.ContactItem;
import org.silverpeas.mobile.client.apps.contacts.resources.ContactsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.contact.ContactFilters;

public class ContactsPage extends PageContent implements ContactsPagesEventHandler {

  private static ContactsPageUiBinder uiBinder = GWT.create(ContactsPageUiBinder.class);

  @UiField(provided = true) protected ContactsMessages msg = null;
  @UiField HTMLPanel container;
  @UiField Anchor mycontacts, allcontacts, allextcontacts, more;
  @UiField UnorderedList list;
  @UiField TextBox filter;
  private int callNumber, startIndex, pageSize = 0;
  private String currentFilter = "";
  private String currentType = ContactFilters.MY;
  private static Timer command = null;

  private static boolean cancelTimer = false;

  public void setContactsVisible(final boolean contactsVisible) {
    if (contactsVisible) {
      allcontacts.removeStyleName("ui-last-child");
    } else {
      allcontacts.addStyleName("ui-last-child");
    }
    allextcontacts.setVisible(contactsVisible);
  }

  public void setPersonnalContactsVisible(final boolean personnalContactsVisible) {
    mycontacts.setVisible(personnalContactsVisible);
  }

  interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
  }

  public ContactsPage() {
    msg = GWT.create(ContactsMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    filter.setVisible(false);
    container.getElement().setId("contacts");
    mycontacts.getElement().setId("btn-my-contacts");
    mycontacts.setVisible(false);
    allcontacts.getElement().setId("btn-all-contacts");
    allcontacts.setVisible(false);
    allextcontacts.getElement().setId("btn-all-contactsext");
    allextcontacts.setVisible(false);
    EventBus.getInstance().addHandler(AbstractContactsPagesEvent.TYPE, this);

    list.getElement().setId("list-contacts");
  }

  public void init(boolean limited) {
    list.clear();

    String tabs = ResourcesManager.getParam("directory.display.tabs");
    String defaultTab = ResourcesManager.getParam("directory.display.tab.default");

    if (!limited) {
        mycontacts.setVisible(tabs.contains("mycontacts"));
        allcontacts.setVisible(tabs.contains("allcontacts"));
        allextcontacts.setVisible(tabs.contains("allextcontacts"));

      Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
        @Override
        public boolean execute() {
          if (defaultTab.equals("mycontacts")) {
            computePosition();
            EventBus.getInstance().fireEvent(
                new ContactsLoadEvent(ContactFilters.MY, filter.getText(), pageSize,
                    startIndex));
            currentType = ContactFilters.MY;
            mycontacts.addStyleName("ui-btn-active");
            mycontacts.addStyleName("ui-first-child");
            if (!allcontacts.isVisible() && !allextcontacts.isVisible()) {
              mycontacts.addStyleName("ui-last-child");
            }
            filter.setVisible(false);
          } else if (defaultTab.equals("allcontacts")) {
            computePosition();
            EventBus.getInstance().fireEvent(
                new ContactsLoadEvent(ContactFilters.ALL, filter.getText(), pageSize,
                    startIndex));
            currentType = ContactFilters.ALL;
            allcontacts.addStyleName("ui-btn-active");
            allcontacts.removeStyleName("ui-last-child");
            if (!mycontacts.isVisible()) {
              allcontacts.addStyleName("ui-first-child");
            }
            if (!allextcontacts.isVisible()) {
              allcontacts.addStyleName("ui-last-child");
            }
            filter.setVisible(true);
          } else if (defaultTab.equals("allextcontacts")) {
            computePosition();
            EventBus.getInstance().fireEvent(
                new ContactsLoadEvent(ContactFilters.ALL_EXT, filter.getText(), pageSize,
                    startIndex));
            currentType = ContactFilters.ALL_EXT;
            allextcontacts.addStyleName("ui-btn-active");
            if (!mycontacts.isVisible() && !allcontacts.isVisible()) {
              allextcontacts.addStyleName("ui-first-child");
            }
            allextcontacts.addStyleName("ui-last-child");
            filter.setVisible(true);
          }
          return false;
        }
      }, 500);
    } else {
      mycontacts.setVisible(false);
      allextcontacts.setVisible(false);
      allcontacts.setVisible(false);
      filter.setVisible(true);
    }
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractContactsPagesEvent.TYPE, this);
  }

  @UiHandler("mycontacts")
  protected void showMycontacts(ClickEvent event) {
    allcontacts.removeStyleName("ui-btn-active");
    allcontacts.addStyleName("ui-btn");
    allextcontacts.removeStyleName("ui-btn-active");
    allextcontacts.addStyleName("ui-btn");
    mycontacts.addStyleName("ui-btn-active");
    currentType = ContactFilters.MY;
    filter.setText("");
    filter.setVisible(false);
    currentFilter = "";
    list.clear();
    callNumber = 0;
    computePosition();
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(currentType, filter.getText(), pageSize, startIndex));
  }

  @UiHandler("allcontacts")
  protected void showAllcontacts(ClickEvent event) {
    mycontacts.removeStyleName("ui-btn-active");
    mycontacts.addStyleName("ui-btn");
    allextcontacts.removeStyleName("ui-btn-active");
    allextcontacts.addStyleName("ui-btn");
    allcontacts.addStyleName("ui-btn-active");
    currentType = ContactFilters.ALL;
    filter.setText("");
    filter.setVisible(true);
    currentFilter = "";
    list.clear();
    callNumber = 0;
    computePosition();
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(currentType, filter.getText(), pageSize, startIndex));
  }

  @UiHandler("allextcontacts")
  protected void showAllExtcontacts(ClickEvent event) {
    mycontacts.removeStyleName("ui-btn-active");
    mycontacts.addStyleName("ui-btn");
    allcontacts.removeStyleName("ui-btn-active");
    allcontacts.addStyleName("ui-btn");

    allextcontacts.addStyleName("ui-btn-active");
    currentType = ContactFilters.ALL_EXT;
    filter.setText("");
    filter.setVisible(true);
    currentFilter = "";
    list.clear();
    callNumber = 0;
    computePosition();
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(currentType, filter.getText(), pageSize, startIndex));
  }

  @UiHandler("more")
  protected void moreContacts(ClickEvent event) {
    computePosition();
    EventBus.getInstance()
            .fireEvent(new ContactsLoadEvent(currentType, filter.getText(), pageSize, startIndex));
  }

  @UiHandler("filter")
  protected void filter(KeyUpEvent event) {
    cancelTimer = true;
    if(!currentFilter.equalsIgnoreCase(filter.getText())) {
      currentFilter = filter.getText();
      if (command != null) {
        command.cancel();
        ContactsPage.command = null;
      }
      command = new Timer() {
        @Override
        public void run() {
          if (!cancelTimer) {
            list.clear();
            callNumber = 0;
            computePosition();
            EventBus.getInstance()
                    .fireEvent(new ContactsLoadEvent(currentType, filter.getText(), pageSize, startIndex));
            ContactsPage.command = null;
          }
        }
      };
      cancelTimer = false;
      command.schedule(500);
    }
  }

  @Override
  public void onContactsLoaded(ContactsLoadedEvent event) {

    for (DetailUserDTO user : event.getListUserDetailDTO()) {
      if (user != null) {
        ContactItem item = new ContactItem();
        item.setData(user);
        list.add(item);
      }
    }
  }

  @Override
  public void onStopPage(final ContactsStopPagesdEvent contactsStopPagesdEvent) {
    this.stop();
  }


  private void computePosition() {
    pageSize = 15;
    if (callNumber == 0) {
      startIndex = 0;
    } else {
      startIndex += pageSize;
    }
    callNumber++;
  }
}