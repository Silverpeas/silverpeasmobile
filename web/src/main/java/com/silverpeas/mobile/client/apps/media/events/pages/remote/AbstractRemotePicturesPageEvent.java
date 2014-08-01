package com.silverpeas.mobile.client.apps.media.events.pages.remote;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractRemotePicturesPageEvent extends GwtEvent<RemotePicturesPageEventHandler> {

	public static Type<RemotePicturesPageEventHandler> TYPE = new Type<RemotePicturesPageEventHandler>();
	
	public AbstractRemotePicturesPageEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RemotePicturesPageEventHandler> getAssociatedType() {
		return TYPE;
	}
}
