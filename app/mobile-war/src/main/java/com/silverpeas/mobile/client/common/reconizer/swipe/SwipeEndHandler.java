package com.silverpeas.mobile.client.common.reconizer.swipe;

import com.google.gwt.event.shared.EventHandler;

/**
 * A {@link SwipeEndHandler} receives {@link SwipeEndEvent}s
 *
 *
 */
public interface SwipeEndHandler extends EventHandler {
	/**
	 * Called when a {@link SwipeEndEvent} occurs
	 *
	 * @param event the event
	 */
	void onSwipeEnd(SwipeEndEvent event);
}
