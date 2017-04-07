/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.silverpeas.mobile.server.services.helpers;

import com.silverpeas.mobile.shared.dto.FavoriteDTO;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.mylinks.model.LinkDetail;
import org.silverpeas.core.mylinks.service.DefaultMyLinksService;
import org.silverpeas.core.mylinks.service.MyLinksService;
import org.silverpeas.core.util.ServiceProvider;

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

  private MyLinksService getMyLinksBm() {
    return ServiceProvider.getService(MyLinksService.class);
  }

  public FavoriteDTO populate(LinkDetail link) {
    FavoriteDTO dto = new FavoriteDTO();
    dto.setName(link.getName());
    dto.setDescription(link.getDescription());
    dto.setHasPosition(link.hasPosition());
    dto.setPosition(link.getPosition());
    dto.setUrl(link.getUrl());
    return dto;
  }

  public List<FavoriteDTO> populate(List<LinkDetail> links) {
    List<FavoriteDTO> dtos = new ArrayList<FavoriteDTO>();
    if (links != null) {
      for (LinkDetail link : links) {
        FavoriteDTO dto = populate(link);
        dtos.add(dto);
      }
    }
    return dtos;

  }
}
