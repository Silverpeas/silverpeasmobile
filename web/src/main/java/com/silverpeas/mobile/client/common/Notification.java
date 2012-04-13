/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.client.common;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtmobile.phonegap.client.Notification.Callback;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;

public class Notification {

  public static void activityStart() {
    if (MobilUtils.isPhoneGap()) {
      com.gwtmobile.phonegap.client.Notification.activityStart();
    } else {
      DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
    }
  }

  public static void activityStop() {
    if (MobilUtils.isPhoneGap()) {
      com.gwtmobile.phonegap.client.Notification.activityStop();
    } else {
      DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
    }
  }

  public static void alert(String message, Callback callback, String title, String buttonLabel) {
    if (MobilUtils.isPhoneGap()) {
      com.gwtmobile.phonegap.client.Notification.alert(message, callback, title, buttonLabel);
    } else {
      Window.alert(message);
    }
  }

  public static void progressStart(String title, String message) {
    if (MobilUtils.isPhoneGap()) {
      com.gwtmobile.phonegap.client.Notification.progressStart(title, message);
    } else {
      DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "progress");
    }
  }

  public static void progressStop() {
    if (MobilUtils.isPhoneGap()) {
      com.gwtmobile.phonegap.client.Notification.progressStop();
    } else {
      DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
    }
  }

  public static void progressValue(int value) {
    if (MobilUtils.isPhoneGap()) {
      com.gwtmobile.phonegap.client.Notification.progressValue(value);
    }
  }
}
