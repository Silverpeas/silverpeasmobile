package com.silverpeas.mobile.server.services;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.silverpeas.authentication.AuthenticationCredential;
import org.silverpeas.authentication.AuthenticationService;

import com.silverpeas.admin.ejb.AdminBusiness;
import com.silverpeas.authentication.ejb.AuthenticationBm;
import com.silverpeas.mobile.shared.dto.DomainDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;
import com.silverpeas.mobile.shared.services.ServiceConnection;
import com.stratelia.webactiv.beans.admin.Domain;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;

/**
 * Service de gestion des connexions.
 * @author svuillet
 */
public class ServiceConnectionImpl extends AbstractAuthenticateService implements	ServiceConnection {

	private static final long serialVersionUID = 1L;

	private AdminBusiness adminBm;
	private AuthenticationBm authentication;
	private OrganizationController organizationController = new OrganizationController();

	public void login(String login, String password, String domainId)
			throws AuthenticationException {
		
		// vérification
		try {
			//String key = getAuthenticationBm().authenticate(login, password, domainId);
			
			AuthenticationService authenticator = new AuthenticationService();
		    AuthenticationCredential credential = AuthenticationCredential.newWithAsLogin(login)
		        .withAsPassword(password).withAsDomainId(domainId);
		    String key = authenticator.authenticate(credential);
			
			if (key == null || key.startsWith("Error_")) {								
				if (key.equals("Error_1")) {
					throw new AuthenticationException(AuthenticationError.BadCredential);
				} else if (key.equals("Error_2")) {
					throw new AuthenticationException(AuthenticationError.Host);
				} else if (key.equals("Error_5")) {
					throw new AuthenticationException(
							AuthenticationError.PwdNotAvailable);
				}				
			}			
		} catch (Exception e) {
			throw new AuthenticationException();
		}
		
		// récupération des informations de l'utilisateur
		String userId;
		try {
			userId = getUserId(login, domainId);
		} catch (Exception e) {
			throw new AuthenticationException(AuthenticationError.Host);
		}
		UserDetail user = getUserDetail(userId);
		setUserInSession(user);
	}
	
	public List<DomainDTO> getDomains() {
		Domain[] allDomains = organizationController.getAllDomains();
		ArrayList<DomainDTO> domains = new ArrayList<DomainDTO>();
		Mapper mapper = new DozerBeanMapper();
		for (int i = 0; i < allDomains.length; i++) {			
			domains.add(mapper.map(allDomains[i], DomainDTO.class)); 
		}
		return domains;
	}	

	private String getUserId(String login, String domainId) throws Exception {
		return getAdminBm().getUserIdByLoginAndDomain(login, domainId);
	}

	public UserDetail getUserDetail(String userId) {
		return organizationController.getUserDetail(userId);
	}

	private AdminBusiness getAdminBm() throws Exception {
		if (adminBm == null) {			 
			adminBm = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBusiness.class);			
		}
		return adminBm;
	}
	
	private AuthenticationBm getAuthenticationBm() throws Exception {
	    if (authentication == null) {	      
	        authentication = EJBUtilitaire.getEJBObjectRef(JNDINames.AUTHENTICATIONBM_EJBHOME, AuthenticationBm.class);
	      
	    }
	    return authentication;
	  }
}
