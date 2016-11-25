package com.silverpeas.mobile.client.apps.comments.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.apps.comments.CommentsApp;
import com.silverpeas.mobile.client.apps.comments.events.pages.AbstractCommentsPagesEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentAddedEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentsLoadedEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentsPagesEventHandler;
import com.silverpeas.mobile.client.apps.comments.resources.CommentsMessages;
import com.silverpeas.mobile.client.common.EventBus;

/**
 * @author: svu
 */
public class CommentsButton extends Composite implements CommentsPagesEventHandler {

  interface CommentsButtonUiBinder extends UiBinder<HTMLPanel, CommentsButton> {
  }

  @UiField HTMLPanel container;
  @UiField Anchor comments;

  protected CommentsMessages msg = null;

  private String contentId, contentType, contentName, instanceId, pageTitle;
  private int commentNumber;

  private static CommentsButtonUiBinder uiBinder = GWT.create(CommentsButtonUiBinder.class);

  public CommentsButton() {
    msg = GWT.create(CommentsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("comments");
    EventBus.getInstance().addHandler(AbstractCommentsPagesEvent.TYPE, this);
    render();
  }

  public void init(String contentId, String instanceId, String contentType, String pageTitle, String contentName, int commentsNumber) {
    this.contentId = contentId;
    this.contentType = contentType;
    this.contentName = contentName;
    this.instanceId = instanceId;
    this.pageTitle = pageTitle;
    this.commentNumber = commentsNumber;
    render();
  }


  @UiHandler("comments")
  void displayComments(ClickEvent event){
    CommentsApp commentsApp = new CommentsApp(contentId, instanceId, contentType, pageTitle, contentName);
    commentsApp.start();
  }

  private void render() {
    if (commentNumber == 0) {
      comments.setText(msg.noComment());
    } else if (commentNumber == 1) {
      comments.setText(msg.comment());
    } else {
      comments.setText(msg.comments(String.valueOf(commentNumber)));
    }
  }

  @Override
  public void onLoadedComments(final CommentsLoadedEvent event) {
  }

  @Override
  public void onAddedComment(final CommentAddedEvent event) {
    this.commentNumber++;
    render();
  }

  public void stop() {
    EventBus.getInstance().removeHandler(AbstractCommentsPagesEvent.TYPE, this);
  }
}