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

package org.silverpeas.mobile.client.apps.formsonline.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormsOnlineAsReceiverLoadEvent;
import org.silverpeas.mobile.client.apps.formsonline.resources.FormsOnlineMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.ActionItem;
import org.silverpeas.mobile.client.components.base.ActionsMenu;

/**
 * @author: svu
 */
public class ViewRequestsToValidateButton extends ActionItem {
    interface MyRequestsButtonUiBinder extends UiBinder<HTMLPanel, ViewRequestsToValidateButton> {
    }

    private static MyRequestsButtonUiBinder uiBinder = GWT.create(MyRequestsButtonUiBinder.class);

    @UiField  HTMLPanel container;
    @UiField  Anchor viewRequests;

    @UiField(provided = true) protected FormsOnlineMessages msg = null;


    public ViewRequestsToValidateButton() {
        msg = GWT.create(FormsOnlineMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
        setId("requestsToValidate");
    }

    @UiHandler("viewRequests")
    void displayNotificationPage(ClickEvent event){
        EventBus.getInstance().fireEvent(new FormsOnlineAsReceiverLoadEvent());
        // hide menu
        ActionsMenu.close(getElement());
    }

}