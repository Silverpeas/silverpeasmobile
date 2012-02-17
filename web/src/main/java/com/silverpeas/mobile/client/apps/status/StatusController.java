package com.silverpeas.mobile.client.apps.status;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.status.events.controller.AbstractStatusControllerEvent;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusControllerEventHandler;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusLoadEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusLoadedEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostEvent;
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
	public void loadStatus(final StatusLoadEvent event) {
		ServicesLocator.serviceRSE.getStatus(event.getCurrentPage(), new AsyncCallback<List<StatusDTO>>(){
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));				
			}
			public void onSuccess(List<StatusDTO> result) {				
				EventBus.getInstance().fireEvent(new StatusLoadedEvent(result));
			}
		});
		
	}

	@Override
	public void postStatus(StatusPostEvent event) {
		if(event.getNewStatus() != null && event.getNewStatus().length()>0){
			ServicesLocator.serviceRSE.updateStatus(event.getNewStatus(), new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					EventBus.getInstance().fireEvent(new ErrorEvent(caught));
	            }
				public void onSuccess(String result) {
					EventBus.getInstance().fireEvent(new StatusPostEvent(result));
				}
			});
		}
	}
}
