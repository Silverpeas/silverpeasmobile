package com.silverpeas.mobile.client.components.base.events;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractScrollEvent extends GwtEvent<ScrollEventHandler>{

	public static Type<ScrollEventHandler> TYPE = new Type<ScrollEventHandler>();
	
	public AbstractScrollEvent(){
	}
	
	@Override
	public Type<ScrollEventHandler> getAssociatedType() {
		return TYPE;
	}
}
