package com.silverpeas.mobile.client.common;

import com.google.gwt.user.client.Window;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.event.ErrorEventHandler;
import com.silverpeas.mobile.client.common.event.ExceptionEvent;
import com.silverpeas.mobile.client.pages.error.ErrorPage;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

public class ErrorManager implements ErrorEventHandler {

  public void onError(ExceptionEvent event) {
    String message = "Erreur système";

    if (event.getException() instanceof AuthenticationException) {
      Window.Location.reload();
      return;
    } else {
      message = event.getException().getMessage();
    }

    Notification.activityStop();
    Notification.progressStop();

    // Affichage du message
    ErrorPage page = new ErrorPage();
    page.setText(message);
    SpMobil.showFullScreen(page, false, "ui-panel-wrapper", "error");
  }

}
