package com.silverpeas.mobile.client.apps.contacts;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.contacts.events.app.AbstractContactsAppEvent;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsAppEventHandler;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.apps.contacts.pages.ContactsPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.network.ConnectionHelper;
import com.silverpeas.mobile.client.persist.ListDetailUserDTO;
import com.silverpeas.mobile.client.persist.ListDetailUserDTOCodec;
import com.silverpeas.mobile.client.persist.User;
import com.silverpeas.mobile.client.persist.UserCodec;
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
    ServicesLocator.serviceContact.getContacts(event.getFilter(), event.getPageSize(), event.getStartIndex(), new AsyncCallback<List<DetailUserDTO>>() {
      public void onFailure(Throwable caught) {
        if (ConnectionHelper.needToGoOffine(caught)) {
          Storage storage = Storage.getLocalStorageIfSupported();
          List<DetailUserDTO> result = null;
          if (storage != null) {
            String key = "contact" + event.getFilter() + "_" + event.getPageSize() + "_" + event.getStartIndex();
            String dataItem = storage.getItem(key);
            if (dataItem == null) {
              result = new ArrayList<DetailUserDTO>();
            } else {
              ListDetailUserDTOCodec codec = GWT.create(ListDetailUserDTOCodec.class);
              ListDetailUserDTO r  = codec.decode(JSONParser.parseStrict(dataItem));
              result = r.users;
            }
          } else{
            result = new ArrayList<DetailUserDTO>();
          }
          EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
        } else {
          EventBus.getInstance().fireEvent(new ErrorEvent(caught));
        }
      }
      public void onSuccess(List<DetailUserDTO> result) {
        // Store in local storage
        Storage storage = Storage.getLocalStorageIfSupported();
        if (storage != null) {
          ListDetailUserDTOCodec codec = GWT.create(ListDetailUserDTOCodec.class);
          JSONValue json = codec.encode(new ListDetailUserDTO(result));
          String key = "contact" + event.getFilter() + "_" + event.getPageSize() + "_" + event.getStartIndex();
          storage.setItem(key, json.toString());
        }
        // Notify view
        EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
      }
    });
  }
}
