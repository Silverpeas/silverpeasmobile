package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

/**
 * @author: svu
 */
public abstract class BaseDTO implements Serializable {

	private static final long serialVersionUID = 8186851689918190659L;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	private String id;

}
