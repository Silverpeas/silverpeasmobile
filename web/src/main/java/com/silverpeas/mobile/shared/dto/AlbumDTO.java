package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

public class AlbumDTO implements Serializable, Comparable<AlbumDTO> {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int compareTo(AlbumDTO o) {
		return name.compareTo(o.getName());
	}

}
