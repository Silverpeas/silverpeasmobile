package org.silverpeas.mobile.server.services;

import jakarta.servlet.http.HttpServletRequest;
import org.silverpeas.kernel.SilverpeasException;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.AuthenticationService;
import org.silverpeas.core.security.authentication.AuthenticationServiceProvider;
import org.silverpeas.core.security.session.SessionInfo;
import org.silverpeas.core.security.session.SessionManagement;
import org.silverpeas.core.security.session.SessionManagementProvider;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.core.web.mvc.controller.MainSessionController;
import org.silverpeas.core.web.rs.RESTWebService;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;
import org.silverpeas.mobile.server.common.CommandCreateList;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
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

  protected void authenticate(String login, String password, String domainId)
      throws SilverpeasException {
    AuthenticationService authService = AuthenticationServiceProvider.getService();
    AuthenticationCredential credential = AuthenticationCredential.newWithAsLogin(login);
    authService.authenticate(credential.withAsPassword(password).withAsDomainId(domainId));
  }

  protected void initSilverpeasSession(HttpServletRequest request) {
    MainSessionController controller = (MainSessionController) request.getSession()
            .getAttribute(MainSessionController.MAIN_SESSION_CONTROLLER_ATT);
    if (controller == null) {
      SessionManagement sessionManagement = SessionManagementProvider.getSessionManagement();
      SessionInfo sessionInfo = sessionManagement.validateSession(request.getSession().getId());
      if (sessionInfo.getSessionId() == null) {
        sessionInfo = sessionManagement.openSession(getUser(), request);
      }

      try {
        controller = new MainSessionController(sessionInfo, request.getSession());
      } catch (SilverpeasException e) {
        SilverLogger.getLogger(this).error(e);
      }
      request.getSession()
              .setAttribute(MainSessionController.MAIN_SESSION_CONTROLLER_ATT, controller);
    }

    GraphicElementFactory gef = (GraphicElementFactory) request.getSession()
            .getAttribute(GraphicElementFactory.GE_FACTORY_SESSION_ATT);
    if (gef == null && controller != null) {
      gef = new GraphicElementFactory(controller);
      request.getSession().setAttribute(GraphicElementFactory.GE_FACTORY_SESSION_ATT, gef);
    }
  }

  @SuppressWarnings("unchecked")
  protected StreamingList<BaseDTO> createStreamingList(CommandCreateList command, int callNumber,
      int callSize, String cacheKey) throws Exception {
    List<BaseDTO> list;
    if (callNumber == 0) {
      list = command.execute();
      getHttpRequest().getSession().setAttribute(cacheKey, list);
    } else {
      list = (List<BaseDTO>) getHttpRequest().getSession().getAttribute(cacheKey);
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
        new StreamingList<>(list.subList(calledSize, calledSize + callSize), moreElements);
    if (!streamingList.getMoreElement()) {
      getHttpRequest().getSession().removeAttribute(cacheKey);
    }
    return streamingList;
  }

  protected StreamingList<?> makeStreamingList(int callNumber, String CACHE_NAME,
      HttpServletRequest request, Populator populator) {
    int callSize = 25;

    if (callNumber==0) request.getSession().removeAttribute(CACHE_NAME);
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
