package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

public class DetailUserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5388415881024885835L;
	private String id;
	private String lastName;
	private String eMail;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String LastName) {
		this.lastName = LastName;
	}
	
	public void seteMail(String EMail) {
	    eMail = EMail;
	}

	public String geteMail() {
	    return eMail;
	}
}
