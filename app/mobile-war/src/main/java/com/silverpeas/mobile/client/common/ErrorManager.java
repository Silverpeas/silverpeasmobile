package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.event.ErrorEventHandler;
import com.silverpeas.mobile.client.common.event.ExceptionEvent;
import com.silverpeas.mobile.client.pages.error.ErrorPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;


public class ErrorManager implements ErrorEventHandler {

    private ApplicationMessages msg = GWT.create(ApplicationMessages.class);

    public void onError(ExceptionEvent event) {
        String message = event.getException().getMessage();
        if (event.getException() instanceof AuthenticationException) {
          AuthenticationException ae = (AuthenticationException) event.getException();
          if (ae.getError() == null) {
            message = msg.badAuthentification();
          } else if (ae.getError().name().equals(AuthenticationException.AuthenticationError.LoginNotAvailable.name())) {
            message = msg.badLoginOrPassword();
          } else if (ae.getError().name().equals(AuthenticationException.AuthenticationError.PwdNotAvailable.name())) {
            message = msg.badPassword();
          }
        }
        if (message == null || message.isEmpty()) {
          message = msg.systemError();
        }

        Notification.activityStop();
        Notification.progressStop();
        Html5Utils.vibrate();

        // Affichage du message
        ErrorPage page = new ErrorPage();
        page.setText(message);
        SpMobil.showFullScreen(page, false, "ui-panel-wrapper", "error");
    }

}
