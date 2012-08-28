package com.silverpeas.mobile.client.apps.almanach.pages;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
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
	private DateTimeFormat date = DateTimeFormat.getFormat("MM/dd/yyyy");
	
	interface EventDetailDTOPageUiBinder extends UiBinder<Widget, EventDetailDTOPage>{
	}
	
	public EventDetailDTOPage(List<EventDetailDTO> listEventDetailDTO){
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractEventDetailDTOPagesEvent.TYPE, this);
		accordionPanel = new AccordionPanel();
		int count = 0;
		Iterator<EventDetailDTO> i = listEventDetailDTO.iterator();

		while(i.hasNext()){
			EventDetailDTO eventDetailDTO = i.next();
			AccordionStack accordionStack = new AccordionStack();
			AccordionHeader accordionHeader = new AccordionHeader();
			AccordionContent accordionContent = new AccordionContent();
			Label nameEvent = new Label("Name : "+eventDetailDTO.get_name());
			accordionHeader.add(nameEvent);
			accordionStack.add(accordionHeader);
			Label startDateEvent = new Label("Date : "+date.format(eventDetailDTO.get_startDate(), null)+" "+eventDetailDTO.getStartHour());
			Label endDateEvent = new Label("Date : "+date.format(eventDetailDTO.get_endDate(), null)+" "+eventDetailDTO.getEndHour());
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
