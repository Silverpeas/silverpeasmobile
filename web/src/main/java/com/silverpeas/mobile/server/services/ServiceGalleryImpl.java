package com.silverpeas.mobile.server.services;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.admin.ejb.AdminBmHome;
import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.control.ejb.GalleryBmHome;
import com.silverpeas.gallery.delegate.PhotoDataCreateDelegate;
import com.silverpeas.gallery.model.AlbumDetail;
import com.silverpeas.gallery.model.PhotoDetail;
import com.silverpeas.gallery.model.PhotoPK;
import com.silverpeas.gallery.model.PhotoSize;
import com.silverpeas.mobile.server.common.LocalDiskFileItem;
import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.helpers.RotationSupport;
import com.silverpeas.mobile.shared.dto.gallery.AlbumDTO;
import com.silverpeas.mobile.shared.dto.gallery.PhotoDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.GalleryException;
import com.silverpeas.mobile.shared.services.ServiceGallery;
import com.silverpeas.util.StringUtil;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.ComponentInstLight;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.FileRepositoryManager;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.ResourceLocator;

/**
 * Service de gestion des galleries d'images.
 * @author svuillet
 */
public class ServiceGalleryImpl extends AbstractAuthenticateService implements ServiceGallery {
	
	private static final long serialVersionUID = 1L;
	private AdminBm adminBm;
	private GalleryBm galleryBm;
		
