package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

public class DetailUserDTO implements Serializable{

	private static final long serialVersionUID = 5388415881024885835L;
	private String id;
	private String lastName;
	private String eMail;
	private String firstName;
	private String avatar;
	private String phoneNumber;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String LastName) {
		this.lastName = LastName;
	}
	
	public void seteMail(String EMail) {
	    eMail = EMail;
	}

	public String geteMail() {
	    return eMail;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public String getAvatar(){
		return avatar;
	}
	
	public void setAvatar(String avatar){
		this.avatar = avatar;
	}
	
	public String getPhoneNumber(){
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
}
