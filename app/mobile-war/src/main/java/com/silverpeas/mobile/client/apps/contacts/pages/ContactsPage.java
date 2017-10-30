package com.silverpeas.mobile.client.apps.contacts.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsPagesEventHandler;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsStopPagesdEvent;
import com.silverpeas.mobile.client.apps.contacts.pages.widgets.ContactItem;
import com.silverpeas.mobile.client.apps.contacts.resources.ContactsMessages;
import com.silverpeas.mobile.client.common.EventBus;
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
  @UiField Anchor mycontacts, allcontacts, allextcontacts;
  @UiField UnorderedList list;
  @UiField TextBox filter;
  private int startIndexAll, startIndexMy, startIndex, pageSize = 0;
  private String currentFilter = "";
  private String currentType = ContactFilters.MY;
  private ContactItem itemWaiting;
  private boolean callingNexData = false;
  private boolean noMoreData = false;
  private static Scheduler.RepeatingCommand command = null;

  public void setContactsVisible(final boolean contactsVisible) {
    if (contactsVisible) {
      allcontacts.removeStyleName("ui-last-child");
    } else {
      allcontacts.addStyleName("ui-last-child");
    }
    allextcontacts.setVisible(contactsVisible);
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
    allcontacts.getElement().setId("btn-all-contacts");
    allextcontacts.getElement().setId("btn-all-contactsext");
    EventBus.getInstance().addHandler(AbstractContactsPagesEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractScrollEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(ContactFilters.MY, filter.getText(), computePageSize(), startIndexMy));
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
    allextcontacts.removeStyleName("ui-btn-active");
    allextcontacts.addStyleName("ui-btn");
    mycontacts.addStyleName("ui-btn-active");
    currentType = ContactFilters.MY;
    filter.setText("");
    filter.setVisible(false);
    currentFilter = "";
    list.clear();
    pageSize = 0;
    startIndexMy = 0;
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(currentType, filter.getText(), computePageSize(), startIndexMy));
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
    pageSize = 0;
    startIndexAll = 0;
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(currentType, filter.getText(), computePageSize(), startIndexAll));
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
    pageSize = 0;
    startIndexAll = 0;
    EventBus.getInstance().fireEvent(new ContactsLoadEvent(currentType, filter.getText(), computePageSize(), startIndexAll));
  }

  @UiHandler("filter")
  protected void filterChosen(FocusEvent event) {
    mycontacts.removeStyleName("ui-btn-active");
    mycontacts.addStyleName("ui-btn");
    allcontacts.removeStyleName("ui-btn-active");
    allcontacts.addStyleName("ui-btn");
  }

  @UiHandler("filter")
  protected void filter(KeyUpEvent event) {
    if(!currentFilter.equalsIgnoreCase(filter.getText())) {
      currentFilter = filter.getText();

      if (command == null) {
        command = new Scheduler.RepeatingCommand() {
          @Override
          public boolean execute() {
            list.clear();
            pageSize = 0;
            startIndex = 0;
            EventBus.getInstance()
                .fireEvent(new ContactsLoadEvent(currentType, filter.getText(), computePageSize(), startIndex));
            ContactsPage.command = null;
            return false;
          }
        };
        Scheduler.get().scheduleFixedDelay(command, 300);
      }
    }
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
  public void onStopPage(final ContactsStopPagesdEvent contactsStopPagesdEvent) {
    this.stop();
  }

  @Override
  public void onScrollEndPage(final EndPageEvent event) {
    if (callingNexData == false && noMoreData == false) {
      callingNexData = true;
      list.add(getWaitingItem());
      Window.scrollTo(0, Document.get().getScrollHeight());

      startIndexAll += computePageSize();
      EventBus.getInstance()
          .fireEvent(new ContactsLoadEvent(currentType, filter.getText(), computePageSize(), startIndexAll));
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
      int available = Window.getClientHeight() - (SpMobil.getMainPage().getHeaderHeight() + container.getOffsetHeight());

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