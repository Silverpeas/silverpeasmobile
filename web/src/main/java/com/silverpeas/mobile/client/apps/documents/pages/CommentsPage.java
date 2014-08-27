package com.silverpeas.mobile.client.apps.documents.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadCommentsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.comments.AbstractCommentsPagesEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.comments.CommentsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.comments.CommentsPagesEventHandler;
import com.silverpeas.mobile.client.apps.documents.pages.widgets.Comment;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.documents.CommentDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;

import java.util.List;

/**
 * @author: svu
 */
public class CommentsPage extends PageContent implements View, CommentsPagesEventHandler {

  interface CommentsPageUiBinder extends UiBinder<HTMLPanel, CommentsPage> {
  }
  @UiField HTMLPanel container;
  @UiField HeadingElement title;
  @UiField UnorderedList commentsList;
  @UiField SpanElement addCommentTitle;
  @UiField Anchor addComment;
  @UiField TextArea newComment;

  protected DocumentsMessages msg = null;
  private PublicationDTO publication;
  private List<CommentDTO> comments;

  private static CommentsPageUiBinder uiBinder = GWT.create(CommentsPageUiBinder.class);

  public CommentsPage() {
    msg = GWT.create(DocumentsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractCommentsPagesEvent.TYPE, this);
    container.getElement().setId("publication");
    addCommentTitle.setInnerHTML(msg.addComment());
  }

  public void setPublication(final PublicationDTO publication) {
    this.publication = publication;
    // send event to controler for retrieve comments infos
    Notification.activityStart();
    EventBus.getInstance().fireEvent(new DocumentsLoadCommentsEvent(publication.getId()));
  }

  @Override
  public void onLoadedComments(final CommentsLoadedEvent event) {
    Notification.activityStop();
    this.comments = event.getComments();

    for (CommentDTO comment : comments) {
      Comment c = new Comment();
      c.setComment(comment);
      commentsList.add(c);
    }
    title.setInnerText(msg.commentsPageTitle(publication.getName()));
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractCommentsPagesEvent.TYPE, this);
  }

  @UiHandler("newComment")
  void changeNewComment(KeyUpEvent event) {
    if (newComment.getText().isEmpty()) {
      addComment.getElement().addClassName("inactif");
    } else {
      addComment.getElement().removeClassName("inactif");
    }
  }

  @UiHandler("addComment")
  void addComment(ClickEvent event) {
    if (!addComment.getElement().hasClassName("inactif")) {
      Window.alert("Coming soon!");
      //TODO : manage user rights + service call
    }
  }
}