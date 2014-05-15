package com.silverpeas.mobile.client.persist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class User {
	private String login;
	private String password;
	private String domainId;
	
	public User() {
		super();		
	}
	
	public static User getInstance(String json) {			
		UserCodec codec = GWT.create(UserCodec.class);
		return codec.decode(JSONParser.parseStrict(json));		
	}
	
	public User(String login, String password, String domainId) {
		super();
		this.setLogin(login);
		this.setPassword(password);
		this.setDomainId(domainId);
	}

	public String toJson() {
		UserCodec codec = GWT.create(UserCodec.class);
		JSONValue json = codec.encode(this);
		return json.toString();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
}
