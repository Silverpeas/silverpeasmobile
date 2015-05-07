package com.silverpeas.mobile.client.apps.contacts;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.contacts.events.app.AbstractContactsAppEvent;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsAppEventHandler;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.pages.ContactsPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactsApp extends App implements ContactsAppEventHandler {

  public ContactsApp(){
    super();
  }

  public void start(){
    EventBus.getInstance().addHandler(AbstractContactsAppEvent.TYPE, this);
    setMainPage(new ContactsPage());
    super.start();
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractContactsAppEvent.TYPE, this);
    super.stop();
  }

  @Override
  public void loadContacts(final ContactsLoadEvent event) {
    ServicesLocator.getServiceContact().getContacts(event.getFilter(), event.getPageSize(),
        event.getStartIndex(), new AsyncCallback<List<DetailUserDTO>>() {
      public void onFailure(Throwable caught) {
        if (OfflineHelper.needToGoOffine(caught)) {
          // Use local storage data
          List<DetailUserDTO> result = loadInLocalStorage(event);
          EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
        } else {
          EventBus.getInstance().fireEvent(new ErrorEvent(caught));
        }
      }

      public void onSuccess(List<DetailUserDTO> result) {
        // Store in local storage
        storeInLocalStorage(result, event);
        // Notify view
        EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
      }
    });
  }

  private List<DetailUserDTO> loadInLocalStorage(final ContactsLoadEvent event) {
    String key = "contact" + event.getFilter() + "_" + event.getPageSize() + "_" + event.getStartIndex();
    List<DetailUserDTO> result = LocalStorageHelper.load(key, List.class);
    if (result == null) {
      result = new ArrayList<DetailUserDTO>();
    }
    return result;
  }

  private void storeInLocalStorage(final List<DetailUserDTO> result, final ContactsLoadEvent event) {
    String key = "contact" + event.getFilter() + "_" + event.getPageSize() + "_" +
        event.getStartIndex();
    LocalStorageHelper.store(key, List.class , result);
  }
}
