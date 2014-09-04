package com.silverpeas.mobile.client.apps.documents.events.pages.publication;

import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;

public class PublicationLoadedEvent extends AbstractPublicationPagesEvent {

  private PublicationDTO publication;
  private boolean commentable;

  public PublicationLoadedEvent(PublicationDTO publication, boolean commentable) {
    super();
    this.publication = publication;
    this.commentable = commentable;
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
}
