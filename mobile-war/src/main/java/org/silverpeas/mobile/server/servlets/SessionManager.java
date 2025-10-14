/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.server.servlets;

import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.silverstatistics.volume.service.SilverStatisticsManager;
import org.silverpeas.mobile.server.helpers.MediaHelper;
import org.silverpeas.mobile.server.services.AbstractAuthenticateService;

import javax.inject.Inject;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

/**
 * @author: svu
 */
public class SessionManager implements HttpSessionListener {

  @Inject
  private SilverStatisticsManager myStatisticsManager = null;

  @Override
  public void sessionCreated(final HttpSessionEvent httpSessionEvent) {

  }

  @Override
  public void sessionDestroyed(final HttpSessionEvent httpSessionEvent) {

    Boolean mob = (Boolean) httpSessionEvent.getSession().getAttribute("isMobile");
    if (mob != null && !mob) {
      MediaHelper.cleanTemporaryFiles(httpSessionEvent.getSession().getId());

      UserDetail user = (UserDetail) httpSessionEvent.getSession()
          .getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME);
      if (user != null) {
        Date now = new Date();
        long duration = now.getTime() - httpSessionEvent.getSession().getCreationTime();
        myStatisticsManager.addStatConnection(user.getId(), now, 1, duration);
      }
    }
  }
}
