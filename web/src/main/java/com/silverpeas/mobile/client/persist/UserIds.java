package com.silverpeas.mobile.client.persist;

import com.gwtmobile.persistence.client.Persistable;

public interface UserIds extends Persistable {
	public String getLogin();
	public void setLogin(String login);
	public String getPassword();
	public void setPassword(String password);
	public String getDomainId();
	public void setDomainId(String domainId);
}
