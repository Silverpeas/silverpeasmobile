package com.silverpeas.mobile.client.apps.documents.events.pages.navigation;


public class GedItemClickEvent extends AbstractGedNavigationPagesEvent {

	private Object gedItem;
	
	public GedItemClickEvent(Object gedItem) {
		super();
		this.gedItem = gedItem;
	}

	@Override
	protected void dispatch(GedNavigationPagesEventHandler handler) {
		handler.onGedItemClicked(this);
	}

	public Object getGedItem() {
		return gedItem;
	}
}
