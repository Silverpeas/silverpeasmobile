package com.silverpeas.mobile.client.apps.almanach.pages;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.widgets.HorizontalPanel;
import com.gwtmobile.ui.client.widgets.ScrollPanel;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadEventsEvent;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AbstractAlmanachPagesEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachPagesEventHandler;
import com.silverpeas.mobile.client.apps.almanach.events.pages.LoadEventDetailDTOEvent;
import com.silverpeas.mobile.client.apps.almanach.pages.widgets.AlmanachWidget;
import com.silverpeas.mobile.client.apps.almanach.resources.AlmanachMessages;
import com.silverpeas.mobile.client.apps.almanach.resources.AlmanachResources;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class AlmanachPage extends PageContent implements AlmanachPagesEventHandler, View {

	private static AlmanachPageUiBinder uiBinder = GWT.create(AlmanachPageUiBinder.class);
	@UiField
	ScrollPanel panelAlmanachWidget;

	@UiField(provided = true)
	protected AlmanachMessages msg = null;
	@UiField(provided = true)
	protected AlmanachResources ressources = null;
	@UiField
	protected Label instance, monthLabel;
	@UiField
	protected HorizontalPanel header;
	
	private ApplicationInstanceDTO currentInstance = null;
	
	private AlmanachLoadedEvent model;
	private Date currentDate = new Date();

	interface AlmanachPageUiBinder extends UiBinder<Widget, AlmanachPage> {
	}

	public AlmanachPage() {
		ressources = GWT.create(AlmanachResources.class);
		ressources.css().ensureInjected();
		msg = GWT.create(AlmanachMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractAlmanachPagesEvent.TYPE, this);
		monthLabel.setWidth(Window.getClientWidth() / 2 + "px");
		
		// load previous instance selection
		EventBus.getInstance().fireEvent(new AlmanachLoadSettingsEvent());
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractAlmanachPagesEvent.TYPE, this);
	}

	public void onAlmanachLoaded(AlmanachLoadedEvent event) {		
		this.model = event;
		this.currentInstance = event.getInstance();
		this.currentDate = new Date();
		displayAlmanach();
	}

	@Override
	public void onLoadEventDetailDTOLoaded(LoadEventDetailDTOEvent event) {
		EventDetailDTOPage eventDetailDTOPage = new EventDetailDTOPage(event.getListEventDetailDTO());
		goTo(eventDetailDTOPage);
	}

	@UiHandler("nextButton")
	void nextMouth(ClickEvent event) {		
		clickGesture(new Command() {
			
			@Override
			public void execute() {
				int m = (currentDate.getMonth()+1);
				if (m == 12){
					m = 0;
					currentDate.setYear(currentDate.getYear() + 1);
				}
				currentDate.setMonth(m);
				displayAlmanach();				
			}
		});		
	}

	@UiHandler("prevButton")
	void prevMouth(ClickEvent event) {
		clickGesture(new Command() {
			
			@Override
			public void execute() {
				int m = (currentDate.getMonth()-1);
				if (m < 0) {
					m = 11;
					currentDate.setYear(currentDate.getYear() - 1);
				}
				currentDate.setMonth(m);
				displayAlmanach();				
			}
		});		
	}

	@UiHandler("place")
	void browseAllAvailableECM(SelectionChangedEvent event) {
		NavigationApp app = new NavigationApp();
		app.setTypeApp(Apps.almanach.name());
		app.setTitle("Almanach Browser");
		app.start(this);
	}
	
	private void displayAlmanach() {
		panelAlmanachWidget.clear();
		AlmanachWidget almanachWidget = new AlmanachWidget(true, model.getListEventDetailDTO(), currentDate);
		panelAlmanachWidget.add(almanachWidget);
		displayPlace(model.getInstance());
	}

	public void displayPlace(ApplicationInstanceDTO appDTO) {
		instance.setText(appDTO.getLabel());		
		DateTimeFormat dtf = DateTimeFormat.getFormat("MMMM yyyy");
		monthLabel.setText(dtf.format(currentDate));
		header.setVisible(true);
	}

	@Override
	public void onLoadedSettings(AlmanachLoadedSettingsEvent event) {
		currentInstance = event.getInstance();					
		displayPlace(currentInstance);
		
		// Send message to controller for retreive event
		EventBus.getInstance().fireEvent(new AlmanachLoadEventsEvent(currentInstance));		
	}
}
