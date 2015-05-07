package com.silverpeas.mobile.client.apps.status;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.status.events.app.AbstractStatusAppEvent;
import com.silverpeas.mobile.client.apps.status.events.app.StatusAppEventHandler;
import com.silverpeas.mobile.client.apps.status.events.app.StatusPostEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostedEvent;
import com.silverpeas.mobile.client.apps.status.pages.StatusPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.StatusDTO;

public class StatusApp extends App implements StatusAppEventHandler {

  private static ApplicationMessages msg;

  public StatusApp(){
    super();
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractStatusAppEvent.TYPE, this);
  }

  public void start(){
    setMainPage(new StatusPage());
    super.start();
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractStatusAppEvent.TYPE, this);
    super.stop();
  }

  @Override
  public void postStatus(StatusPostEvent event) {
    if(event.getPostStatus() != null && event.getPostStatus().length()>0){
      ServicesLocator.serviceRSE.updateStatus(event.getPostStatus(), new AsyncCallback<String>() {
        public void onFailure(Throwable caught) {
          if (OfflineHelper.needToGoOffine(caught)) {
            Notification.alert(msg.needToBeOnline());
          } else {
            EventBus.getInstance().fireEvent(new ErrorEvent(caught));
          }
        }
        public void onSuccess(String result) {
          StatusDTO status  = new StatusDTO();
          status.setCreationDate(new Date());
          status.setDescription(result);
          EventBus.getInstance().fireEvent(new StatusPostedEvent(status));
        }
      });
    }
  }
}
