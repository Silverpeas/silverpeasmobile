package com.silverpeas.mobile.client.apps.contacts.events.pages;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractContactsDetailPagesEvent extends GwtEvent<ContactDetailPagesEventHandler>{

	public static Type<ContactDetailPagesEventHandler> TYPE = new Type<ContactDetailPagesEventHandler>();
	
	public AbstractContactsDetailPagesEvent(){
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ContactDetailPagesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
