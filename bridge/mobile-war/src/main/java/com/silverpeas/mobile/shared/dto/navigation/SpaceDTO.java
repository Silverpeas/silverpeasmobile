package com.silverpeas.mobile.shared.dto.navigation;

import java.io.Serializable;


public class SpaceDTO extends SilverpeasObjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	boolean personal;
	public boolean isPersonal() {
		return personal;
	}
	public void setPersonal(boolean personal) {
		this.personal = personal;
	}
	
}
