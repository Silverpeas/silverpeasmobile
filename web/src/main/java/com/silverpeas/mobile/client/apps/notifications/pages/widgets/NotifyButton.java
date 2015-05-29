package com.silverpeas.mobile.client.apps.notifications.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
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
import com.silverpeas.mobile.client.apps.comments.resources.CommentsMessages;
import com.silverpeas.mobile.client.apps.notifications.NotificationsApp;
import com.silverpeas.mobile.client.common.EventBus;

/**
 * @author: svu
 */
public class NotifyButton extends Composite {
    interface NotifyButtonUiBinder extends UiBinder<HTMLPanel, NotifyButton> {
    }

    private static NotifyButtonUiBinder uiBinder = GWT.create(NotifyButtonUiBinder.class);

    @UiField  HTMLPanel container;
    @UiField  Anchor notify;


    public NotifyButton() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("notify")
    void displayNotificationPage(ClickEvent event){
        String contentId = "";
        String instanceId = "";
        String contentType = "";
        String pageTitle = "";
        String contentName = "";
        NotificationsApp app = new NotificationsApp(contentId, instanceId, contentType, pageTitle, contentName);
        app.start();
    }

}