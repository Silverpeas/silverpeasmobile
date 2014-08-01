package com.silverpeas.mobile.client.apps.media.persistances;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class GallerySettings {
	
	private String selectedAlbumId;
	private String selectedGalleryId;
	private String selectedGalleryLabel;
		
	public GallerySettings() {
		super();
	}	
	
	public GallerySettings(String selectedAlbumId, String selectedGalleryId, String selectedGalleryLabel) {
		super();
		this.selectedAlbumId = selectedAlbumId;
		this.selectedGalleryId = selectedGalleryId;
		this.selectedGalleryLabel = selectedGalleryLabel;
	}
	
	public static GallerySettings getInstance(String json) {			
		GallerySettingsCodec codec = GWT.create(GallerySettingsCodec.class);
		return codec.decode(JSONParser.parseStrict(json));		
	}

	public String getSelectedAlbumId() {
		return selectedAlbumId;
	}
	public void setSelectedAlbumId(String selectedAlbumId) {
		this.selectedAlbumId = selectedAlbumId;
	}
	public String getSelectedGalleryId() {
		return selectedGalleryId;
	}
	public void setSelectedGalleryId(String selectedGalleryId) {
		this.selectedGalleryId = selectedGalleryId;
	}
	public String getSelectedGalleryLabel() {
		return selectedGalleryLabel;
	}
	public void setSelectedGalleryLabel(String selectedGalleryLabel) {
		this.selectedGalleryLabel = selectedGalleryLabel;
	}
	
	public String toJson() {
		GallerySettingsCodec codec = GWT.create(GallerySettingsCodec.class);
		JSONValue json = codec.encode(this);
		return json.toString();
	}	
}
