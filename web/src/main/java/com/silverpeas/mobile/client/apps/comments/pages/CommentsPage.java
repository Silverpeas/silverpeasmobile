package com.silverpeas.mobile.client.apps.comments.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.silverpeas.mobile.client.apps.comments.events.app.AddCommentEvent;
import com.silverpeas.mobile.client.apps.comments.events.app.CommentsLoadEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.AbstractCommentsPagesEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentAddedEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentsLoadedEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentsPagesEventHandler;
import com.silverpeas.mobile.client.apps.comments.resources.CommentsMessages;
import com.silverpeas.mobile.client.apps.comments.pages.widgets.Comment;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;

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

  protected CommentsMessages msg = null;
  private String contentId, contentType, instanceId;
  private List<CommentDTO> comments;

  private static CommentsPageUiBinder uiBinder = GWT.create(CommentsPageUiBinder.class);

  public CommentsPage() {
    msg = GWT.create(CommentsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractCommentsPagesEvent.TYPE, this);
    container.getElement().setId("publication");
    addCommentTitle.setInnerHTML(msg.addComment());
  }

  public void setContentInfos(final String contentId, final String instanceId, final String contentType) {
    this.contentId = contentId;
    this.contentType = contentType;
    this.instanceId = instanceId;
    // send event to controler for retrieve comments infos
    Notification.activityStart();
    EventBus.getInstance().fireEvent(new CommentsLoadEvent(contentId, contentType));
  }

  public void setTitle(String title) {
    this.title.setInnerText(msg.commentsPageTitle(title));
  }

  @Override
  public void onLoadedComments(final CommentsLoadedEvent event) {
    Notification.activityStop();
    this.comments = event.getComments();
    renderList();
  }

  private void renderList() {
    commentsList.clear();
    for (CommentDTO comment : comments) {
      Comment c = new Comment();
      c.setComment(comment);
      commentsList.add(c);
    }
  }

  @Override
  public void onAddedComment(final CommentAddedEvent event) {
    comments.add(0, event.getComment());
    renderList();
    newComment.setText("");
    addComment.getElement().addClassName("inactif");
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
      EventBus.getInstance().fireEvent(new AddCommentEvent(contentId, instanceId, contentType, newComment.getText()));
    }
  }
}