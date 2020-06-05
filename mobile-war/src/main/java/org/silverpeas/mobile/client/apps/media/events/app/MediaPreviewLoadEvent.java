/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.client.apps.media.events.app;

import org.silverpeas.mobile.shared.dto.media.MediaDTO;

public class MediaPreviewLoadEvent extends AbstractMediaAppEvent {

  private String instanceId, mediaId, contentType;
  private MediaDTO media;

  public MediaPreviewLoadEvent(String instanceId, String contentType, String mediaId, MediaDTO media) {
    super();
    this.mediaId = mediaId;
    this.contentType = contentType;
    this.instanceId = instanceId;
    this.media = media;
  }

  @Override
  protected void dispatch(MediaAppEventHandler handler) {
    handler.loadMediaPreview(this);
  }

  public String getInstanceId() {
    return instanceId;
  }

  public String getMediaId() {
    return mediaId;
  }

  public MediaDTO getMedia() {
    return media;
  }

  public String getContentType() {
    return contentType;
  }
}
