package com.silverpeas.mobile.client.apps.almanach.pages;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
<<<<<<< HEAD
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AbstractAlmanachPagesEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachPagesEventHandler;
import com.silverpeas.mobile.client.apps.almanach.events.pages.LoadEventDetailDTOEvent;
import com.silverpeas.mobile.client.apps.almanach.pages.widgets.AlmanachWidget;
import com.silverpeas.mobile.client.apps.almanach.resources.AlmanachMessages;
import com.silverpeas.mobile.client.apps.almanach.resources.AlmanachResources;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
=======
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AbstractAlmanachPagesEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachPagesEventHandler;
import com.silverpeas.mobile.client.apps.almanach.pages.widgets.AlmanachWidget;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f

public class AlmanachPage extends Page implements AlmanachPagesEventHandler,
		View {

	private static AlmanachPageUiBinder uiBinder = GWT
			.create(AlmanachPageUiBinder.class);
	@UiField
<<<<<<< HEAD
	HorizontalPanel panelAlmanachWidget;
	
	@UiField(provided = true) protected AlmanachMessages msg = null;
	@UiField(provided = true) protected AlmanachResources ressources = null;
	@UiField Label instance, monthLabel;
	
	interface AlmanachPageUiBinder extends UiBinder<Widget, AlmanachPage> {
	}

	public AlmanachPage() {
		ressources = GWT.create(AlmanachResources.class);		
		ressources.css().ensureInjected();
		msg = GWT.create(AlmanachMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractAlmanachPagesEvent.TYPE, this);
=======
	HorizontalPanel l;

	interface AlmanachPageUiBinder extends UiBinder<Widget, AlmanachPage> {
	}

	@SuppressWarnings("deprecation")
	public AlmanachPage() {
		initWidget(uiBinder.createAndBindUi(this));
		Date date = new Date();
		EventBus.getInstance()
				.addHandler(AbstractAlmanachPagesEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new AlmanachLoadEvent(date.getMonth()));
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractAlmanachPagesEvent.TYPE,
				this);
	}

	public void onAlmanachLoaded(AlmanachLoadedEvent event) {
<<<<<<< HEAD
		panelAlmanachWidget.clear();
		AlmanachWidget almanachWidget = new AlmanachWidget(true, event.getListEventDetailDTO());
		panelAlmanachWidget.add(almanachWidget);
		displayPlace(event.getInstance());
	}

	@Override
	public void onLoadEventDetailDTOLoaded(LoadEventDetailDTOEvent event) {
		EventDetailDTOPage eventDetailDTOPage = new EventDetailDTOPage(event.getListEventDetailDTO());
		goTo(eventDetailDTOPage);
	}
	
	@UiHandler("place")
	void browseAllAvailableECM(SelectionChangedEvent event) {
		NavigationApp app = new NavigationApp();
		app.setTypeApp(Apps.almanach.name());
		app.setTitle("Almanach Browser");
		app.start(this);
	}
	
	@SuppressWarnings("deprecation")
	public void displayPlace(ApplicationInstanceDTO appDTO){
		instance.setText(appDTO.getLabel());
		Date date = new Date();
		monthLabel.setText(String.valueOf(date.getMonth()));
=======
		AlmanachWidget al = new AlmanachWidget(true, event.getListEventDetailDTO());
		l.add(al);
>>>>>>> 09b477a4f719202ea331d7ac57a295f981f9520f
	}
}
