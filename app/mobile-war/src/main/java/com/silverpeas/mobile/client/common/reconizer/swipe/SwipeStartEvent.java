package com.silverpeas.mobile.client.common.reconizer.swipe;

import com.silverpeas.mobile.client.common.event.touch.TouchCopy;

/**
 * A {@link SwipeStartEvent} is fired when the user moves his finger over a
 * certain amount on the display
 *
 */
public class SwipeStartEvent extends SwipeEvent<SwipeStartHandler> {

	private final static Type<SwipeStartHandler> TYPE = new Type<SwipeStartHandler>();
	private final int distance;
	private final TouchCopy touch;

	public static Type<SwipeStartHandler> getType() {
		return TYPE;
	}

	/**
	 * Construct a {@link SwipeStartEvent}
	 *
	 * @param distance the distance the finger already moved
	 * @param touch
	 * @param direction the direction of the finger
	 */
	public SwipeStartEvent(TouchCopy touch, int distance, SwipeEvent.DIRECTION direction) {
		super(direction);
		this.touch = touch;
		this.distance = distance;
	}

	@Override
	public Type<SwipeStartHandler> getAssociatedType() {
		return TYPE;
	}

	/**
	 * The distance the finger moved before the event occured
	 *
	 * @return the distance in px
	 */
	public int getDistance() {
		return distance;
	}

	@Override
	protected void dispatch(SwipeStartHandler handler) {
		handler.onSwipeStart(this);
	}

	public TouchCopy getTouch() {
		return touch;
	}
}
