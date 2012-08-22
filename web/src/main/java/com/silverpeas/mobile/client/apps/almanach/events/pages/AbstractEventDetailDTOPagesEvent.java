package com.silverpeas.mobile.client.apps.almanach.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractEventDetailDTOPagesEvent extends GwtEvent<EventDetailDTOPagesEventHandler> {

	public static Type<EventDetailDTOPagesEventHandler> TYPE = new Type<EventDetailDTOPagesEventHandler>();
	
	public AbstractEventDetailDTOPagesEvent(){
	}
	
	public com.google.gwt.event.shared.GwtEvent.Type<EventDetailDTOPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
