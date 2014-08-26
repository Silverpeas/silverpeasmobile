package com.silverpeas.mobile.client.apps.documents.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.silverpeas.mobile.shared.dto.documents.CommentDTO;

/**
 * @author: svu
 */
public class Comment extends Composite {
  interface CommentUiBinder extends UiBinder<HTMLPanel, Comment> {
  }

  @UiField SpanElement date, userName;
  @UiField ParagraphElement content;
  @UiField Image avatar;

  private CommentDTO comment;

  private static CommentUiBinder uiBinder = GWT.create(CommentUiBinder.class);

  public Comment() {
    uiBinder.createAndBindUi(this);
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setComment(final CommentDTO comment) {
    this.comment = comment;
    render();
  }

  private void render() {
    content.setInnerHTML(comment.getContent());
    date.setInnerHTML(" - " + comment.getDate());
    userName.setInnerHTML(comment.getUserName());
    avatar.setUrl(comment.getAvatar());
  }
}