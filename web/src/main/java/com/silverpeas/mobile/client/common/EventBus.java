package com.silverpeas.mobile.client.common;

import com.google.gwt.event.shared.HandlerManager;

/**
 * 
 * @author svuillet
 */
public class EventBus extends HandlerManager {

	private static EventBus instance = null;
	
	public static EventBus getInstance() {
		if (instance == null) {
			instance = new EventBus();
		}
		return instance;
	}
	
	private EventBus() {
		super(null);
	}
}