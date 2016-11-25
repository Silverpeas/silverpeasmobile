package com.silverpeas.mobile.client.common;

import com.google.gwt.dom.client.Element;

/**
 * @author: svu
 */
public class DOMUtils {
  public static native String getMarginHeight(Element element) /*-{
    var styles = window.getComputedStyle(element);
    var margin = parseInt(styles['marginTop']) + parseInt(styles['marginBottom']);
    return margin;
  }-*/;
}
