package com.silverpeas.mobile.shared.dto.navigation;

import com.silverpeas.mobile.shared.dto.RightDTO;

import java.io.Serializable;


public class ApplicationInstanceDTO extends SilverpeasObjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String type;
  private RightDTO rights;
	
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}

  public RightDTO getRights() {
    return rights;
  }

  public void setRights(final RightDTO rights) {
    this.rights = rights;
  }
}
