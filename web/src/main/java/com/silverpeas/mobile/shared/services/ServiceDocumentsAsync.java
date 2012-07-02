package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;

public interface ServiceDocumentsAsync {

	void getTopics(String instanceId, String rootTopicId, AsyncCallback<List<TopicDTO>> callback);

	void getPublications(String instanceId, String topicId, 	AsyncCallback<List<PublicationDTO>> callback);

	void getPublication(String pubId, AsyncCallback<PublicationDTO> callback);

}
