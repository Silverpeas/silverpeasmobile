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

package com.silverpeas.mobile.client.apps.gallery.events.pages;

import java.util.List;

import com.silverpeas.mobile.client.apps.gallery.persistances.GallerySettings;
import com.silverpeas.mobile.shared.dto.gallery.AlbumDTO;

public class GalleryLoadedSettingsEvent extends AbstractGalleryPagesEvent {

  private GallerySettings settings;
  private List<AlbumDTO> albums;

  public GalleryLoadedSettingsEvent(GallerySettings settings, List<AlbumDTO> albums) {
    super();
    this.settings = settings;
    this.albums = albums;
  }

  @Override
  protected void dispatch(GalleryPagesEventHandler handler) {
    handler.onLoadedSettings(this);
  }

  public GallerySettings getSettings() {
    return settings;
  }

  public List<AlbumDTO> getAlbums() {
    return albums;
  }
}
