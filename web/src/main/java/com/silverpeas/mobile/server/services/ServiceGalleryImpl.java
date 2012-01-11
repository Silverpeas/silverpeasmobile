package com.silverpeas.mobile.server.services;

import java.io.FileOutputStream;
import java.io.OutputStream;

import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.GalleryException;
import com.silverpeas.mobile.shared.services.ServiceGallery;

/**
 * Service de gestion des galleries d'images.
 * @author svuillet
 */
public class ServiceGalleryImpl extends AbstractAuthenticateService implements ServiceGallery {

	private static final long serialVersionUID = 1L;
	
	public void uploadPicture(String name, String data) throws GalleryException, AuthenticationException {
		checkUserInSession();
		
		byte [] dataDecoded = org.apache.commons.codec.binary.Base64.decodeBase64(data.getBytes());
				
		try {
			OutputStream outputStream = new FileOutputStream("/home/svuillet/" + name + ".jpg");
			outputStream.write(dataDecoded);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
