/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.notifications.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.notifications.NotificationsApp;
import org.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import org.silverpeas.mobile.client.components.base.ActionItem;

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