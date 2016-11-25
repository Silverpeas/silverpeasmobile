package com.silverpeas.mobile.client.apps.notifications.pages.widgets.events;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractSelectionEvent extends GwtEvent<SelectionEventHandler>{

	public static Type<SelectionEventHandler> TYPE = new Type<SelectionEventHandler>();

	public AbstractSelectionEvent(){
	}

	@Override
	public Type<SelectionEventHandler> getAssociatedType() {
		return TYPE;
	}
}
