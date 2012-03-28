package com.silverpeas.mobile.shared.dto.gallery;

import java.io.Serializable;

public class PhotoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private boolean download;
	private String permalink;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDownload() {
		return download;
	}
	public void setDownload(boolean download) {
		this.download = download;
	}
	public String getPermalink() {
		return permalink;
	}
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}
	
}
