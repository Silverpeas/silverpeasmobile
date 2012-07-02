package com.silverpeas.mobile.shared.dto.gallery;

import java.io.Serializable;

public class PhotoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private boolean download;
	private String dataPhoto;
	private String format;
	private String title;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDataPhoto() {
		return dataPhoto;
	}
	public void setDataPhoto(String dataPhoto) {
		this.dataPhoto = dataPhoto;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
}
