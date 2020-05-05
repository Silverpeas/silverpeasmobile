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

package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.impl.StackTracePrintStream;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.event.ErrorEventHandler;
import org.silverpeas.mobile.client.common.event.ExceptionEvent;
import org.silverpeas.mobile.client.pages.error.ErrorPage;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;


public class ErrorManager implements ErrorEventHandler {

    private ApplicationMessages msg = GWT.create(ApplicationMessages.class);

    public void onError(ExceptionEvent event) {
      String message = getStackTrace(event);

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

  private String getStackTrace(final ExceptionEvent event) {
    final String message;
    StringBuilder msgEx = new StringBuilder();
    StackTracePrintStream ps = new StackTracePrintStream(msgEx);
    event.getException().printStackTrace(ps);
    ps.flush();

    message = msg.toString();
    return message;
  }

}
