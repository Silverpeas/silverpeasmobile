package com.silverpeas.mobile.client.apps.gallery.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractGalleryPagesEvent extends GwtEvent<GalleryPagesEventHandler> {

	public static Type<GalleryPagesEventHandler> TYPE = new Type<GalleryPagesEventHandler>();
	
	public AbstractGalleryPagesEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GalleryPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
