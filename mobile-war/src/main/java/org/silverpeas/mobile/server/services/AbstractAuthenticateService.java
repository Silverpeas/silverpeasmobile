/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.silverpeas.core.SilverpeasException;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.AuthenticationService;
import org.silverpeas.core.security.authentication.AuthenticationServiceProvider;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.web.mvc.controller.MainSessionController;
import org.silverpeas.mobile.server.common.CommandCreateList;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public abstract class AbstractAuthenticateService extends RemoteServiceServlet {
  public static final String USER_ATTRIBUT_NAME = "user";
  public static final String MAINSESSIONCONTROLLER_ATTRIBUT_NAME = "SilverSessionController";

  protected void setUserInSession(UserDetail user) {
    getThreadLocalRequest().getSession().setAttribute(USER_ATTRIBUT_NAME, user);
  }

  protected UserDetail getUserInSession() {
    return (UserDetail) getThreadLocalRequest().getSession().getAttribute(USER_ATTRIBUT_NAME);
  }

  protected void checkUserInSession() throws AuthenticationException {
    if (getThreadLocalRequest().getSession().getAttribute(USER_ATTRIBUT_NAME) == null) {
      throw new AuthenticationException(AuthenticationError.NotAuthenticate);
    }
  }

  protected static SettingBundle getSettings() {
    return ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
  }

  protected void setMainsessioncontroller(String login, String password, String domainId) throws SilverpeasException {
    AuthenticationService authService = AuthenticationServiceProvider.getService();
    AuthenticationCredential credential = AuthenticationCredential.newWithAsLogin(login);
    String key = authService.authenticate(credential
        .withAsPassword(password)
        .withAsDomainId(domainId));
    MainSessionController mainSessionController = new MainSessionController(key, getThreadLocalRequest().getSession());
  }

  protected MainSessionController getMainSessionController() throws Exception {
    return (MainSessionController) getThreadLocalRequest().getSession().getAttribute(MAINSESSIONCONTROLLER_ATTRIBUT_NAME);
  }

  protected StreamingList createStreamingList(CommandCreateList command, int callNumber, int callSize, String cacheKey) throws Exception {
    List list;
    if (callNumber == 0) {
      list = command.execute();
      getThreadLocalRequest().getSession().setAttribute(cacheKey, list);
    } else {
      list = (ArrayList<BaseDTO>) getThreadLocalRequest().getSession().getAttribute(cacheKey);
    }


    int calledSize = 0;
    boolean moreElements = true;
    if (callNumber > 0) calledSize = callSize * callNumber;

    if ((calledSize + callSize) >= list.size()) {
      moreElements = false;
      callSize = list.size() - calledSize;
    }
    StreamingList<BaseDTO> streamingList = new StreamingList<BaseDTO>(list.subList(calledSize, calledSize + callSize), moreElements);
    if (!streamingList.hasMoreElement()) getThreadLocalRequest().getSession().removeAttribute(cacheKey);
    return streamingList;
  }
}
