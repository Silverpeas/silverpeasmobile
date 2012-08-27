package com.silverpeas.mobile.client.apps.almanach;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AbstractAlmanachControllerEvent;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachControllerEventHandler;
<<<<<<< HEAD
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
=======
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;
<<<<<<< HEAD
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class AlmanachController implements Controller, AlmanachControllerEventHandler, NavigationEventHandler {

	public AlmanachController() {
		super();
		EventBus.getInstance().addHandler(AbstractAlmanachControllerEvent.TYPE,this);
		EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
=======

public class AlmanachController implements Controller,
		AlmanachControllerEventHandler {

	public AlmanachController() {
		super();
		EventBus.getInstance().addHandler(AbstractAlmanachControllerEvent.TYPE,
				this);
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
	}

	@Override
	public void stop() {
<<<<<<< HEAD
		EventBus.getInstance().removeHandler(AbstractAlmanachControllerEvent.TYPE, this);
		EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
	}

	public void loadAlmanach(final ApplicationInstanceDTO instance) {
		ServicesLocator.serviceAlmanach.getAlmanach(instance.getId(), new AsyncCallback<Collection<EventDetailDTO>>() {
=======
		EventBus.getInstance().removeHandler(
				AbstractAlmanachControllerEvent.TYPE, this);
	}

	@Override
	public void loadAlmanach(AlmanachLoadEvent event) {
		ServicesLocator.serviceAlmanach.getAllRDV(event.getMonth(), new AsyncCallback<Collection<EventDetailDTO>>() {
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(Collection<EventDetailDTO> result) {
<<<<<<< HEAD
				EventBus.getInstance().fireEvent(new AlmanachLoadedEvent(instance, result));
			}
		});
	}

	@Override
	public void loadSettings(AlmanachLoadSettingsEvent event) {
		
	}

	@Override
	public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
		loadAlmanach(event.getInstance());
	}
=======
				EventBus.getInstance().fireEvent(new AlmanachLoadedEvent(result));
			}
		});
	}
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
}