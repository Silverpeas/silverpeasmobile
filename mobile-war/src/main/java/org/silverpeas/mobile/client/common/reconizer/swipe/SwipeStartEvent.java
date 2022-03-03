/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

import org.silverpeas.mobile.client.common.event.touch.TouchCopy;

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
