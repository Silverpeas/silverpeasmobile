package com.silverpeas.mobile.client.apps.status;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.client.apps.status.events.StatusEvents;
import com.silverpeas.mobile.client.apps.status.pages.StatusPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.components.base.events.apps.AppEvent;
import com.silverpeas.mobile.client.components.base.events.page.PageEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.StatusDTO;

public class StatusApp extends App {

    private static ApplicationMessages msg;

    public StatusApp(){
        super();
        msg = GWT.create(ApplicationMessages.class);
    }

    public void start(){
        setMainPage(new StatusPage());
        super.start();
    }

    @Override
    public void receiveEvent(AppEvent event) {
        if (event.getSender() instanceof StatusPage) {
            if (event.getName().equals(StatusEvents.POST.toString())) {
                final String postStatus = (String) event.getData();
                if (postStatus != null && postStatus.length() > 0) {
                    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<String>() {

                        @Override
                        public void attempt() {
                            ServicesLocator.getServiceRSE().updateStatus(postStatus, this);
                        }

                        public void onSuccess(String result) {
                            super.onSuccess(result);
                            StatusDTO status = new StatusDTO();
                            status.setCreationDate(new Date());
                            status.setDescription(result);
                            EventBus.getInstance().fireEvent(new PageEvent(StatusApp.this, StatusEvents.POSTED.toString(), status));
                        }
                    };
                    action.attempt();
                }
            }
        }
    }
}
