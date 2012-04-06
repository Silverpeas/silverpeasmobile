package com.silverpeas.mobile.shared.dto.documents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PublicationDTO implements Serializable, Comparable<PublicationDTO> {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private ArrayList<AttachmentDTO> attachments = null;
		
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
	public int compareTo(PublicationDTO o) {
		return name.compareTo(o.getName());
	}
	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(ArrayList<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}
}
