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

import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.event.touch.TouchCopy;

public class SwipeRecognizer implements TouchStartHandler, TouchMoveHandler, TouchEndHandler,
    TouchCancelHandler {


  private final HasAllTouchHandlers source;

  private final int minDistance;

  private final int threshold;

  private int touchCount;

  private enum State {
    INVALID, READY, FINDER_DOWN, FOUND_DIRECTION
  }

  private State state;

  private SwipeEvent.DIRECTION direction;

  private int lastDistance;

  private int x;

  private int y;

  /**
   * construct a swipe recognizer
   *
   * @param source the source to fire events on
   */
  public SwipeRecognizer(HasAllTouchHandlers source) {
    this(source, 40);
  }

  /**
   * construct a swipe recognizer
   *
   * @param source the source to fire events on
   * @param minDistance the minimum distance to cover before this counts as a swipe
   */
  public SwipeRecognizer(HasAllTouchHandlers source, int minDistance) {
    this(source, minDistance, 10);
  }

  /**
   * construct a swipe recognizer
   *
   * @param source the source to fire events on
   * @param minDistance the minimum distance to cover before this counts as a swipe
   * @param threshold the initial threshold before swipe start is fired
   */
  public SwipeRecognizer(HasAllTouchHandlers source, int minDistance, int threshold) {
    if (source == null)
      throw new IllegalArgumentException("source can not be null");

    if (minDistance <= 0 || minDistance < threshold) {
      throw new IllegalArgumentException("minDistance > 0 and minDistance > threshold");
    }

    if (threshold <= 0) {
      throw new IllegalArgumentException("threshold > 0");
    }

    this.source = source;
    this.minDistance = minDistance;
    this.threshold = threshold;
    this.touchCount = 0;
    state = State.READY;
    ((HasAllTouchHandlers) source).addTouchStartHandler(this);
    ((HasAllTouchHandlers) source).addTouchMoveHandler(this);
    ((HasAllTouchHandlers) source).addTouchEndHandler(this);
    ((HasAllTouchHandlers) source).addTouchCancelHandler(this);
  }

  @Override
  public void onTouchStart(TouchStartEvent event) {
    touchCount++;

    switch (state) {
      case INVALID:
        break;

      case READY:
        state = State.FINDER_DOWN;

        x = event.getTouches().get(0).getPageX();
        y = event.getTouches().get(0).getPageY();
        break;

      case FINDER_DOWN:
      default:
        state = State.INVALID;
        break;
    }
  }

  @Override
  public void onTouchMove(TouchMoveEvent event) {
    Touch touch = event.getTouches().get(0);

    switch (state) {
      case INVALID:

        break;
      case READY:
        // WTF?
        state = State.INVALID;
        break;
      case FINDER_DOWN:

        if (Math.abs(touch.getPageX() - x) >= threshold) {
          state = State.FOUND_DIRECTION;

          direction = touch.getPageX() - x > 0 ? SwipeEvent.DIRECTION.LEFT_TO_RIGHT : SwipeEvent
              .DIRECTION.RIGHT_TO_LEFT;

          SwipeStartEvent swipeStartEvent =
              new SwipeStartEvent(TouchCopy.copy(touch), touch.getPageX() - x, direction);

          getEventPropagator().fireEvent(swipeStartEvent);

        } else {
          if (Math.abs(touch.getPageY() - y) >= threshold) {
            state = State.FOUND_DIRECTION;

            direction = touch.getPageY() - y > 0 ? SwipeEvent.DIRECTION.TOP_TO_BOTTOM : SwipeEvent.DIRECTION.BOTTOM_TO_TOP;

            SwipeStartEvent swipeStartEvent =
                new SwipeStartEvent(TouchCopy.copy(touch), touch.getPageY() - y, direction);

            getEventPropagator().fireEvent(swipeStartEvent);

          }

        }
        break;

      case FOUND_DIRECTION:

        switch (direction) {
          case TOP_TO_BOTTOM:
          case BOTTOM_TO_TOP:
            lastDistance = Math.abs(touch.getPageY() - y);
            getEventPropagator().fireEvent(
                new SwipeMoveEvent(TouchCopy.copy(touch), lastDistance > minDistance,
                    lastDistance, direction));
            break;

          case LEFT_TO_RIGHT:
          case RIGHT_TO_LEFT:
            lastDistance = Math.abs(touch.getPageX() - x);
            getEventPropagator().fireEvent(
                new SwipeMoveEvent(TouchCopy.copy(touch), lastDistance > minDistance,
                    lastDistance, direction));

            break;

          default:
            break;
        }

        break;

      default:
        break;
    }
  }

  @Override
  public void onTouchEnd(TouchEndEvent event) {
    touchCount--;

    switch (state) {
      case FOUND_DIRECTION:
        getEventPropagator().fireEvent(new SwipeEndEvent(lastDistance > minDistance, lastDistance, direction));
        reset();
        break;
      default:
        reset();
        break;
    }
  }

  @Override
  public void onTouchCancel(TouchCancelEvent event) {
    touchCount--;
    if (touchCount <= 0) {
      reset();
    }
  }

  /**
   * the threshold before an event is fired (deadzone)
   *
   * @return the threshold in px
   */
  public int getThreshold() {
    return threshold;
  }

  /**
   * the distance that needs to be covered before counting as a swipe
   *
   * @return the distance in px
   */
  public int getMinDistance() {
    return minDistance;
  }

  private void reset() {
    state = State.READY;
    touchCount = 0;
  }

  private EventBus getEventPropagator() {
    return EventBus.getInstance();
  }
}
