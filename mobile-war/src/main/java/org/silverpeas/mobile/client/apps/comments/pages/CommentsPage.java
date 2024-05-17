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

package org.silverpeas.mobile.client.apps.comments.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.comments.events.app.AddCommentEvent;
import org.silverpeas.mobile.client.apps.comments.events.app.CommentsLoadEvent;
import org.silverpeas.mobile.client.apps.comments.events.app.DeleteCommentEvent;
import org.silverpeas.mobile.client.apps.comments.events.pages.*;
import org.silverpeas.mobile.client.apps.comments.pages.widgets.Comment;
import org.silverpeas.mobile.client.apps.comments.resources.CommentsMessages;
import org.silverpeas.mobile.client.apps.tasks.pages.widgets.TaskItem;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.components.PopinConfirmation;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.widgets.DeleteButton;
import org.silverpeas.mobile.client.components.base.widgets.EditButton;
import org.silverpeas.mobile.shared.dto.TaskDTO;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: svu
 */
public class CommentsPage extends PageContent implements View, CommentsPagesEventHandler {

  interface CommentsPageUiBinder extends UiBinder<HTMLPanel, CommentsPage> {
  }
  @UiField HTMLPanel container;
  @UiField HeadingElement title;
  @UiField
  UnorderedList commentsList;
  @UiField SpanElement addCommentTitle;
  @UiField Anchor addComment;
  @UiField TextArea newComment;

  private DeleteButton buttonDelete = new DeleteButton();
  private EditButton buttonEdit = new EditButton();
  protected CommentsMessages msg = null;
  private String contentId, contentType, instanceId;
  private List<CommentDTO> comments;

  private static CommentsPageUiBinder uiBinder = GWT.create(CommentsPageUiBinder.class);

  public CommentsPage() {
    msg = GWT.create(CommentsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractCommentsPagesEvent.TYPE, this);
    container.getElement().setId("publication");
    buttonDelete.setId("delete-comment");
    buttonDelete.setCallback(new Command() {
      @Override
      public void execute() {
        PopinConfirmation popin = new PopinConfirmation(msg.deleteComment());
        popin.setYesCallback(new Command() {
          @Override
          public void execute() {
            EventBus.getInstance().fireEvent(new DeleteCommentEvent(getSelectedComment().getComment()));
          }
        });
        popin.show();
      }
    });
    buttonEdit.setCallback(new Command() {
      @Override
      public void execute() {
        //TODO
      }
    });
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
      c.setParent(this);
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
  public void onDeletedComment(CommentDeletedEvent event) {
    comments.remove(event.getComment());
    renderList();
    clearActions();
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

  @Override
  public void setSelectionMode(boolean selectionMode) {
    super.setSelectionMode(selectionMode);
    if (selectionMode) {
      clearActions();
      addActionShortcut(buttonDelete);
      addActionShortcut(buttonEdit);
    } else {
      clearActions();
    }
  }

  private Comment getSelectedComment() {
    for (int i = 0; i < commentsList.getCount(); i++) {
      Comment item = (Comment) commentsList.getWidget(i);
      if (item.isSelected()) {
        return item;
      }
    }
    return null;
  }
}