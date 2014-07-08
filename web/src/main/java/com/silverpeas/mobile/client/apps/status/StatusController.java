package com.silverpeas.mobile.client.apps.status;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.status.events.controller.AbstractStatusControllerEvent;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusControllerEventHandler;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusPostEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.StatusDTO;

public class StatusController implements Controller, StatusControllerEventHandler{
	
	public StatusController(){
		super();
		EventBus.getInstance().addHandler(AbstractStatusControllerEvent.TYPE, this);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractStatusControllerEvent.TYPE, this);
	}

	@Override
	public void postStatus(StatusPostEvent event) {
		if(event.getPostStatus() != null && event.getPostStatus().length()>0){
			ServicesLocator.serviceRSE.updateStatus(event.getPostStatus(), new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					EventBus.getInstance().fireEvent(new ErrorEvent(caught));
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
