package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.GalleryException;

@RemoteServiceRelativePath("Gallery")
public interface ServiceGallery extends RemoteService {		
	public void uploadPicture(String name, String data) throws GalleryException, AuthenticationException;
}
