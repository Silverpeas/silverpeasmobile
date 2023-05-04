package org.silverpeas.mobile.server.services;

import org.silverpeas.core.SilverpeasException;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.AuthenticationResponse;
import org.silverpeas.core.security.authentication.AuthenticationService;
import org.silverpeas.core.security.authentication.AuthenticationServiceProvider;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.web.mvc.controller.MainSessionController;
import org.silverpeas.core.web.rs.RESTWebService;
import org.silverpeas.mobile.server.common.CommandCreateList;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author svu
 */
public abstract class AbstractRestWebService extends RESTWebService {

  public static final String MAINSESSIONCONTROLLER_ATTRIBUT_NAME = "SilverSessionController";

  protected static SettingBundle getSettings() {
    return ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
  }

  protected MainSessionController getMainSessionController() {
    return (MainSessionController) getHttpRequest().getSession()
        .getAttribute(MAINSESSIONCONTROLLER_ATTRIBUT_NAME);
  }

  protected void setMainsessioncontroller(String login, String password, String domainId)
      throws SilverpeasException {
    AuthenticationService authService = AuthenticationServiceProvider.getService();
    AuthenticationCredential credential = AuthenticationCredential.newWithAsLogin(login);
    AuthenticationResponse response =
        authService.authenticate(credential.withAsPassword(password).withAsDomainId(domainId));
    String key;
    if (response == null) {
      key = null;
    } else if (response.getStatus().succeeded()) {
      key = response.getToken();
    } else {
      key = response.getStatus().getCode();
    }
    MainSessionController mainSessionController =
        new MainSessionController(key, getHttpRequest().getSession());
  }

  protected StreamingList createStreamingList(CommandCreateList command, int callNumber,
      int callSize, String cacheKey) throws Exception {
    List list;
    if (callNumber == 0) {
      list = command.execute();
      getHttpRequest().getSession().setAttribute(cacheKey, list);
    } else {
      list = (ArrayList<BaseDTO>) getHttpRequest().getSession().getAttribute(cacheKey);
    }


    int calledSize = 0;
    boolean moreElements = true;
    if (callNumber > 0) {
      calledSize = callSize * callNumber;
    }

    if ((calledSize + callSize) >= list.size()) {
      moreElements = false;
      callSize = list.size() - calledSize;
    }
    StreamingList<BaseDTO> streamingList =
        new StreamingList<BaseDTO>(list.subList(calledSize, calledSize + callSize), moreElements);
    if (!streamingList.getMoreElement()) {
      getHttpRequest().getSession().removeAttribute(cacheKey);
    }
    return streamingList;
  }

  protected StreamingList<?> makeStreamingList(int callNumber, String CACHE_NAME,
      HttpServletRequest request, Populator populator) {
    int callSize = 25;

    List<?> list = (List<?>) request.getSession().getAttribute(CACHE_NAME);
    if (list == null) {
      list = populator.execute();
      request.getSession().setAttribute(CACHE_NAME, list);
    }

    int calledSize = 0;
    boolean moreElements = true;
    if (callNumber > 0) {
      calledSize = callSize * callNumber;
    }

    if ((calledSize + callSize) >= list.size()) {
      moreElements = false;
      callSize = list.size() - calledSize;
    }

    List<?> sbList = list.subList(calledSize, calledSize + callSize);
    StreamingList<?> streamingList = new StreamingList<>(sbList, moreElements);
    if (callNumber == 0) {
      streamingList.setFirstCall(true);
    }
    if (!streamingList.getMoreElement()) {
      getHttpRequest().getSession().removeAttribute(CACHE_NAME);
    }

    return streamingList;
  }
}
