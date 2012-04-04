package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;

public interface ServiceDocumentsAsync {

	void getTopics(String instanceId, String rootTopicId,
			AsyncCallback<List<TopicDTO>> callback);

}
