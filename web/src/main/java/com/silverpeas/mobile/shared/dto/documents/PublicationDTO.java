package com.silverpeas.mobile.shared.dto.documents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.silverpeas.mobile.shared.dto.BaseDTO;

public class PublicationDTO extends BaseDTO implements Serializable, Comparable<PublicationDTO> {

	private static final long serialVersionUID = 1L;	
	private String name;
	private String description;
	private String version;
	private String creator;
	private String updater;
	private String wysiwyg;
	private String updateDate;
	private ArrayList<AttachmentDTO> attachments = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int compareTo(PublicationDTO o) {
		return name.compareTo(o.getName());
	}
	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(ArrayList<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public String getWysiwyg() {
		return wysiwyg;
	}
	public void setWysiwyg(String wysiwyg) {
		this.wysiwyg = wysiwyg;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {		
		this.updateDate = updateDate;
	}

}
