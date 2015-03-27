package com.silverpeas.mobile.client.common.reconizer.swipe;

import com.google.gwt.event.shared.GwtEvent;

/**
 * A {@link SwipeEndEvent} occurs when the user lifts his finger of the display
 *
 *
 */
public class SwipeEndEvent extends SwipeEvent<SwipeEndHandler> {

	private final static Type<SwipeEndHandler> TYPE = new Type<SwipeEndHandler>();
	private final boolean distanceReached;
	private final int distance;

	public static Type<SwipeEndHandler> getType() {
		return TYPE;
	}

	/**
	 * Construct a swipe end event
	 *
	 * @param distanceReached was the minumum distance reached
	 * @param distance the distance that was covered by the finger
	 * @param direction the direction of the swipe
	 */
	public SwipeEndEvent(boolean distanceReached, int distance, SwipeEvent.DIRECTION direction) {
		super(direction);
		this.distanceReached = distanceReached;
		this.distance = distance;
	}

	@Override
	public Type<SwipeEndHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SwipeEndHandler handler) {
		handler.onSwipeEnd(this);
	}

	/**
	 * the distance the finger has covered in px
	 *
	 * @return the distance in px
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * is the minimum distance reached by this swipe
	 *
	 * @return true if minimum distance was reached
	 */
	public boolean isDistanceReached() {
		return distanceReached;
	}
}
