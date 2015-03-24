package com.silverpeas.mobile.client.common;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.VideoElement;

/**
 * @author: svu
 */
public class Html5Utils {

  public static native void setVideoFullScreen(Element element) /*-{
    if (element.requestFullscreen) {
      element.requestFullscreen();
    } else if (element.msRequestFullscreen) {
      element.msRequestFullscreen();
    } else if (element.mozRequestFullScreen) {
      element.mozRequestFullScreen();
    } else if (element.webkitRequestFullscreen) {
      element.webkitRequestFullscreen();
    }
  }-*/;

  public static native void setVideoNotFullScreen(Element element) /*-{
    if (element.exitFullscreen) {
      element.exitFullscreen();
    } else if (element.mozCancelFullScreen) {
      element.mozCancelFullScreen();
    } else if (element.webkitExitFullscreen) {
      element.webkitExitFullscreen();
    }
  }-*/;
}
