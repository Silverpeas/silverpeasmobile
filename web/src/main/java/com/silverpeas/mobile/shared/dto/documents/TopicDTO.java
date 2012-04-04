package com.silverpeas.mobile.shared.dto.documents;

import java.io.Serializable;

public class TopicDTO implements Serializable, Comparable<TopicDTO> {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private boolean terminal;
	
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
	public int compareTo(TopicDTO o) {
		return name.compareTo(o.getName());
	}
	public boolean isTerminal() {
		return terminal;
	}
	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

}
