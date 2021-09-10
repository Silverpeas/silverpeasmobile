/*
 * Copyright (C) 2000 - 2021 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.common.reconizer.swipe;

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
