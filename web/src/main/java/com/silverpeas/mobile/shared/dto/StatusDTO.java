package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class StatusDTO implements Serializable, Comparable<StatusDTO> {

	private static final long serialVersionUID = 2921606984249560882L;
	
	 private int id;
	 private int userId;
	 private Date creationDate;
	 private String description = "";	 
	 
	public int compareTo(StatusDTO o) {		
		return getCreationDate().compareTo(o.getCreationDate()) * -1;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj) {		
		return ((StatusDTO) obj).getId() == id; 
	}
}
