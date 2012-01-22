package com.silverpeas.mobile.server.services;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.model.AlbumDetail;
import com.silverpeas.mobile.shared.dto.AlbumDTO;
import com.silverpeas.mobile.shared.dto.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.GalleryException;
import com.silverpeas.mobile.shared.services.ServiceGallery;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.stratelia.webactiv.beans.admin.ComponentInstLight;
import com.stratelia.webactiv.util.JNDINames;

/**
 * Service de gestion des galleries d'images.
 * @author svuillet
 */
public class ServiceGalleryImpl extends AbstractAuthenticateService implements ServiceGallery {

	private static final long serialVersionUID = 1L;
	private AdminBm adminBm;
	private GalleryBm galleryBm;
	
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
	
	
	public List<ApplicationInstanceDTO> getAllGalleries() throws GalleryException, AuthenticationException {
		
		ArrayList<ApplicationInstanceDTO> results = new ArrayList<ApplicationInstanceDTO>();
		try {
			List<String> rootSpaceIds = getAdminBm().getAllRootSpaceIds();
			for (String rootSpaceId : rootSpaceIds) {
				List<String> componentIds = getAdminBm().getAvailCompoIds(rootSpaceId, getUserInSession().getId());
				for (String componentId : componentIds) {
					ComponentInstLight instance = getAdminBm().getComponentInstLight(componentId);
					if (instance.getName().equals("gallery")) {
						ApplicationInstanceDTO i = new ApplicationInstanceDTO();
						i.setId(instance.getId());
						i.setLabel(instance.getLabel());
						results.add(i);
					}
				}				
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results;		
	}
	
	public List<AlbumDTO> getAllAlbums(String instanceId) throws GalleryException, AuthenticationException {
		ArrayList<AlbumDTO> results = new ArrayList<AlbumDTO>();
		try {			
			Collection<AlbumDetail> albums = getGalleryBm().getAllAlbums(instanceId);
			for (AlbumDetail albumDetail : albums) {
				AlbumDTO album = new AlbumDTO();
				album.setId(String.valueOf(albumDetail.getId()));
				album.setName(albumDetail.getName());				
				results.add(album);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	private AdminBm getAdminBm() throws Exception {
		if (adminBm == null) {
			adminBm = (AdminBm) EJBDynaProxy.createProxy(JNDINames.ADMINBM_EJBHOME, AdminBm.class);
		}
		return adminBm;
	}
	
	private GalleryBm getGalleryBm() throws Exception {
		if (galleryBm == null) {
			galleryBm = (GalleryBm) EJBDynaProxy.createProxy(JNDINames.GALLERYBM_EJBHOME, GalleryBm.class);
		}
		return galleryBm;
	}
}
