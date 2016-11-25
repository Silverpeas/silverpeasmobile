package com.silverpeas.mobile.client.components;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Frame;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.base.PageContent;

/**
 * @author: svu
 */
public class IframePage extends PageContent implements View {

  private Frame frame;
  public IframePage(String url) {
    frame = new Frame(url);
    frame.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
    initWidget(frame);
  }

  public void setSize(String width, String height) {
    frame.setSize(width, height);
  }
}
