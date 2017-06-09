package com.silverpeas.mobile.client.components;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.mobil.Orientation;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.components.base.events.window.AbstractWindowEvent;
import com.silverpeas.mobile.client.components.base.events.window.OrientationChangeEvent;
import com.silverpeas.mobile.client.components.base.events.window.WindowEventHandler;

/**
 * @author: svu
 */
public class IframePage extends PageContent implements View, WindowEventHandler {

  private Frame frame;
  public IframePage(String url) {
    frame = new Frame(url);
    frame.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
    frame.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
    frame.getElement().getStyle().setTop(SpMobil.getMainPage().getHeaderHeight(), Style.Unit.PX);

    setViewport();
    initWidget(frame);
    EventBus.getInstance().addHandler(AbstractWindowEvent.TYPE, this);
  }

  public void setSize(String width, String height) {
    frame.setSize(width, height);
  }

  @Override
  protected void setViewport() {
    NodeList<Element> metas = Document.get().getHead().getElementsByTagName("meta");
    for (int i = 0; i < metas.getLength(); i++) {
      if (metas.getItem(i).getAttribute("name").equals("viewport")) {
        metas.getItem(i).setAttribute("content","width=device-width, target-densitydpi=device-dpi, initial-scale=1.0, maximum-scale=5.0, user-scalable=1");
      }
    }
  }

  @Override
  public void back() {
    super.setViewport();
    super.back();
    EventBus.getInstance().removeHandler(AbstractWindowEvent.TYPE, this);
  }

  @Override
  public void onOrientationChange(final OrientationChangeEvent event) {
    int heightAvailable = Window.getClientHeight() - (SpMobil.getMainPage().getHeaderHeight() + SpMobil.getMainPage().getFooterHeight());
    int widthAvailable = Window.getClientWidth();
    setSize(widthAvailable + "px", heightAvailable + "px");

    if (event.getOrientation().equals(Orientation.Landscape)) {

    } else if (event.getOrientation().equals(Orientation.Portrait)) {

    }
  }
}
