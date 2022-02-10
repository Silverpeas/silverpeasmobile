/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.documents.events.pages.publication;

import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;

public class PublicationLoadedEvent extends AbstractPublicationPagesEvent {

  private PublicationDTO publication;
  private boolean commentable, ableToStoreContent, notifiable;
  private String type;

  public PublicationLoadedEvent(PublicationDTO publication, boolean commentable, boolean ableToStoreContent, boolean notifiable, String type) {
    super();
    this.notifiable = notifiable;
    this.publication = publication;
    this.commentable = commentable;
    this.ableToStoreContent = ableToStoreContent;
    this.type = type;
  }

  @Override
  protected void dispatch(PublicationNavigationPagesEventHandler handler) {
    handler.onLoadedPublication(this);
  }

  public PublicationDTO getPublication() {
    return publication;
  }

  public boolean isCommentable() {
    return commentable;
  }

  public boolean isAbleToStoreContent() {
    return ableToStoreContent;
  }

  public boolean isNotifiable() {
    return notifiable;
  }

  public String getType() { return type; }
}
