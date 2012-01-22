package com.silverpeas.mobile.client.pages.gallery.controler;

import com.google.gwt.core.client.GWT;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.pages.gallery.controler.event.GalleryLoadedSettingsEvent;
import com.silverpeas.mobile.client.persist.GallerySettings;

public class GalleryControler {
	
	public void saveOrUpdateSettings(final String galleryId, final String albumId) {
		Database.open();		
		final Entity<GallerySettings> settingsEntity = GWT.create(GallerySettings.class);
		final Collection<GallerySettings> settings = settingsEntity.all();		
		Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {
			@Override
			public void onSuccess() {
				settings.destroyAll(new com.gwtmobile.persistence.client.Callback() {
					public void onSuccess() {						
						Persistence.flush();
						final Entity<GallerySettings> settingsEntity = GWT.create(GallerySettings.class);				
						Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {			
							public void onSuccess() {
								final GallerySettings settings = settingsEntity.newInstance();
								settings.setSelectedGalleryId(galleryId);
								settings.setSelectedAlbumId(albumId);
								Persistence.flush();
							}
						});				
					}
				});				
			}		
		});		
	}
	
	public void getSettings() {
		Database.open();		
		final Entity<GallerySettings> settingsEntity = GWT.create(GallerySettings.class);
		final Collection<GallerySettings> settings = settingsEntity.all().limit(1);			
		settings.one(new ScalarCallback<GallerySettings>() {
			public void onSuccess(GallerySettings result) {
				EventBus.getInstance().fireEvent(new GalleryLoadedSettingsEvent(result));
			}
		});
	}
}
