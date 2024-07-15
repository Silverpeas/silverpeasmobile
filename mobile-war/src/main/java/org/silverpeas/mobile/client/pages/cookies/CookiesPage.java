/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

package org.silverpeas.mobile.client.pages.cookies;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;

import java.util.Date;


public class CookiesPage extends PageContent {

  interface CookiesPageUiBinder extends UiBinder<Widget, CookiesPage> {}

  private static CookiesPageUiBinder uiBinder = GWT.create(CookiesPageUiBinder.class);

  @UiField
  Anchor accept;

  @UiField
  HTML text;

  @UiField(provided = true)
  protected ApplicationMessages msg = null;

  public CookiesPage() {
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    SpMobil.getMainPage().hideFooter();
    text.setHTML(msg.cookiesInformation());
    Notification.activityStop();
  }

  @UiHandler("accept")
  void accept(ClickEvent e) {
    String cookie = Cookies.getCookie("accept_cookies");
    if (cookie == null || cookie.isEmpty()) {
      long duration = Long.parseLong(ResourcesManager.getParam("displayCookiesInformationFrequency"));
      final long DURATION = 1000 * 60 * 60 * 24 * duration;
      Date expires = new Date(System.currentTimeMillis() + DURATION);
      Cookies.setCookie("accept_cookies", "accepted", expires);
    }
    SpMobil.getInstance().loadIds(null);
  }

}
