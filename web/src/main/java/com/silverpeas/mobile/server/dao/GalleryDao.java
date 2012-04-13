/**
 * Copyright (C) 2000 - 2011 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.server.dao;

import java.rmi.RemoteException;

import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.control.ejb.GalleryBmHome;
import com.silverpeas.gallery.model.PhotoDetail;
import com.silverpeas.gallery.model.PhotoPK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;

public class GalleryDao {

  private GalleryBm galleryBm = null;

  private GalleryBm getGalleryBm() {
    if (galleryBm == null) {
      try {
        GalleryBmHome home =
            EJBUtilitaire.getEJBObjectRef(JNDINames.GALLERYBM_EJBHOME, GalleryBmHome.class);
        galleryBm = home.create();
      } catch (Exception e) {
        // TODO : fixer me!
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

    // TODO : upload file ?

  }

}