	/**
	 * Importation d'une image dans un album.
	 */
	public void uploadPicture(String name, String data, String idGallery, String idAlbum) throws GalleryException, AuthenticationException {
		checkUserInSession();
		
		String extension = "jpg";
		if (data.indexOf("data:image/jpeg;base64,") != -1) {
			data = data.substring("data:image/jpeg;base64,".length());
			extension = "jpg";		
		}
		
		byte [] dataDecoded = org.apache.commons.codec.binary.Base64.decodeBase64(data.getBytes());
				
		try {
			// rotation de l'image si nécessaire
			Metadata metadata = ImageMetadataReader.readMetadata(new BufferedInputStream(new ByteArrayInputStream(dataDecoded)), true);
			Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
			int existingOrientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);			
			InputStream in = new ByteArrayInputStream(dataDecoded);
			BufferedImage bi = ImageIO.read(in);
			BufferedImage bir = RotationSupport.adjustOrientation(bi, existingOrientation);
			
			// stockage temporaire de la photo upload
			String tempDir = System.getProperty("java.io.tmpdir");
			String filename = tempDir + File.separator + name + "." + extension;
			OutputStream outputStream = new FileOutputStream(filename);
			
			// TODO : When Silverpeas support extended exif metadata : preserve Exif metadata (rotate remove exif metadata)
			
			ImageIO.write(bir, extension, outputStream);	
			outputStream.close();
						
			File file = new File(filename);
			RotationSupport.setOrientation(RotationSupport.NOT_ROTATED, file);
			
			// récupération de la configuration de la gallery			
			OrganizationController orga = new OrganizationController();
		    boolean watermark = "yes".equalsIgnoreCase(orga.getComponentParameterValue(idGallery, "watermark"));
		    boolean download = !"no".equalsIgnoreCase(orga.getComponentParameterValue(idGallery, "download"));
		    String watermarkHD = orga.getComponentParameterValue(idGallery, "WatermarkHD");
		    if(!StringUtil.isInteger(watermarkHD))  {
		      watermarkHD = "";
		    }
		    String watermarkOther = orga.getComponentParameterValue(idGallery, "WatermarkOther");
		     if(!StringUtil.isInteger(watermarkOther))  {
		      watermarkOther = "";
		    }			
			
		    // creation de la photo dans l'albums
			createPhoto(name, getUserInSession().getId(), idGallery, idAlbum, file, watermark, watermarkHD, watermarkOther, download);
			file.delete();
			
		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceGalleryImpl.uploadPicture", "root.EX_NO_MESSAGE", e);			
		}	
	}
	
	private String createPhoto(String name, String userId, String componentId,
		      String albumId, File file, boolean watermark, String watermarkHD,
		      String watermarkOther, boolean download)
		      throws Exception {

	    // création de la photo
	    PhotoDetail newPhoto = new PhotoDetail(name, null, new Date(), null, null, null, download, false);
	    newPhoto.setAlbumId(albumId);
	    newPhoto.setCreatorId(userId);
	    PhotoPK pk = new PhotoPK("unknown", componentId);
	    newPhoto.setPhotoPK(pk);

	    List<FileItem> parameters = new ArrayList<FileItem>();    
	    LocalDiskFileItem item = new LocalDiskFileItem(file);
	    parameters.add(item);    
	    
	    PhotoDataCreateDelegate delegate = new PhotoDataCreateDelegate("fr", albumId, parameters);
	    
	    getGalleryBm().createPhoto(getUserInSession(), componentId, newPhoto, watermark, watermarkHD, watermarkOther, delegate);
	    
	    return newPhoto.getId();
	}
	
	
	/**
	 * Retourne la listes des galleries accessibles.
	 */
	public List<ApplicationInstanceDTO> getAllGalleries() throws GalleryException, AuthenticationException {
		checkUserInSession();
		
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
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceGalleryImpl.getAllGalleries", "root.EX_NO_MESSAGE", e);			
		}
		
		Collections.sort(results);		
		return results;		
	}
	
	/**
	 * Retourne la liste des albums d'une galerie.
	 */
	public List<AlbumDTO> getAllAlbums(String instanceId) throws GalleryException, AuthenticationException {
		checkUserInSession();
		
		ArrayList<AlbumDTO> results = new ArrayList<AlbumDTO>();
		try {			
			Collection<AlbumDetail> albums = getGalleryBm().getAllAlbums(instanceId);
			for (AlbumDetail albumDetail : albums) {
				if (albumDetail.getLevel() != 1) {
					AlbumDTO album = new AlbumDTO();
					album.setId(String.valueOf(albumDetail.getId()));
					album.setName(albumDetail.getName());				
					results.add(album);
				}				
			}
		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceGalleryImpl.getAllAlbums", "root.EX_NO_MESSAGE", e);			
		}
		return results;
	}
	
	
	/**
	 * Retourne les photos miniatures d'un album.
	 */
	public List<PhotoDTO> getAllPictures(String instanceId, String albumId) throws GalleryException, AuthenticationException {
		checkUserInSession();
		
		ArrayList<PhotoDTO> results = new ArrayList<PhotoDTO>();
		try {			
			Collection<AlbumDetail> albums = getGalleryBm().getAllAlbums(instanceId);
			for (AlbumDetail albumDetail : albums) {
				if (albumDetail.getId() == Integer.parseInt(albumId)) {
					Collection<PhotoDetail> photos = getGalleryBm().getAllPhoto(albumDetail.getNodePK(), false);
					Iterator<PhotoDetail> iPhotos = photos.iterator();
					while (iPhotos.hasNext()) {
						PhotoDetail photoDetail = (PhotoDetail) iPhotos.next();					
						PhotoDTO photo = getPicture(instanceId, photoDetail.getId(), PhotoSize.SMALL);				
						results.add(photo);
					}					
					return results;
				}				
			}
		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceGalleryImpl.getAllPictures", "root.EX_NO_MESSAGE", e);			
		}
		return results;
	}
	
	/**
	 * Retourne la photo originale.
	 */
	public PhotoDTO getOriginalPicture(String instanceId, String pictureId) throws GalleryException, AuthenticationException {
		checkUserInSession();
		
		PhotoDTO picture = null;
		try {		
			picture = getPicture(instanceId, pictureId, PhotoSize.ORIGINAL);	
		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceGalleryImpl.getOriginalPicture", "root.EX_NO_MESSAGE", e);			
		}
		return picture;
	}
	
	/**
	 * Retourne la photo preview.
	 */
	public PhotoDTO getPreviewPicture(String instanceId, String pictureId) throws GalleryException, AuthenticationException {
		checkUserInSession();
		
		PhotoDTO picture = null;
		try {		
			picture = getPicture(instanceId, pictureId, PhotoSize.PREVIEW);
		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceGalleryImpl.getPreviewPicture", "root.EX_NO_MESSAGE", e);			
		}
		return picture;
	}	
	
	private PhotoDTO getPicture(String instanceId, String pictureId, PhotoSize size) throws RemoteException, Exception, FileNotFoundException, IOException {
		PhotoDTO picture;
		PhotoDetail photoDetail = getGalleryBm().getPhoto(new PhotoPK(pictureId));
		picture = new PhotoDTO();
		picture.setId(photoDetail.getId());
		picture.setDownload(photoDetail.isDownload());						
		picture.setDataPhoto(getBase64ImageData(instanceId, photoDetail, size));
		picture.setFormat(size.name());
		picture.setTitle(photoDetail.getTitle());
		return picture;
	}

	@SuppressWarnings("deprecation")
	private String getBase64ImageData(String instanceId, PhotoDetail photoDetail, PhotoSize size) throws FileNotFoundException, IOException {
		ResourceLocator gallerySettings = new ResourceLocator("com.silverpeas.gallery.settings.gallerySettings", "");
		String nomRep = gallerySettings.getString("imagesSubDirectory") + photoDetail.getPhotoPK().getId();
		String[] rep = {nomRep};
		String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);
		File f;
		if (size.equals(PhotoSize.ORIGINAL)) {
			f = new File(path+ photoDetail.getImageName());
		} else {
			f = new File(path+ photoDetail.getPhotoPK().getId() + size.getPrefix());
		}	
		
		FileInputStream is = new FileInputStream(f);
		byte[] binaryData = new byte[(int) f.length()];
		is.read(binaryData);
		is.close();
		String data = "data:" + photoDetail.getImageMimeType() + ";base64," + new String(Base64.encodeBase64(binaryData));
		
		return data;
	}
	
	private AdminBm getAdminBm() throws Exception {
		if (adminBm == null) {
			AdminBmHome home = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBmHome.class);
			adminBm = home.create();
		}
		return adminBm;
	}
	
	private GalleryBm getGalleryBm() throws Exception {
		if (galleryBm == null) {
			GalleryBmHome home = EJBUtilitaire.getEJBObjectRef(JNDINames.GALLERYBM_EJBHOME, GalleryBmHome.class);
			galleryBm = home.create(); 
		}
		return galleryBm;
	}
}
