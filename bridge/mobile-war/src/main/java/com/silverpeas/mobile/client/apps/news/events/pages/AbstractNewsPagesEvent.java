package com.silverpeas.mobile.client.apps.news.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractNewsPagesEvent extends GwtEvent<NewsPagesEventHandler>{

	public static Type<NewsPagesEventHandler> TYPE = new Type<NewsPagesEventHandler>();

	public AbstractNewsPagesEvent(){
	}

	@Override
	public Type<NewsPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
