package com.silverpeas.mobile.client.common.event.touch;

/**
 * @author: svu
 */
import com.google.gwt.dom.client.Touch;

public class TouchCopy {

  public static TouchCopy copy(Touch touch) {
    return new TouchCopy(touch.getPageX(), touch.getPageY(), touch.getIdentifier());
  }

  private final int x;
  private final int y;
  private final int id;

  public TouchCopy(int x, int y, int id) {
    this.x = x;
    this.y = y;
    this.id = id;
  }

  public int getPageX() {
    return x;
  }

  public int getPageY() {
    return y;
  }

  public int getIdentifier() {
    return id;
  }
}
