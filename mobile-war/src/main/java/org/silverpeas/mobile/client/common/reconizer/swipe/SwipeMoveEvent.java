/*
 * Copyright (C) 2000 - 2025 Silverpeas
 *
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
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

import org.silverpeas.mobile.client.common.event.touch.TouchCopy;

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
