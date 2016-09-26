package com.silverpeas.mobile.shared.dto.media;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class AlbumDTO extends BaseDTO implements Serializable, Comparable<AlbumDTO> {

	private static final long serialVersionUID = 1L;
	private String name;
  private int countMedia;

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

  public int getCountMedia() {
    return countMedia;
  }

  public void setCountMedia(final int countMedia) {
    this.countMedia = countMedia;
  }
}
