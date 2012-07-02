package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.gallery.AlbumDTO;
import com.silverpeas.mobile.shared.dto.gallery.PhotoDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.GalleryException;

@RemoteServiceRelativePath("Gallery")
public interface ServiceGallery extends RemoteService {		
	public void uploadPicture(String name, String data, String idGallery, String idAlbum) throws GalleryException, AuthenticationException;
	public List<ApplicationInstanceDTO> getAllGalleries() throws GalleryException, AuthenticationException;
	public List<AlbumDTO> getAllAlbums(String instanceId) throws GalleryException, AuthenticationException;
	public List<PhotoDTO> getAllPictures(String instanceId, String albumId) throws GalleryException, AuthenticationException;
	public PhotoDTO getOriginalPicture(String instanceId, String pictureId) throws GalleryException, AuthenticationException;
	public PhotoDTO getPreviewPicture(String instanceId, String pictureId) throws GalleryException, AuthenticationException;
}
