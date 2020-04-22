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

package org.silverpeas.mobile.server.servlets;

import org.apache.commons.io.IOUtils;
import org.silverpeas.core.util.logging.SilverLogger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("serial")
public class FireBaseServiceWorkerServlet extends AbstractSilverpeasMobileServlet {


  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      checkUserInSession(request, response);
      response.setContentType("text/javascript");
      response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
      response.setHeader("Pragma", "no-cache");
      PrintWriter out = response.getWriter();
      String jsonFireBaseConfig = getSettings().getString("push.notification.clientConfig","null");
      out.println("var firebaseConfig = " + jsonFireBaseConfig + ";");

      if (!jsonFireBaseConfig.equalsIgnoreCase("null")) {
        ServletContext context = getServletContext();
        InputStream template = context.getResourceAsStream("/WEB-INF/firebase-messaging-sw.template");
        out.println(IOUtils.toString(template, StandardCharsets.UTF_8));
      }

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
  }
}
