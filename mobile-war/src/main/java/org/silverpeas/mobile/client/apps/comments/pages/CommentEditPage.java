/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.client.apps.comments.pages;

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
import org.silverpeas.mobile.client.apps.comments.events.app.UpdateCommentEvent;
import org.silverpeas.mobile.client.apps.comments.resources.CommentsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;

/**
 * @author: svu
 */
public class CommentEditPage extends PageContent implements View {

  private CommentDTO comment;

  interface CommentsPageUiBinder extends UiBinder<HTMLPanel, CommentEditPage> {
  }
  @UiField HTMLPanel container;
  @UiField HeadingElement title;
  @UiField SpanElement addCommentTitle;
  @UiField Anchor saveComment;
  @UiField TextArea newComment;

  protected CommentsMessages msg = null;
  private String contentId, contentType, instanceId;

  private static CommentsPageUiBinder uiBinder = GWT.create(CommentsPageUiBinder.class);

  public CommentEditPage() {
    msg = GWT.create(CommentsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("publication");
  }

  public void setTitle(String title) {
    this.title.setInnerText(msg.commentsPageTitle(title));
  }

  @UiHandler("newComment")
  void changeNewComment(KeyUpEvent event) {
    if (newComment.getText().isEmpty()) {
      saveComment.getElement().addClassName("inactif");
    } else {
      saveComment.getElement().removeClassName("inactif");
    }
  }

  @UiHandler("saveComment")
  void addComment(ClickEvent event) {
    if (!saveComment.getElement().hasClassName("inactif")) {
      comment.setText(newComment.getText());
      EventBus.getInstance().fireEvent(new UpdateCommentEvent(comment));
      back();
    }
  }

  public void setComment(CommentDTO comment) {
    this.comment = comment;
    newComment.setText(comment.getText());
  }
}