package com.silverpeas.mobile.client.apps.media.persistances;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;


public class Picture {
	private String id;
	private String Uri;
	private String name;
	
	public Picture() {
		super();
	}
	
	public static Picture getInstance(String json) {			
		PictureCodec codec = GWT.create(PictureCodec.class);
		return codec.decode(JSONParser.parseStrict(json));		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUri() {
		return Uri;
	}
	public void setUri(String uri) {
		Uri = uri;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toJson() {
		PictureCodec codec = GWT.create(PictureCodec.class);
		JSONValue json = codec.encode(this);
		return json.toString();
	}	
	
}
