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

package org.silverpeas.mobile.client.apps.comments.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.comments.CommentsApp;
import org.silverpeas.mobile.client.apps.comments.events.pages.AbstractCommentsPagesEvent;
import org.silverpeas.mobile.client.apps.comments.events.pages.CommentAddedEvent;
import org.silverpeas.mobile.client.apps.comments.events.pages.CommentsLoadedEvent;
import org.silverpeas.mobile.client.apps.comments.events.pages.CommentsPagesEventHandler;
import org.silverpeas.mobile.client.apps.comments.resources.CommentsMessages;
import org.silverpeas.mobile.client.common.EventBus;

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