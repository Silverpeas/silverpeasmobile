package com.silverpeas.mobile.client.apps.media.events.pages.local;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractLocalPicturesPageEvent extends GwtEvent<LocalPicturesPageEventHandler> {

	public static Type<LocalPicturesPageEventHandler> TYPE = new Type<LocalPicturesPageEventHandler>();
	
	public AbstractLocalPicturesPageEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LocalPicturesPageEventHandler> getAssociatedType() {
		return TYPE;
	}
}
