package com.silverpeas.mobile.client.apps.media.events.pages.local;


public class DeletedLocalPictureEvent extends AbstractLocalPicturesPageEvent {

	private boolean noMorePicture;
		
	public DeletedLocalPictureEvent(boolean noMorePicture) {
		super();
		this.noMorePicture = noMorePicture;
	}

	@Override
	protected void dispatch(LocalPicturesPageEventHandler handler) {
		handler.onDeletedLocalPicture(this);
	}

	public boolean isNoMorePicture() {
		return noMorePicture;
	}
}
