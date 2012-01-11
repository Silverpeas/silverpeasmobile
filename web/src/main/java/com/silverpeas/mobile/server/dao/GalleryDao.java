package com.silverpeas.mobile.server.dao;

import java.rmi.RemoteException;

import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.model.GalleryRuntimeException;
import com.silverpeas.gallery.model.PhotoDetail;
import com.silverpeas.gallery.model.PhotoPK;
import com.silverpeas.gallery.servlets.GalleryRequestRouter;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.stratelia.webactiv.util.JNDINames;

public class GalleryDao {
	
	private GalleryBm galleryBm = null;
	
	private GalleryBm getGalleryBm()
	{
		if (galleryBm == null) {
			try {
				galleryBm = (GalleryBm)EJBDynaProxy.createProxy(JNDINames.GALLERYBM_EJBHOME , GalleryBm.class);
			}
			catch (Exception e)	{
				//TODO : fixer me!
			}
		}
		return galleryBm;
	}
	
	public void addPicture(String userId) throws RemoteException {
		PhotoDetail photo = new PhotoDetail();			
	    photo.setCreatorId(userId);
	    PhotoPK pk = new PhotoPK("unknown", "gallery2");
	    photo.setPhotoPK(pk);
	    String albumId = "51";
		
		getGalleryBm().createPhoto(photo, albumId);
		
		//TODO : upload file ?
		
	}
	
}
