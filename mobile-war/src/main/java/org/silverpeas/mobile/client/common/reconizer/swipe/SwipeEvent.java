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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * base class for all swipte events
 *
 *
 * @param <H> Handler type of the event
 */
public abstract class SwipeEvent<H extends EventHandler> extends GwtEvent<H> {

	public enum DIRECTION {
		LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP;
	}

	private final DIRECTION direction;

	/**
	 * Construct a swipe event with a given direction
	 *
	 * @param direction the direction to use
	 */
	public SwipeEvent(DIRECTION direction) {
		this.direction = direction;
	}

	/**
	 * the direction of the event
	 *
	 * @return the direction
	 */
	public DIRECTION getDirection() {
		return direction;
	}
}
