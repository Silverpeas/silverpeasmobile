package com.silverpeas.mobile.shared.dto.media;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class PhotoDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean download;
	private String dataPhoto;
	private String format;
	private String title;
  private String name;
  private long size;
  private int sizeH;
  private int sizeL;
  private String updateDate;
  private String updater;
  private int commentsNumber;

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

  public void setName(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setSize(final long size) {
    this.size = size;
  }

  public long getSize() {
    return size;
  }

  public void setSizeH(final int sizeH) {
    this.sizeH = sizeH;
  }

  public int getSizeH() {
    return sizeH;
  }

  public void setSizeL(final int sizeL) {
    this.sizeL = sizeL;
  }

  public int getSizeL() {
    return sizeL;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(final String updateDate) {
    this.updateDate = updateDate;
  }

  public String getUpdater() {
    return updater;
  }

  public void setUpdater(final String updater) {
    this.updater = updater;
  }

  public void setCommentsNumber(final int commentsNumber) {
    this.commentsNumber = commentsNumber;
  }

  public int getCommentsNumber() {
    return commentsNumber;
  }
}
