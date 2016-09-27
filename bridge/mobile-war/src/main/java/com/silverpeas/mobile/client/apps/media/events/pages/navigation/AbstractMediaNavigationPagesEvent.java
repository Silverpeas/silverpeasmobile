package com.silverpeas.mobile.client.apps.media.events.pages.navigation;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractMediaNavigationPagesEvent extends GwtEvent<MediaNavigationPagesEventHandler> {

	public static Type<MediaNavigationPagesEventHandler> TYPE = new Type<MediaNavigationPagesEventHandler>();
	
	public AbstractMediaNavigationPagesEvent() {
	}
	
	@Override
	public Type<MediaNavigationPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
