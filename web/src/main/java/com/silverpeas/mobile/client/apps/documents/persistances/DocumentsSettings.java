package com.silverpeas.mobile.client.apps.documents.persistances;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class DocumentsSettings {
	
	private String selectedTopicId;
	private String selectedInstanceId;
	private String selectedTopicLabel;
	private String selectedInstanceLabel;
		
	public DocumentsSettings() {
		super();
	}
	
	public DocumentsSettings(String selectedInstanceId, String selectedInstanceLabel) {
		super();
		this.selectedInstanceId = selectedInstanceId;
		this.selectedInstanceLabel = selectedInstanceLabel;
	}
	
	public static DocumentsSettings getInstance(String json) {			
		DocumentsSettingsCodec codec = GWT.create(DocumentsSettingsCodec.class);
		return codec.decode(JSONParser.parseStrict(json));		
	}

	public String getSelectedTopicId() {
		return selectedTopicId;
	}
	public void setSelectedTopicId(String selectedTopicId) {
		this.selectedTopicId = selectedTopicId;
	}
	public String getSelectedInstanceId() {
		return selectedInstanceId;
	}
	public void setSelectedInstanceId(String selectedInstanceId) {
		this.selectedInstanceId = selectedInstanceId;
	}
	public String getSelectedTopicLabel() {
		return selectedTopicLabel;
	}
	public void setSelectedTopicLabel(String selectedTopicLabel) {
		this.selectedTopicLabel = selectedTopicLabel;
	}
	public String getSelectedInstanceLabel() {
		return selectedInstanceLabel;
	}
	public void setSelectedInstanceLabel(String selectedInstanceLabel) {
		this.selectedInstanceLabel = selectedInstanceLabel;
	}
	
	public String toJson() {
		DocumentsSettingsCodec codec = GWT.create(DocumentsSettingsCodec.class);
		JSONValue json = codec.encode(this);
		return json.toString();
	}	
}
