package com.silverpeas.mobile.client.apps.notifications.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.apps.notifications.NotificationsApp;
import com.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import com.silverpeas.mobile.client.components.base.ActionItem;

/**
 * @author: svu
 */
public class NotifyButton extends ActionItem {
    interface NotifyButtonUiBinder extends UiBinder<HTMLPanel, NotifyButton> {
    }

    private static NotifyButtonUiBinder uiBinder = GWT.create(NotifyButtonUiBinder.class);

    @UiField  HTMLPanel container;
    @UiField  Anchor notify;

    @UiField(provided = true) protected NotificationsMessages msg = null;
    private String instanceId, contentId, contentType, title, pageTitle;


    public NotifyButton() {
        msg = GWT.create(NotificationsMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
        setId("notify");
    }

    public void init(String instanceId, String contentId, String contentType, String title, String pageTitle) {
        this.instanceId = instanceId;
        this.contentId = contentId;
        this.contentType = contentType;
        this.title = title;
        this.pageTitle = pageTitle;
    }

    @UiHandler("notify")
    void displayNotificationPage(ClickEvent event){
        NotificationsApp app = new NotificationsApp(instanceId, contentId, contentType, title, pageTitle);
        app.start();

        // hide menu
        getElement().getParentElement().removeAttribute("style");

    }

}