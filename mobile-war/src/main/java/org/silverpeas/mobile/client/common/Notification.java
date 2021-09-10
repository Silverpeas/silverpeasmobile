/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


public class Notification {


  private static boolean activity = false;

  static {
    Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
      public void onPreviewNativeEvent(Event.NativePreviewEvent pEvent) {
        if (activity) {
          pEvent.cancel();
        }
      }
    });
  }

  public static void activityStart() {
    activity = true;

    Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
      @Override
      public boolean execute() {
        if (activity) {
          activityStartImmediately();
        }
        return false;
      }
    }, 1000);
  }

  public static void activityStartImmediately() {
    activity = true;
    RootPanel.getBodyElement().getStyle().setProperty("cursor", "wait");
    RootPanel.get().setHeight(Window.getClientHeight() + "px");
    RootPanel.getBodyElement().addClassName("csspinner traditional");
  }

  public static void activityStop() {
    activity = false;
    RootPanel.getBodyElement().getStyle().setProperty("cursor", "default");
    RootPanel.get().setHeight("");
    RootPanel.getBodyElement().removeClassName("csspinner traditional");
  }

  public static void alert(String message) {
    Window.alert(message);
  }

  public static void confirm(String message, Callback<Boolean, Boolean> callback, String buttonsLabels) {
    //TODO : implementation
  }

  public static void progressStart(String title, String message) {
    RootPanel.getBodyElement().getStyle().setProperty("cursor", "progress");
  }

  public static void progressStop() {
    RootPanel.getBodyElement().getStyle().setProperty("cursor", "default");
  }

  public static void progressValue(int value) {
    //TODO : implementation
  }

  public static native void notifyMessage(String icon, String title, String message) /*-{
      Notification.requestPermission(function (permission) {
        var instance = new Notification(title, {icon: icon, body: message});
      });
  }-*/;
}
