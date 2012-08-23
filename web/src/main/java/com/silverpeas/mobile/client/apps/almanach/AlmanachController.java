package com.silverpeas.mobile.client.apps.almanach;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AbstractAlmanachControllerEvent;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachControllerEventHandler;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;

public class AlmanachController implements Controller,
		AlmanachControllerEventHandler {

	public AlmanachController() {
		super();
		EventBus.getInstance().addHandler(AbstractAlmanachControllerEvent.TYPE,
				this);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(
				AbstractAlmanachControllerEvent.TYPE, this);
	}

	@Override
	public void loadAlmanach(AlmanachLoadEvent event) {
		ServicesLocator.serviceAlmanach.getAllRDV(event.getMonth(), new AsyncCallback<Collection<EventDetailDTO>>() {
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(Collection<EventDetailDTO> result) {
				EventBus.getInstance().fireEvent(new AlmanachLoadedEvent(result));
			}
		});
	}
}