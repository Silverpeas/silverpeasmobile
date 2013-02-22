package com.silverpeas.mobile.client.apps.almanach;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AbstractAlmanachControllerEvent;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachControllerEventHandler;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class AlmanachController implements Controller, AlmanachControllerEventHandler, NavigationEventHandler {

	public AlmanachController() {
		super();
		EventBus.getInstance().addHandler(AbstractAlmanachControllerEvent.TYPE,this);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractAlmanachControllerEvent.TYPE, this);
		EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
	}

	public void loadAlmanach(final ApplicationInstanceDTO instance) {
		ServicesLocator.serviceAlmanach.getAlmanach(instance.getId(), new AsyncCallback<List<EventDetailDTO>>() {
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(List<EventDetailDTO> result) {
				EventBus.getInstance().fireEvent(new AlmanachLoadedEvent(instance, result));
			}
		});
	}

	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
		loadAlmanach(event.getInstance());
	}
}