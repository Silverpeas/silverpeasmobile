package com.silverpeas.mobile.client.common.reconizer.swipe;

import com.google.gwt.event.shared.EventHandler;

/**
 * A {@link SwipeStartHandler} receives {@link SwipeStartEvent}s
 *
 *
 */
public interface SwipeStartHandler extends EventHandler {

	/**
	 * called when a {@link SwipeStartEvent} occurs
	 *
	 * @param event the event
	 */
	void onSwipeStart(SwipeStartEvent event);
}
