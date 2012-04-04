package com.silverpeas.mobile.client.apps.documents.persistances;

import com.gwtmobile.persistence.client.Persistable;

public interface DocumentsSettings extends Persistable {
	public String getSelectedTopicId();
	public void setSelectedTopicId(String topicId);
	public String getSelectedInstanceId();
	public void setSelectedInstanceId(String instanceId);
	public String getSelectedInstanceLabel();
	public void setSelectedInstanceLabel(String instanceLabel);
}
