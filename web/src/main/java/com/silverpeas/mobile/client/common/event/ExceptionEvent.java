package com.silverpeas.mobile.client.common.event;

import com.google.gwt.event.shared.GwtEvent;

public abstract class ExceptionEvent extends GwtEvent<ErrorEventHandler> {

	public static Type<ErrorEventHandler> TYPE = new Type<ErrorEventHandler>();
	
	private final Throwable caught;
	
	public ExceptionEvent(Throwable caught) {
		this.caught = caught;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ErrorEventHandler> getAssociatedType() {
		return TYPE;
	}

	public Throwable getException() {
		return caught;
	}

}
