package com.silverpeas.mobile.client.apps.documents.events.pages.publication;

import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;

public class PublicationLoadedEvent extends AbstractPublicationPagesEvent {

  private PublicationDTO publication;
  private boolean commentable, ableToStoreContent, notifiable;

  public PublicationLoadedEvent(PublicationDTO publication, boolean commentable, boolean ableToStoreContent, boolean notifiable) {
    super();
    this.notifiable = notifiable;
    this.publication = publication;
    this.commentable = commentable;
    this.ableToStoreContent = ableToStoreContent;
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
}
