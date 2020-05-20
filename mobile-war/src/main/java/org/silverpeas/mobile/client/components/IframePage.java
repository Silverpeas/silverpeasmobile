/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.client.components;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.common.mobil.Orientation;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.events.window.AbstractWindowEvent;
import org.silverpeas.mobile.client.components.base.events.window.OrientationChangeEvent;
import org.silverpeas.mobile.client.components.base.events.window.WindowEventHandler;

/**
 * @author: svu
 */
public class IframePage extends PageContent implements View, WindowEventHandler {

  private Frame frame;
  private String html;

  private void init() {
    frame.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
    frame.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
    frame.getElement().getStyle().setTop(SpMobil.getMainPage().getHeaderHeight(), Style.Unit.PX);
    setViewport();
    initWidget(frame);
    EventBus.getInstance().addHandler(AbstractWindowEvent.TYPE, this);
  }

  public IframePage(String url) {
    frame = new Frame(url);
    init();
  }

  public IframePage(String url, String html) {
    frame = new Frame(url);
    init();
    this.html = html;

    frame.addLoadHandler(new LoadHandler() {
      @Override
      public void onLoad(final LoadEvent loadEvent) {
        IFrameElement el = IFrameElement.as(frame.getElement());
        write(el.getContentDocument(), html);
      }
    });
  }

  private static native void write(Document doc, String newHTML) /*-{
    doc.open();
    doc.write(newHTML);
    doc.close();
  }-*/;

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
