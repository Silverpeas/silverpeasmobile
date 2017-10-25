package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
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
          RootPanel.getBodyElement().getStyle().setProperty("cursor", "wait");
          RootPanel.get().setHeight(Window.getClientHeight() + "px");
          RootPanel.getBodyElement().addClassName("csspinner traditional");
        }
        return false;
      }
    }, 1000);
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
    if ($wnd.Notification || $wnd.mozNotification || $wnd.webkitNotification || $wnd.msNotification) {
      Notification.requestPermission(function (permission) {});
      var instance = new Notification(title, {icon: icon, body: message});
    } else {
      $wnd.alert(message);
    }
  }-*/;
}
