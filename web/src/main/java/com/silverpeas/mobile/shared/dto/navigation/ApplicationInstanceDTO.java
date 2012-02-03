package com.silverpeas.mobile.shared.dto.navigation;

import java.io.Serializable;


public class ApplicationInstanceDTO extends SilverpeasObjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String type;
	
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
}
