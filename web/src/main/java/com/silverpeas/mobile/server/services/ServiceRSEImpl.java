package com.silverpeas.mobile.server.services;

import java.util.Date;
import java.util.List;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.silverpeas.socialnetwork.status.Status;
import com.silverpeas.socialnetwork.status.StatusService;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.UserDetail;

/**
 * Service de gestion du réseau social.
 * @author ethomas
 */
public class ServiceRSEImpl extends AbstractAuthenticateService implements ServiceRSE {

	private static final long serialVersionUID = 1L;
	private StatusDao statusDao = new StatusDao();
	private StatusService statusService = new StatusService();
	
	public String updateStatus(String textStatus) throws RSEexception, AuthenticationException {
		checkUserInSession();
		UserDetail user = getUserInSession();
		Status status = new Status(Integer.parseInt(user.getId()), new Date(), textStatus);		
		return statusService.changeStatusService(status);
	}

	public List<StatusDTO> getStatus(int step) throws RSEexception, AuthenticationException {
		checkUserInSession();
		UserDetail user = getUserInSession();
		try {			
		    return statusDao.getAllStatus(Integer.parseInt(user.getId()), step);
		} catch (Exception ex) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceRSEImpl.getAllStatus", "root.EX_NO_MESSAGE", ex);		    
		    throw new RSEexception(ex);
		}	
	}
}
