package com.silverpeas.mobile.client.apps.documents.events.pages.navigation;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractTopicsNavigationPagesEvent extends GwtEvent<TopicsNavigationPagesEventHandler> {

	public static Type<TopicsNavigationPagesEventHandler> TYPE = new Type<TopicsNavigationPagesEventHandler>();
	
	public AbstractTopicsNavigationPagesEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<TopicsNavigationPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
