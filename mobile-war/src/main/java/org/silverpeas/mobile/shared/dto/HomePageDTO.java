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

package org.silverpeas.mobile.shared.dto;

import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomePageDTO extends BaseDTO implements Serializable{

  private static final long serialVersionUID = 5388415881024885835L;

  private String spaceName;

  private List<SilverpeasObjectDTO> spacesAndApps;
  private List<NewsDTO> news = new ArrayList<NewsDTO>();
  private List<MyLinkDTO> favorites = new ArrayList<MyLinkDTO>();
  private List<PublicationDTO> lastPublications = new ArrayList<PublicationDTO>();

  public List<PublicationDTO> getLastPublications() {
    return lastPublications;
  }

  public void setLastPublications(final List<PublicationDTO> lastPublications) {
    this.lastPublications = lastPublications;
  }

  public List<NewsDTO> getNews() {
    return news;
  }

  public void setNews(final List<NewsDTO> news) {
    this.news = news;
  }

  public List<MyLinkDTO> getFavorites() {
    return favorites;
  }

  public void setFavorites(final List<MyLinkDTO> favorites) {
    this.favorites = favorites;
  }

  public List<SilverpeasObjectDTO> getSpacesAndApps() {
    return spacesAndApps;
  }

  public void setSpacesAndApps(final List<SilverpeasObjectDTO> spacesAndApps) {
    this.spacesAndApps = spacesAndApps;
  }

  public String getSpaceName() {
    return spaceName;
  }

  public void setSpaceName(final String spaceName) {
    this.spaceName = spaceName;
  }
}
