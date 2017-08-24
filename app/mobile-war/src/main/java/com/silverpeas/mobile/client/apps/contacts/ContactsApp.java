package com.silverpeas.mobile.client.apps.contacts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
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
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

import java.util.ArrayList;
import java.util.List;

public class ContactsApp extends App implements ContactsAppEventHandler {

    private ApplicationMessages msg;

    public ContactsApp(){
        super();
        msg = GWT.create(ApplicationMessages.class);
    }

    public void start(){
        EventBus.getInstance().addHandler(AbstractContactsAppEvent.TYPE, this);

        ServicesLocator.getServiceContact().hasContacts(new AsyncCallback<Boolean>() {
          @Override
          public void onFailure(final Throwable throwable) {
            ContactsPage page = new ContactsPage();
            page.setContactsVisible(true);
            setMainPage(page);
            ContactsApp.super.start();
          }

          @Override
          public void onSuccess(final Boolean hasContacts) {
            ContactsPage page = new ContactsPage();
            page.setContactsVisible(hasContacts);
            setMainPage(page);
            ContactsApp.super.start();
          }
        });
    }

    @Override
    public void stop() {
        EventBus.getInstance().removeHandler(AbstractContactsAppEvent.TYPE, this);
        super.stop();
    }

    @Override
    public void loadContacts(final ContactsLoadEvent event) {
        AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<DetailUserDTO>>(getOfflineAction(event)) {

            @Override
            public void attempt() {
                Notification.activityStart();
                ServicesLocator.getServiceContact().getContacts(event.getType(), event.getFilter(), event.getPageSize(),
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

    private Command getOfflineAction(final ContactsLoadEvent event) {
        Command offlineAction = new Command() {

            @Override
            public void execute() {
                String key = "contact" + event.getFilter() + "_" + event.getPageSize() + "_" + event.getStartIndex();
                List<DetailUserDTO> result = LocalStorageHelper.load(key, List.class);
                if (result == null) {
                    result = new ArrayList<DetailUserDTO>();
                }
                EventBus.getInstance().fireEvent(new ContactsLoadedEvent(result));
            }
        };

        return offlineAction;
    }

    private void storeInLocalStorage(final List<DetailUserDTO> result, final ContactsLoadEvent event) {
        String key = "contact" + event.getFilter() + "_" + event.getPageSize() + "_" +
                event.getStartIndex();
        LocalStorageHelper.store(key, List.class , result);
    }
}
