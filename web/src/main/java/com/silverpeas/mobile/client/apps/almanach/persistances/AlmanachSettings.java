package com.silverpeas.mobile.client.apps.almanach.persistances;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class AlmanachSettings {	
	
	private String selectedInstanceId;
	private String selectedInstanceLabel;
			
	public AlmanachSettings(String selectedInstanceId, String selectedInstanceLabel) {
		super();
		this.selectedInstanceId = selectedInstanceId;
		this.selectedInstanceLabel = selectedInstanceLabel;
	}
	
	public AlmanachSettings() {
		super();
	}
	
	public static AlmanachSettings getInstance(String json) {			
		AlmanachSettingsCodec codec = GWT.create(AlmanachSettingsCodec.class);
		return codec.decode(JSONParser.parseStrict(json));		
	}
	
	public String getSelectedInstanceId() {
		return selectedInstanceId;
	}
	public void setSelectedInstanceId(String selectedInstanceId) {
		this.selectedInstanceId = selectedInstanceId;
	}
	public String getSelectedInstanceLabel() {
		return selectedInstanceLabel;
	}
	public void setSelectedInstanceLabel(String selectedInstanceLabel) {
		this.selectedInstanceLabel = selectedInstanceLabel;
	}

	public String toJson() {
		AlmanachSettingsCodec codec = GWT.create(AlmanachSettingsCodec.class);
		JSONValue json = codec.encode(this);
		return json.toString();
	}	
}
