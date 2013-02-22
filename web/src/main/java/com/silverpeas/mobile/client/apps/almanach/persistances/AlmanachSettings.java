package com.silverpeas.mobile.client.apps.almanach.persistances;

import com.gwtmobile.persistence.client.Persistable;

public interface AlmanachSettings extends Persistable {	
	public String getSelectedInstanceId();
	public void setSelectedInstanceId(String instanceId);
	public String getSelectedInstanceLabel();
	public void setSelectedInstanceLabel(String instanceLabel);	
}
