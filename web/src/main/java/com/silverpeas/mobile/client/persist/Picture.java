package com.silverpeas.mobile.client.persist;

import com.gwtmobile.persistence.client.Persistable;

public interface Picture extends Persistable {
	public String getData();
	public void setData(String data);
	public String getName();
	public void setName(String name);	
}
