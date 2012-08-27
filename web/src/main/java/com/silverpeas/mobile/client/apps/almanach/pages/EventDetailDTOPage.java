package com.silverpeas.mobile.client.apps.almanach.pages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.AccordionContent;
import com.gwtmobile.ui.client.widgets.AccordionHeader;
import com.gwtmobile.ui.client.widgets.AccordionPanel;
import com.gwtmobile.ui.client.widgets.AccordionStack;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AbstractEventDetailDTOPagesEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.EventDetailDTOPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;

public class EventDetailDTOPage extends Page implements EventDetailDTOPagesEventHandler, View{
	
	private static EventDetailDTOPageUiBinder uiBinder = GWT.create(EventDetailDTOPageUiBinder.class);
	@UiField AccordionPanel accordionPanel;
	private DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
	private DateFormat hour = new SimpleDateFormat("HH");
	
	interface EventDetailDTOPageUiBinder extends UiBinder<Widget, EventDetailDTOPage>{
	}
	
	public EventDetailDTOPage(List<EventDetailDTO> listEventDetailDTO){
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractEventDetailDTOPagesEvent.TYPE, this);
		Iterator<EventDetailDTO> i = listEventDetailDTO.iterator();
		int count=0;
		while(i.hasNext()){
			EventDetailDTO eventDetailDTO = i.next();
			AccordionStack accordionStack = new AccordionStack();
			AccordionHeader accordionHeader = new AccordionHeader();
			AccordionContent accordionContent = new AccordionContent();
			Label nameEvent = new Label("Name : "+eventDetailDTO.get_name());
			accordionHeader.add(nameEvent);
			accordionStack.add(accordionHeader);
			StringBuilder startDate = new StringBuilder(date.format(eventDetailDTO.get_startDate()));
			StringBuilder endDate = new StringBuilder(date.format(eventDetailDTO.get_endDate()));
			StringBuilder startHour = new StringBuilder(hour.format(eventDetailDTO.getStartHour()));
			StringBuilder endHour = new StringBuilder(hour.format(eventDetailDTO.getEndHour()));
			Label startDateEvent = new Label("Date : "+startDate+" "+startHour);
			Label endDateEvent = new Label("Date : "+endDate+" "+endHour);
			Label placeEvent = new Label("Place : "+eventDetailDTO.getPlace());
			accordionContent.add(startDateEvent);
			accordionContent.add(endDateEvent);
			accordionContent.add(placeEvent);
			accordionStack.add(accordionContent);
			accordionPanel.add(accordionStack);
			if(count==0){
				accordionStack.expand();
			}
			count++;
		}
	}
	
	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractEventDetailDTOPagesEvent.TYPE, this);
	}
}
