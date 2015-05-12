package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.event.ErrorEventHandler;
import com.silverpeas.mobile.client.common.event.ExceptionEvent;
import com.silverpeas.mobile.client.pages.error.ErrorPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.realityforge.gwt.appcache.client.ApplicationCache;

public class ErrorManager implements ErrorEventHandler {

    private ApplicationMessages msg = GWT.create(ApplicationMessages.class);

    public void onError(ExceptionEvent event) {
        String message = msg.systemError();

        message = event.getException().getMessage();

        Notification.activityStop();
        Notification.progressStop();

        // Affichage du message
        ErrorPage page = new ErrorPage();
        page.setText(message);
        SpMobil.showFullScreen(page, false, "ui-panel-wrapper", "error");
    }

}
