package com.silverpeas.mobile.client.apps.contacts.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsPagesEventHandler;
import com.silverpeas.mobile.client.apps.contacts.pages.widgets.ContactItem;
import com.silverpeas.mobile.client.apps.contacts.resources.ContactsMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.components.base.events.AbstractScrollEvent;
import com.silverpeas.mobile.client.components.base.events.EndPageEvent;
import com.silverpeas.mobile.client.components.base.events.ScrollEventHandler;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.contact.ContactFilters;

import java.util.Iterator;

public class ContactsPage extends PageContent implements ContactsPagesEventHandler, ScrollEventHandler {

  private static ContactsPageUiBinder uiBinder = GWT.create(ContactsPageUiBinder.class);

  @UiField(provided = true) protected ContactsMessages msg = null;
  @UiField HTMLPanel container;
  @UiField Anchor mycontacts, allcontacts;
  @UiField UnorderedList list;
  private int startIndexAll, startIndexMy, pageSize = 0;
  private boolean allContacts = false;
  private ContactItem itemWaiting;
  private boolean callingNexData = false;
  private boolean noMoreData = false;


  interface ContactsPageUiBinder extends UiBinder<Widget, ContactsPage> {
  }

  public ContactsPage() {
    msg = GWT.create(ContactsMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("contacts");
    mycontacts.getElement().setId("btn-my-contacts");
    allcontacts.getElement().setId("btn-all-contacts");
    EventBus.getInstance().addHandler(AbstractContactsPagesEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractScrollEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(ContactFilters.MY, computePageSize(), startIndexMy));
    list.getElement().setId("list-contacts");
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractContactsPagesEvent.TYPE, this);
    EventBus.getInstance().removeHandler(AbstractScrollEvent.TYPE, this);
  }

  @UiHandler("mycontacts")
  protected void showMycontacts(ClickEvent event) {
    allcontacts.removeStyleName("ui-btn-active");
    allcontacts.addStyleName("ui-btn");
    mycontacts.addStyleName("ui-btn-active");
    allContacts = false;
    list.clear();
    pageSize = 0;
    startIndexMy = 0;
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(ContactFilters.MY, computePageSize(), startIndexMy));
  }

  @UiHandler("allcontacts")
  protected void showAllcontacts(ClickEvent event) {
    mycontacts.removeStyleName("ui-btn-active");
    mycontacts.addStyleName("ui-btn");
    allcontacts.addStyleName("ui-btn-active");
    allContacts = true;
    list.clear();
    pageSize = 0;
    startIndexAll = 0;
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(ContactFilters.ALL, computePageSize(), startIndexAll));
  }

  @Override
  public void onContactsLoaded(ContactsLoadedEvent event) {
    noMoreData = event.getListUserDetailDTO().isEmpty();
    list.remove(getWaitingItem());
    Iterator<DetailUserDTO> i = event.getListUserDetailDTO().iterator();
    while (i.hasNext()) {
      DetailUserDTO user = i.next();
      if (user != null) {
        ContactItem item = new ContactItem();
        item.setData(user);
        list.add(item);
      }
    }
    callingNexData = false;
  }

  @Override
  public void onScrollEndPage(final EndPageEvent event) {
    if (callingNexData == false && noMoreData == false) {
      callingNexData = true;
      list.add(getWaitingItem());
      Window.scrollTo(0, Document.get().getScrollHeight());
      if (allContacts) {
        startIndexAll += computePageSize();
        EventBus.getInstance()
            .fireEvent(new ContactsLoadEvent(ContactFilters.ALL, computePageSize(), startIndexAll));
      } else {
        startIndexMy += computePageSize();
        EventBus.getInstance()
            .fireEvent(new ContactsLoadEvent(ContactFilters.MY, computePageSize(), startIndexMy));
      }
    }
  }

  private ContactItem getWaitingItem() {
    if (itemWaiting == null) {
      itemWaiting = new ContactItem();
      itemWaiting.setData(SpMobil.user);
      itemWaiting.hideData();
      itemWaiting.setStyleName("csspinner traditional");
    }
    return itemWaiting;
  }

  private int computePageSize() {
    if (pageSize == 0) {
      // compute height available for items
      int available = Window.getClientHeight() - (SpMobil.mainPage.getHeaderHeight() + container.getOffsetHeight());

      // compute item height
      ContactItem item = new ContactItem();
      item.setData(SpMobil.user);
      item.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
      list.add(item);
      int itemHeight = item.getOffsetHeight();
      list.remove(item);

      pageSize =  (available / itemHeight) + 1; // add one for scroll
    }
    return pageSize;
  }
}