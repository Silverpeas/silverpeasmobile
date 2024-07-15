/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

package org.silverpeas.mobile.client.apps.notifications.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.notifications.events.app.SendNotificationEvent;
import org.silverpeas.mobile.client.apps.notifications.events.pages.AbstractNotificationPagesEvent;
import org.silverpeas.mobile.client.apps.notifications.events.pages.NotificationPagesEventHandler;
import org.silverpeas.mobile.client.apps.notifications.events.pages.NotificationSendedEvent;
import org.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.components.Snackbar;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: svu
 */
public class NotificationSenderPage extends PageContent implements View, NotificationPagesEventHandler {

    interface NotificationSenderPageUiBinder extends UiBinder<HTMLPanel, NotificationSenderPage> {}

    private static NotificationSenderPageUiBinder uiBinder = GWT.create(NotificationSenderPageUiBinder.class);
    private ApplicationResources resources = GWT.create(ApplicationResources.class);

    @UiField protected HTMLPanel container;
    @UiField(provided = true) protected NotificationsMessages msg = null;
    @UiField protected Anchor send;
    @UiField TextArea message;
    @UiField TextBox subject;
    @UiField InlineHTML iconFile, iconSelection;
    @UiField HTML destinataires;

    List<BaseDTO> selection = new ArrayList<BaseDTO>();

    public NotificationSenderPage() {

        msg = GWT.create(NotificationsMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
        EventBus.getInstance().addHandler(AbstractNotificationPagesEvent.TYPE, this);
        container.getElement().setId("edit-notification");
        message.getElement().setId("message");
        subject.getElement().setId("subject");
        iconFile.setHTML(resources.publication().getText());

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
             @Override
             public void execute() {

                 //Element destinataires = Document.get().getElementById("destinataires");


                 String dest = "";
                 for (BaseDTO sel : selection) {
                     if (sel instanceof UserDTO) {
                         dest += ((UserDTO) sel).getFirstName() + " " + ((UserDTO) sel).getLastName() + ", ";
                     } else if(sel instanceof GroupDTO) {
                         dest += ((GroupDTO) sel).getName() + ", ";
                     }
                 }
                 dest = dest.substring(0, dest.length() - 2);
                 dest += "<div id=\"nb-user-sectionne\" class=\"nb-user-sectionne\">"+"(" + selection.size() + ")"+"</div>";

                 destinataires.setHTML(dest);
                 iconSelection.setHTML(resources.peoples().getText());
                 subject.setText(getTitle());
             }
         }
        );
    }

    public void setSelection(List<BaseDTO> selection) {
        this.selection = selection;
    }

    @UiHandler("send")
    protected void sendNotification(ClickEvent event) {
        NotificationDTO notification = new NotificationDTO(message.getText());
        EventBus.getInstance().fireEvent(new SendNotificationEvent(notification, selection, subject.getText()));
    }

    @Override
    public void onNotificationSended(NotificationSendedEvent event) {
        back();
        back();
        Snackbar.show(msg.sended(), Snackbar.DELAY, Snackbar.INFO);
    }

    @Override
    public void stop() {
        super.stop();
        EventBus.getInstance().removeHandler(AbstractNotificationPagesEvent.TYPE, this);
    }
}