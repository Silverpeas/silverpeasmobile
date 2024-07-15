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

package org.silverpeas.mobile.server.servlets;

import org.apache.commons.io.IOUtils;
import org.silverpeas.kernel.logging.SilverLogger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SuppressWarnings("serial")
public class AppServiceWorkerServlet extends AbstractSilverpeasMobileServlet {


  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      checkUserInSession(request, response);
      response.setContentType("text/javascript");
      response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
      response.setHeader("Pragma", "no-cache");
      ServletContext context = getServletContext();
      PrintWriter out = response.getWriter();
      String jsonFireBaseConfig = getSettings().getString("push.notification.clientConfig","null");
      out.println("var firebaseConfig = " + jsonFireBaseConfig + ";");
      String version = request.getServletContext().getInitParameter("SILVERPEAS_VERSION");
      out.println("const OFFLINE_VERSION = '" + version + "';");

      String ressources = "";

      // statics ressources
      File resFolder = new File(context.getRealPath("/spmobile/"));
      for (File res : resFolder.listFiles()) {
        if (res.getName().contains(".") && !res.getName().endsWith(".txt") &&
            !res.getName().contains("devmode") && !res.getName().endsWith(".rpc") &&
            !res.getName().contains("chat.") && !res.getName().endsWith(".cache.js")) {
          ressources += "'" + res.getName() + "', ";
        }
      }

      // gwt ressources
      String lang = request.getHeader("Accept-Language");
      if (lang != null && !lang.isEmpty()) {
        lang = lang.substring(0, 2);
        if (!lang.equalsIgnoreCase("fr")) {
          lang = "en";
        }
      } else {
        lang = "en";
      }
      if (lang.equalsIgnoreCase("fr")) lang = "default";

      String userAgent = request.getHeader("User-Agent").toLowerCase();

      List<String> allLines = Files.readAllLines(Paths.get(context.getRealPath("/spmobile/compilation-mappings.txt")));
      allLines.removeIf(item -> item.isEmpty());
      allLines.removeIf(item -> item.equals("Devmode:devmode.js"));
      int i = 0;
      String f, l, u;
      while (i < allLines.size()) {
        f = allLines.get(i);
        l = allLines.get(i+1).replaceAll("locale ", "");
        u = allLines.get(i+2).replaceAll("user.agent ", "");
        if (u.equalsIgnoreCase("safari") && userAgent.contains(u) && lang.equals(l)) {
          ressources += "'" + f + "', ";
          break;
        } else if (u.equalsIgnoreCase("gecko1_8") && userAgent.contains("firefox") && lang.equals(l)) {
          ressources += "'" + f + "', ";
          break;
        }
        i = i + 3;
      }

      // dynamics ressources
      ressources += "'manifest.json', 'app-init.js'";

      if (!jsonFireBaseConfig.equals("null")) {
        ressources += ", '/silverpeas/spmobile/firebasejs/7.12.0/firebase-app.js', '/silverpeas/spmobile/firebasejs/7.12.0/firebase-messaging.js'";
      }

      out.println("const OFFLINE_URLS = [" + ressources + "];");

      InputStream template = context.getResourceAsStream("/WEB-INF/app-sw.template");
      out.println(IOUtils.toString(template, StandardCharsets.UTF_8));

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
  }
}
