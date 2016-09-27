package com.silverpeas.mobile.client.apps.documents.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface DocumentsResources extends ClientBundle {
	
	@Source("documents.css")
	DocumentsCSS css();
		
	@Source("word.gif")
	ImageResource msword();
	
	@Source("excel.gif")	
	ImageResource msexcel();
	
	@Source("powerpoint.gif")	
	ImageResource mspowerpoint();

	@Source("image.gif")
	ImageResource image();
	
	@Source("pdf.gif")
	ImageResource pdf();
	
	@Source("unknown.gif")
	ImageResource unknown();
}
