/*
 * Copyright (C) 2000 - 2018 Silverpeas
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
 */

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.mylinks.model.LinkDetail;
import org.silverpeas.mobile.server.services.helpers.FavoritesHelper;
import org.silverpeas.mobile.shared.dto.FavoriteDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.FavoritesException;
import org.silverpeas.mobile.shared.services.ServiceFavorites;

import java.util.List;

/**
 * Service de gestion des favoris.
 * @author svu
 */
public class ServiceFavoritesImpl extends AbstractAuthenticateService implements ServiceFavorites {

  private static final long serialVersionUID = 1L;

  @Override
  public List<FavoriteDTO> getFavorites() throws FavoritesException, AuthenticationException {
    checkUserInSession();
    List<LinkDetail> favorites = FavoritesHelper.getInstance().getBookmarkPerso(getUserInSession().getId());
    return FavoritesHelper.getInstance().populate(favorites);
  }

  @Override
  public void addFavorite(String instanceId, String objectId, String objectType, String description) throws FavoritesException, AuthenticationException {
    checkUserInSession();
    FavoritesHelper.getInstance().addToFavorite(instanceId,objectId,objectType,description, getUserInSession().getId());
  }

}
