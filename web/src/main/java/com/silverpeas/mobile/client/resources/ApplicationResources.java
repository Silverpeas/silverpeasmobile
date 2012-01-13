package com.silverpeas.mobile.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ApplicationResources extends ClientBundle {
	
	@Source("application.css")
	ApplicationCSS css();
	
	@Source("contacts.png")
	ImageResource contacts();
	
	@Source("documents.png")
	ImageResource documents();
	
	@Source("status.png")
	ImageResource status();
	
	@Source("dashboard.png")
	ImageResource dashboard();
	
	@Source("gallery.png")
	ImageResource gallery();
	
	@Source("agenda.png")
	ImageResource agenda();
	
	@Source("bookmark.png")
	ImageResource bookmark();
	
	@Source("notifications.png")
	ImageResource notifications();
	
	@Source("logo.gif")
	ImageResource logo();
	
	@Source("online.png")
	ImageResource online();
	
	@Source("offline.png")
	ImageResource offline();
	
	@Source("camera.png")
	ImageResource camera();
	
	@Source("sync.png")
	ImageResource sync();
	
	@Source("settings.png")
	ImageResource settings();
	
	@Source("localstorage.png")
	ImageResource localstorage();
}
