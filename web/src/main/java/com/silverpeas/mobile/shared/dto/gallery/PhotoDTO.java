package com.silverpeas.mobile.shared.dto.gallery;

import java.io.Serializable;

public class PhotoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private boolean download;
	private String dataPhotoTiny;
	private String dataPhotoPreview;
	private String dataPhotoOriginal;
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
	public String getDataPhotoTiny() {
		return dataPhotoTiny;
	}
	public void setDataPhotoTiny(String dataPhotoTiny) {
		this.dataPhotoTiny = dataPhotoTiny;
	}
	public String getDataPhotoOriginal() {
		return dataPhotoOriginal;
	}
	public void setDataPhotoOriginal(String dataPhotoOriginal) {
		this.dataPhotoOriginal = dataPhotoOriginal;
	}
	public String getDataPhotoPreview() {
		return dataPhotoPreview;
	}
	public void setDataPhotoPreview(String dataPhotoPreview) {
		this.dataPhotoPreview = dataPhotoPreview;
	}
}
