package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.event.touch.TouchCopy;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.components.base.PageContent;

/**
 * @author: svu
 */
public class PhotoViewerPage extends PageContent implements View, TouchStartHandler,
    TouchEndHandler, TouchMoveHandler, TouchCancelHandler {

  interface PhotoViewerPageUiBinder extends UiBinder<HTMLPanel, PhotoViewerPage> {
  }

  @UiField HTMLPanel viewer;
  private Image img;
  private static PhotoViewerPageUiBinder uiBinder = GWT.create(PhotoViewerPageUiBinder.class);
  private int touchCount;
  private double ratio;
  private double distance = 0.0;
  private double zoomLevel = 1.0;
  private double initialZoomLevel = 1.0;
  private long initialTranslationX, initialTranslationY = 0;
  private long currentTranslationX, currentTranslationY = 0;
  private String translation = "";
  private int startx, starty = 0;

  private enum State {
    READY, INVALID, ONE_FINGER, TWO_FINGER;
  }

  private State state, previousState;

  private TouchCopy touchStart1;
  private TouchCopy touchStart2;

  public PhotoViewerPage() {
    initWidget(uiBinder.createAndBindUi(this));
    state = State.READY;
    previousState = state;
    Document.get().getElementsByTagName("body").getItem(0).getStyle().setOverflowY(Style.Overflow.HIDDEN);
  }

  public void setDataPhoto(final String dataPhoto) {
    this.img = new Image(dataPhoto);
    img.setStyleName("photo");
    img.addTouchStartHandler(this);
    img.addTouchEndHandler(this);
    img.addTouchMoveHandler(this);
    img.addTouchCancelHandler(this);
    viewer.add(img);
  }

  @Override
  public void onTouchCancel(final TouchCancelEvent event) {
    touchCount--;
    if (touchCount <= 0) {
      previousState = State.ONE_FINGER;
      reset();
    } else {
      if (state == State.TWO_FINGER) {
        previousState = State.TWO_FINGER;
        state = State.ONE_FINGER;
      } else {
        if (touchCount == 2) {
          state = State.TWO_FINGER;
          previousState = State.TWO_FINGER;
        }
      }
    }
  }

  @Override
  public void onTouchEnd(final TouchEndEvent event) {
    touchCount--;
    if (touchCount <= 0) {
      previousState = State.ONE_FINGER;
      reset();
    } else {
      if (state == State.TWO_FINGER) {
        previousState = State.TWO_FINGER;
        state = State.ONE_FINGER;
      } else {
        if (touchCount == 2) {
          state = State.TWO_FINGER;
          previousState = State.TWO_FINGER;
        }
      }
    }
  }

  @Override
  public void onTouchMove(final TouchMoveEvent event) {
    int left = img.getAbsoluteLeft();
    int top = img.getAbsoluteTop();
    int x1,y1,x2,y2;
    switch (state) {
      case ONE_FINGER:
        if (previousState.equals(State.TWO_FINGER) == false) {
          Touch touch = event.getTouches().get(0);
          x1 = touch.getPageX();
          y1 = touch.getPageY();
          if (startx != 0 && starty != 0) {
            currentTranslationX = Math.round((x1 - startx) / initialZoomLevel) + initialTranslationX;
            currentTranslationY = Math.round((y1 - starty) / initialZoomLevel) + initialTranslationY;
            translation = "translate(" + currentTranslationX + "px," + currentTranslationY + "px)";
            applyTransformation(initialZoomLevel, translation);
          } else {
            startx = x1;
            starty = y1;
          }
        }
        break;

      case TWO_FINGER:
        Touch touch1 = event.getTouches().get(0);
        Touch touch2 = event.getTouches().get(1);
        x1 = touch1.getPageX() - left;
        y1 = touch1.getPageY() - top;
        x2 = touch2.getPageX() - left;
        y2 = touch2.getPageY() - top;

        double newDistance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        calculateZoomLevel(newDistance);
        applyTransformation(zoomLevel, translation);
        if (distance == 0.0) {
          distance = newDistance;
        }
        break;

      default:
        state = State.INVALID;
        break;
    }
  }

  private void applyTransformation(double zoom, String translation) {
    img.getElement().setAttribute("style","-webkit-transform: scale(" + zoom + ") " + translation);
  }

  private double calculateZoomLevel(double newDistance) {
    ratio = (newDistance / distance);
    if (ratio >= 1) {
      zoomLevel = ratio + initialZoomLevel;
    } else {
      zoomLevel = initialZoomLevel - (1 / ratio);
    }
    if (zoomLevel < 1) zoomLevel = 1;
    return zoomLevel;
  };

  @Override
  public void onTouchStart(final TouchStartEvent event) {
    touchCount++;
    switch (state) {
      case READY:
        touchStart1 = TouchCopy.copy(event.getTouches().get(0));
        startx = touchStart1.getPageX();
        starty = touchStart1.getPageY();
        state = State.ONE_FINGER;
        break;
      case ONE_FINGER:
        touchStart2 = TouchCopy.copy(event.getTouches().get(1));
        distance = (int) Math.sqrt(Math.pow(touchStart1.getPageX() - touchStart2.getPageX(), 2) + Math.pow(touchStart1.getPageY() - touchStart2.getPageY(), 2));
        state = State.TWO_FINGER;
        break;

      default:
        state = State.INVALID;
        break;
    }
  }

  @Override
  public void stop() {
    Document.get().getElementsByTagName("body").getItem(0).getStyle().clearOverflowY();
    super.stop();
  }

  private void reset() {
    initialZoomLevel = zoomLevel;
    initialTranslationX = currentTranslationX;
    initialTranslationY = currentTranslationY;
    touchCount = 0;
    distance = 0.0;
    startx = 0;
    starty = 0;
    state = State.READY;
  }
}