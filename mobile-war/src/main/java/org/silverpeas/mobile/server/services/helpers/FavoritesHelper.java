/*
 * Copyright (C) 2000 - 2025 Silverpeas
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.server.services.helpers;

import org.silverpeas.core.mylinks.model.LinkDetail;
import org.silverpeas.core.mylinks.service.MyLinksService;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.FavoriteDTO;
import org.silverpeas.mobile.shared.dto.MyLinkDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: svu
 */
public class FavoritesHelper {

  private static FavoritesHelper instance;

  public static FavoritesHelper getInstance() {
    if (instance == null) {
      instance = new FavoritesHelper();
    }
    return instance;
  }

  public List<LinkDetail> getBookmarkPerso(String userId) {
    return (List<LinkDetail>) getMyLinksBm().getAllLinksByUser(userId);
  }

  public List<LinkDetail> getBookmarkPersoVisible(String userId) {
    ArrayList<LinkDetail> linksVisibles = new ArrayList<LinkDetail>();
    List<LinkDetail> links = (List<LinkDetail>) getMyLinksBm().getAllLinksByUser(userId);
    for (LinkDetail link : links) {
      if (link.isVisible()) {
        linksVisibles.add(link);
      }
    }
    return linksVisibles;
  }

  public void addToFavorite(String instanceId, String objectId, String objectType, String description, String userId) {
    LinkDetail link = new LinkDetail();
    String url = "";

    if (objectType.equals(ContentsTypes.Folder.name())) {
      url = "/Topic/" + objectId + "?ComponentId=" + instanceId;
    } else if (objectType.equals(ContentsTypes.Space.name())) {
      url = "/Space/" + objectId;
    } else if (objectType.equals(ContentsTypes.Publication.name())) {
      url = "/Publication/" + objectId;
    } else if (objectType.equals(ContentsTypes.App.name())) {
      url = "/Component/" + instanceId;
    } else if (objectType.equals(ContentsTypes.Album.name())) {
      url = "/Rgallery/" + instanceId + "/ViewAlbum?Id=" + objectId;
    } else if (objectType.equals(ContentsTypes.Media.name())) {
      url = "/Media/" + objectId;
    }

    link.setUrl(url);
    link.setName(description);
    link.setHasPosition(false);
    link.setPosition(0);
    link.setVisible(true);
    link.setPopup(false);
    link.setUserId(userId);
    getMyLinksBm().createLink(link);
  }

  private MyLinksService getMyLinksBm() {
    return ServiceProvider.getService(MyLinksService.class);
  }

  public MyLinkDTO populate(LinkDetail link) {
    MyLinkDTO dto = new MyLinkDTO();
    dto.setName(link.getName());
    dto.setDescription(link.getDescription());
    dto.setPosition(link.getPosition());
    dto.setUrl(link.getUrl());
    return dto;
  }

  public List<MyLinkDTO> populate(List<LinkDetail> links) {
    List<MyLinkDTO> dtos = new ArrayList<MyLinkDTO>();
    if (links != null) {
      for (LinkDetail link : links) {
        MyLinkDTO dto = populate(link);
        dtos.add(dto);
      }
    }
    return dtos;

  }
}
