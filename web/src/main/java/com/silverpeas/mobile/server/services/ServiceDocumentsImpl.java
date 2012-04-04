package com.silverpeas.mobile.server.services;

import java.util.List;

import org.apache.log4j.Logger;

import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.DocumentsException;
import com.silverpeas.mobile.shared.services.ServiceDocuments;

/**
 * Service de gestion des GED.
 * @author svuillet
 */
public class ServiceDocumentsImpl extends AbstractAuthenticateService implements ServiceDocuments {

	private final static Logger LOGGER = Logger.getLogger(ServiceDocumentsImpl.class);
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * Retourne tous les topics de premier niveau d'un topic.
	 */
	@Override
	public List<TopicDTO> getTopics(String instanceId, String rootTopicId) throws DocumentsException, AuthenticationException {
		checkUserInSession();
		
		// TODO Auto-generated method stub
		return null;
	}	
}
