package com.silverpeas.mobile.client.apps.almanach.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface AlmanachResources extends ClientBundle{
	@Source("almanach.css")
	AlmanachCSS css();
	
	@Source("almanachs.png")
	ImageResource almanachs();
	
	@Source("cal.png")
	ImageResource cal();
}
