package com.silverpeas.mobile.client.common.reconizer.swipe;

import com.silverpeas.mobile.client.common.event.touch.TouchCopy;

/**
 * A {@link SwipeMoveEvent} occurs when the user moves his finger over the
 * display
 *
 *
 */
public class SwipeMoveEvent extends SwipeEvent<SwipeMoveHandler> {

	private final static Type<SwipeMoveHandler> TYPE = new Type<SwipeMoveHandler>();
	private final boolean distanceReached;
	private final int distance;
	private final TouchCopy touch;

	public static Type<SwipeMoveHandler> getType() {
		return TYPE;
	}

	/**
	 * Construct a {@link SwipeMoveEvent}
	 *
	 * @param touch
	 *
	 * @param distanceReached is the minimum distance reached for this swipe
	 * @param distance the distance in px
	 * @param direction the direction of the swipe
	 */
	public SwipeMoveEvent(TouchCopy touch, boolean distanceReached, int distance, SwipeEvent.DIRECTION direction) {
		super(direction);
		this.touch = touch;
		this.distanceReached = distanceReached;
		this.distance = distance;
	}

	@Override
	public Type<SwipeMoveHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SwipeMoveHandler handler) {
		handler.onSwipeMove(this);
	}

	/**
	 * the distance of this swipe
	 *
	 * @return the distance of this swipe in px
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * is the minimum distance reached
	 *
	 * @return true if the minimum distance reached
	 */
	public boolean isDistanceReached() {
		return distanceReached;
	}

	public TouchCopy getTouch() {
		return touch;
	}
}
