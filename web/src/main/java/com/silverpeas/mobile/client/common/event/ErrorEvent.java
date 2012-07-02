package com.silverpeas.mobile.client.common.event;



public class ErrorEvent extends ExceptionEvent {

	public ErrorEvent(Throwable caught) {
		super(caught);
	}

	@Override
	protected void dispatch(ErrorEventHandler handler) {
		handler.onError(this);
	}

}
