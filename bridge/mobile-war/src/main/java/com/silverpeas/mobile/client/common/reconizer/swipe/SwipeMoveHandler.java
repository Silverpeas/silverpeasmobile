package com.silverpeas.mobile.client.common.reconizer.swipe;

import com.google.gwt.event.shared.EventHandler;

/**
 * A {@link SwipeMoveHandler} receives {@link SwipeMoveEvent}s
 *
 *
 */
public interface SwipeMoveHandler extends EventHandler {
	/**
	 * called when a {@link SwipeMoveEvent} occurs
	 *
	 * @param event the event
	 */
	void onSwipeMove(SwipeMoveEvent event);
}
