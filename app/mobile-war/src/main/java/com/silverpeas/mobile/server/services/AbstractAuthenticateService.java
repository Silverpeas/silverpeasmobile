package com.silverpeas.mobile.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.silverpeas.mobile.server.common.CommandCreateList;
import com.silverpeas.mobile.shared.StreamingList;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.web.mvc.controller.MainSessionController;

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

  protected MainSessionController getMainSessionController() throws Exception {
    MainSessionController mainSessionController = (MainSessionController) getThreadLocalRequest().getSession().getAttribute(MAINSESSIONCONTROLLER_ATTRIBUT_NAME);
    return mainSessionController;
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
