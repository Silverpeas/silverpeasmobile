package com.silverpeas.mobile.client.apps.status.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractStatusPostPagesEvent extends GwtEvent<StatusPostPagesEventHandler>{
	
	public static Type<StatusPostPagesEventHandler> TYPE = new Type<StatusPostPagesEventHandler>();
	
	public AbstractStatusPostPagesEvent(){
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StatusPostPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
