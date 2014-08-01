package com.silverpeas.mobile.client.apps.media.events.pages.remote.viewer;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractPictureViewerPageEvent extends GwtEvent<PicturesViewerPageEventHandler> {

	public static Type<PicturesViewerPageEventHandler> TYPE = new Type<PicturesViewerPageEventHandler>();
	
	public AbstractPictureViewerPageEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PicturesViewerPageEventHandler> getAssociatedType() {
		return TYPE;
	}
}
