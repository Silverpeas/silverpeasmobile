package com.silverpeas.mobile.client.common.reconizer.swipe;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A widget that implements this interface sources {@link SwipeStartEvent}s,
 * {@link SwipeMoveEvent}s and {@link SwipeEndEvent}s
 *
 *
 */
public interface HasSwipeHandlers {
	/**
	 * register for a {@link SwipeStartEvent}
	 *
	 * @param handler the handler to register
	 * @return the {@link HandlerRegistration}
	 */
	HandlerRegistration addSwipeStartHandler(SwipeStartHandler handler);

	/**
	 * register for a {@link SwipeMoveHandler}
	 *
	 * @param handler the handler to register
	 * @return the {@link HandlerRegistration}
	 */
	HandlerRegistration addSwipeMoveHandler(SwipeMoveHandler handler);

	/**
	 * register for a {@link SwipeEndHandler}
	 *
	 * @param handler the handler to register
	 * @return the {@link HandlerRegistration}
	 */
	HandlerRegistration addSwipeEndHandler(SwipeEndHandler handler);
}
