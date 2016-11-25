package com.silverpeas.mobile.client.apps.tasks.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractTasksPagesEvent extends GwtEvent<TasksPagesEventHandler>{

	public static Type<TasksPagesEventHandler> TYPE = new Type<TasksPagesEventHandler>();

	public AbstractTasksPagesEvent(){
	}

	@Override
	public Type<TasksPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
