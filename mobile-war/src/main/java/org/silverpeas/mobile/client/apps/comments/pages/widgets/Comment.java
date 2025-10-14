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

package org.silverpeas.mobile.client.apps.comments.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.tasks.pages.TaskPage;
import org.silverpeas.mobile.client.components.base.widgets.SelectableItem;
import org.silverpeas.mobile.shared.dto.RightDTO;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;

/**
 * @author: svu
 */
public class Comment extends SelectableItem {
  interface CommentUiBinder extends UiBinder<HTMLPanel, Comment> {
  }

  @UiField HTMLPanel container;
  @UiField Anchor link;
  @UiField SpanElement date, userName;
  @UiField ParagraphElement content;
  @UiField
  ImageElement avatar;

  private CommentDTO comment;

  private static CommentUiBinder uiBinder = GWT.create(CommentUiBinder.class);

  private RightDTO right;

  public Comment() {
    uiBinder.createAndBindUi(this);
    initWidget(uiBinder.createAndBindUi(this));
    setMultiSelection(false);
    setContainer(container);
  }

  public void setRight(RightDTO right) {
    this.right = right;
  }

  public void setComment(final CommentDTO comment) {
    this.comment = comment;
    render();
  }

  public CommentDTO getComment() {
    return comment;
  }

  private void render() {
    content.setInnerHTML(comment.getTextForHtml());

    if (comment.getModificationDate().isEmpty()) {
      date.setInnerHTML(" - " + comment.getCreationDate());
    } else {
      date.setInnerHTML(" - " + comment.getModificationDate());
    }
    userName.setInnerHTML(comment.getAuthor().getFullName());
    avatar.setSrc(comment.getAuthor().getAvatar());
  }

  @UiHandler("link")
  protected void startTouch(TouchStartEvent event) {
    if (right.getManager()) {
      startTouch(event, true);
    } else {
      startTouch(event, comment.getAuthor().getId().equals(SpMobil.getUser().getId()));
    }
  }

  @UiHandler("link")
  protected void endTouch(TouchEndEvent event) {
    endTouch(event, true, null);
  }
}