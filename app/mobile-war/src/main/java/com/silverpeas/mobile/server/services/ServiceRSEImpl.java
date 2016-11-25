package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.socialnetwork.status.Status;
import org.silverpeas.core.socialnetwork.status.StatusService;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.util.logging.SilverLogger;

import java.util.Date;
import java.util.List;

/**
 * Service de gestion du réseau social.
 * @author ethomas
 */
public class ServiceRSEImpl extends AbstractAuthenticateService implements ServiceRSE {

  private static final long serialVersionUID = 1L;
  private StatusDao statusDao = new StatusDao();

  @Override
  public String updateStatus(String textStatus) throws RSEexception, AuthenticationException {
    checkUserInSession();
    UserDetail user = getUserInSession();
    Status status = new Status(Integer.parseInt(user.getId()), new Date(), textStatus);
    return getStatusService().changeStatusService(status);
  }

  @Override
  public List<StatusDTO> getStatus(int step) throws RSEexception, AuthenticationException {
    checkUserInSession();
    UserDetail user = getUserInSession();
    try {
      return statusDao.getAllStatus(Integer.parseInt(user.getId()), step);
    } catch (Exception ex) {
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceRSEImpl.getAllStatus", ex);
      throw new RSEexception(ex);
    }
  }

  @Override
  public StatusDTO getStatus() throws RSEexception, AuthenticationException {
    checkUserInSession();
    UserDetail user = getUserInSession();
    try {
      return statusDao.getStatus(Integer.parseInt(user.getId()));
    } catch (Exception ex) {
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceRSEImpl.getStatus", ex);
      throw new RSEexception(ex);
    }
  }

  private StatusService getStatusService() {
    return ServiceProvider.getService(StatusService.class);
  }
}
