package com.silverpeas.mobile.shared.dto.navigation;

import java.io.Serializable;


public class SpaceDTO extends SilverpeasObjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	boolean personal;
	int orberNum;

	public boolean isPersonal() {
		return personal;
	}
	public void setPersonal(boolean personal) {
		this.personal = personal;
	}

	@Override
	public int compareTo(SilverpeasObjectDTO o) {
		if (o instanceof SpaceDTO) {
			if (isPersonal()) {
				return 100;
			} else {
				return new Integer(orberNum).compareTo(((SpaceDTO) o).orberNum);
			}
		} else {
			return super.compareTo(o);
		}
	}

	public void setOrberNum(int orberNum) {
		this.orberNum = orberNum;
	}
}
