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
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.core.web.mvc.controller.MainSessionController;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.mobile.server.helpers.AntivirusHelper;
import org.silverpeas.mobile.server.helpers.AntivirusResult;
import org.silverpeas.mobile.server.services.AbstractAuthenticateService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class AbstractSilverpeasMobileServlet extends HttpServlet {

    protected void checkUserInSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME) == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    protected UserDetail getUserInSession(HttpServletRequest request) {
        return (UserDetail) request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME);
    }

    protected MainSessionController getMainSessionController(HttpServletRequest request) throws Exception {
        MainSessionController mainSessionController = (MainSessionController) request.getSession().getAttribute(AbstractAuthenticateService.MAINSESSIONCONTROLLER_ATTRIBUT_NAME);
        return mainSessionController;
    }

    protected static SettingBundle getSettings() {
        return ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
    }

    protected boolean scanAntivirus(HttpServletResponse response, File file) throws IOException {
        AntivirusResult r = AntivirusHelper.scan(new FileInputStream(file));
        if (!r.isSafe()) {
            if (r.isError() && !getSettings().getBoolean("antivirus.allow.fileunverified", true)) {
                SilverLogger.getLogger(this).warn("Impossible to verify if the file is virus-free :" + file.getName());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "");
                return true;
            } else {
                SilverLogger.getLogger(this).error("File " + file.getName() + " infected with : " + r.getVirusName());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "");
                return true;
            }
        }
        return false;
    }
}
