package com.silverpeas.mobile.client.apps.notifications.pages;

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
import com.silverpeas.mobile.client.apps.comments.pages.widgets.Comment;
import com.silverpeas.mobile.client.apps.comments.resources.CommentsMessages;
import com.silverpeas.mobile.client.apps.notifications.events.pages.AbstractNotificationPagesEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.NotificationPagesEventHandler;
import com.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
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
public class NotificationPage extends PageContent implements View, NotificationPagesEventHandler {

  interface NotificationPageUiBinder extends UiBinder<HTMLPanel, NotificationPage> {
  }

  protected NotificationsMessages msg = null;
  private String contentId, contentType, instanceId;

  private static NotificationPageUiBinder uiBinder = GWT.create(NotificationPageUiBinder.class);

  public NotificationPage() {
    msg = GWT.create(NotificationsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractNotificationPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractNotificationPagesEvent.TYPE, this);
  }
}