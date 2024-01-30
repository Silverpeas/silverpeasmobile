/*
 * Copyright (C) 2000 - 2022 Silverpeas
 *
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.common;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.components.IframePage;

/**
 * @author svu
 */
public class PublicationContentHelper {

  public static void showContent(String pubId, String appId, String title) {

    // compute height available for content
    int heightAvailable = Window.getClientHeight() -
        (SpMobil.getMainPage().getHeaderHeight() + SpMobil.getMainPage().getFooterHeight());
    int widthAvailable = Window.getClientWidth();
    // display content
    final String url = UrlUtils.getServicesLocation() + "PublicationContent" + "?id=" + pubId + "&componentId=" + appId;


    try {
      new RequestBuilder(RequestBuilder.GET, url).sendRequest(null, new RequestCallback() {
        @Override
        public void onResponseReceived(final Request request, final Response response) {
          IframePage page = new IframePage("javascript:;", response.getText());
          page.setSize(widthAvailable + "px", heightAvailable + "px");
          page.setPageTitle(title);
          page.show();
        }

        @Override
        public void onError(final Request request, final Throwable throwable) {
          EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
        }
      });

    } catch (RequestException e) {
      EventBus.getInstance().fireEvent(new ErrorEvent(e));
    }
  }

  public static void showContent(String pubId, String appId, Element basement) {
    final String url = UrlUtils.getServicesLocation() + "PublicationContent" + "?id=" + pubId + "&componentId=" + appId;
    IFrameElement iframeC = Document.get().createIFrameElement();
    iframeC.setSrc(url);
    iframeC.setAttribute("onload", "javascript:(function(o){o.style.height=o.contentWindow.document.body.scrollHeight+'px';}(this));");
    String s = "border-style: none; width: 100%; overflow: hidden;";
    iframeC.setAttribute("style", s);
    basement.appendChild(iframeC);
  }

  private static native void write(Document doc, String newHTML) /*-{
    doc.open();
    doc.write(newHTML);
    doc.close();
  }-*/;

}
