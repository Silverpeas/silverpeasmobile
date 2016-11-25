package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class SocialInformationDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String description;
	private Date date;
	private String type;
	private String auteur;
	
	public String getDescription(){
		return description;
	}
	
	public Date getDate(){
		return date;
	}
	
	public String getType(){
		return type;
	}
	
	public String getAuteur(){
		return auteur;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setAuteur(String auteur){
		this.auteur = auteur;
	}
}
