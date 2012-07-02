package com.silverpeas.mobile.client.apps.dashboard;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.dashboard.events.controller.AbstractDashboardControllerEvent;
import com.silverpeas.mobile.client.apps.dashboard.events.controller.DashboardControllerEventHandler;
import com.silverpeas.mobile.client.apps.dashboard.events.controller.DashboardLoadEvent;
import com.silverpeas.mobile.client.apps.dashboard.events.pages.DashboardLoadedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.SocialInformationDTO;

public class DashboardController implements Controller, DashboardControllerEventHandler{

	public DashboardController(){
		super();
		EventBus.getInstance().addHandler(AbstractDashboardControllerEvent.TYPE, this);
	}
	
	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractDashboardControllerEvent.TYPE, this);
	}

	@Override
	public void getSocialInformations(DashboardLoadEvent event) {
		ServicesLocator.serviceDashboard.getAll(event.getReinitailisationPage(), event.getSocialInformationType(), new AsyncCallback<List<SocialInformationDTO>>(){
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(List<SocialInformationDTO> result) {
				EventBus.getInstance().fireEvent(new DashboardLoadedEvent(result));
			}
		});
	}
}
