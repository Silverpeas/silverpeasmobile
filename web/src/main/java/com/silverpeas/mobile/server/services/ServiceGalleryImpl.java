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

import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.stratelia.webactiv.util.node.model.NodePK;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.silverpeas.admin.ejb.AdminBusiness;
import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.delegate.PhotoDataCreateDelegate;
import com.silverpeas.gallery.model.AlbumDetail;
import com.silverpeas.gallery.model.PhotoDetail;
import com.silverpeas.gallery.model.PhotoPK;
import com.silverpeas.gallery.model.PhotoSize;
import com.silverpeas.mobile.server.common.LocalDiskFileItem;
import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.helpers.RotationSupport;
import com.silverpeas.mobile.shared.dto.media.AlbumDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;
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
	private AdminBusiness adminBm;
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
			InputStream in = new ByteArrayInputStream(dataDecoded);
			BufferedImage bi = ImageIO.read(in);
			if (directory != null) {
				int existingOrientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
				bi = RotationSupport.adjustOrientation(bi, existingOrientation);
			}
			// stockage temporaire de la photo upload
			String tempDir = System.getProperty("java.io.tmpdir");
			String filename = tempDir + File.separator + name + "." + extension;
			OutputStream outputStream = new FileOutputStream(filename);
			
			// TODO : When Silverpeas support extended exif metadata : preserve Exif metadata (rotate remove exif metadata)
			
			ImageIO.write(bi, extension, outputStream);	
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
	 * Retourne la liste des albums d'une appli media.
	 */
	private List<AlbumDTO> getAlbums(String instanceId, String rootAlbumId) throws GalleryException, AuthenticationException {
		checkUserInSession();
		
		ArrayList<AlbumDTO> results = new ArrayList<AlbumDTO>();
		try {
      if (rootAlbumId == null) {
        Collection<AlbumDetail> albums = getGalleryBm().getAllAlbums(instanceId);
        for (AlbumDetail albumDetail : albums) {
          if (albumDetail.getLevel() == 2) {
            AlbumDTO album = populate(albumDetail);
            results.add(album);
          }
        }
      } else {
        AlbumDetail rootAlbum = getGalleryBm().getAlbum(new NodePK(rootAlbumId, instanceId), false);
        Collection<AlbumDetail> albums = rootAlbum.getChildrenAlbumsDetails();
        for (AlbumDetail albumDetail : albums) {
          AlbumDTO album = populate(albumDetail);
          results.add(album);
        }
      }
		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceGalleryImpl.getAlbums", "root.EX_NO_MESSAGE", e);
		}
		return results;
	}

  private AlbumDTO populate(final AlbumDetail albumDetail) {
    AlbumDTO album = new AlbumDTO();
    album.setId(String.valueOf(albumDetail.getId()));
    album.setName(albumDetail.getName());
    return album;
  }


  /**
	 * Retourne les photos miniatures d'un album.
	 */
	private List<PhotoDTO> getPictures(String instanceId, String albumId) throws GalleryException, AuthenticationException {
		checkUserInSession();
		
		ArrayList<PhotoDTO> results = new ArrayList<PhotoDTO>();
    if (albumId == null) return results;
		try {

      Collection<PhotoDetail> photos = getGalleryBm().getAllPhoto(new NodePK(albumId, instanceId), false);
			Iterator<PhotoDetail> iPhotos = photos.iterator();
					while (iPhotos.hasNext()) {
						PhotoDetail photoDetail = (PhotoDetail) iPhotos.next();					
						PhotoDTO photo = getPicture(instanceId, photoDetail.getId(), PhotoSize.SMALL);
						results.add(photo);
					}					
			return results;

		} catch (Exception e) {
			SilverTrace.error(SpMobileLogModule.getName(), "ServiceGalleryImpl.getAllPictures", "root.EX_NO_MESSAGE", e);			
		}
		return results;
	}

  public List<BaseDTO> getAlbumsAndPictures(String instanceId, String rootAlbumId) throws GalleryException, AuthenticationException {
	checkUserInSession();
    ArrayList<BaseDTO> list = new ArrayList<BaseDTO>();
    list.addAll(getAlbums(instanceId, rootAlbumId));
    list.addAll(getPictures(instanceId, rootAlbumId));
    return list;
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
	
	private AdminBusiness getAdminBm() throws Exception {
		if (adminBm == null) {
			adminBm = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBusiness.class);			
		}
		return adminBm;
	}
	
	private GalleryBm getGalleryBm() throws Exception {
		if (galleryBm == null) {
			galleryBm = EJBUtilitaire.getEJBObjectRef(JNDINames.GALLERYBM_EJBHOME, GalleryBm.class);			  
		}
		return galleryBm;
	}
}
