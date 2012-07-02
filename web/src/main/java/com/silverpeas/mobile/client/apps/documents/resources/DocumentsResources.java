package com.silverpeas.mobile.client.apps.documents.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface DocumentsResources extends ClientBundle {
	
	@Source("documents.css")
	DocumentsCSS css();
	
	@Source("instances.png")
	ImageResource instances();

	@Source("topics.png")
	ImageResource topics();
	
	@Source("msword.png")
	ImageResource msword();
	
	@Source("msexcel.png")	
	ImageResource msexcel();
	
	@Source("pdf.png")
	ImageResource pdf();
	
	@Source("unknown.png")
	ImageResource unknown();
}
