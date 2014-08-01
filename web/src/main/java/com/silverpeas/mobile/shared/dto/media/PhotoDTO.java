package com.silverpeas.mobile.shared.dto.media;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class PhotoDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean download;
	private String dataPhoto;
	private String format;
	private String title;

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
