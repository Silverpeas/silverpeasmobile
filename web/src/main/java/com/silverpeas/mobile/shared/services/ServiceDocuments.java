package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.DocumentsException;

@RemoteServiceRelativePath("Documents")
public interface ServiceDocuments extends RemoteService {		
	public List<BaseDTO> getTopicsAndPublications(String instanceId, String rootTopicId) throws DocumentsException, AuthenticationException;
	public List<TopicDTO> getTopics(String instanceId, String rootTopicId) throws DocumentsException, AuthenticationException;
	public List<PublicationDTO> getPublications(String instanceId, String topicId) throws DocumentsException, AuthenticationException;
	public PublicationDTO getPublication(String pubId) throws DocumentsException, AuthenticationException;
}
