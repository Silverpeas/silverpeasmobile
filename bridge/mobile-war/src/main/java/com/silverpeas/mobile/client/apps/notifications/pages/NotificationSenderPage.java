package com.silverpeas.mobile.client.apps.notifications.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.apps.notifications.events.app.SendNotificationEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.AbstractNotificationPagesEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.AllowedUsersAndGroupsLoadedEvent;
import com.silverpeas.mobile.client.apps.notifications.events.pages.NotificationPagesEventHandler;
import com.silverpeas.mobile.client.apps.notifications.events.pages.NotificationSendedEvent;
import com.silverpeas.mobile.client.apps.notifications.resources.NotificationsMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.Popin;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.GroupDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: svu
 */
public class NotificationSenderPage extends PageContent implements View, NotificationPagesEventHandler {

    interface NotificationSenderPageUiBinder extends UiBinder<HTMLPanel, NotificationSenderPage> {}

    private static NotificationSenderPageUiBinder uiBinder = GWT.create(NotificationSenderPageUiBinder.class);

    @UiField protected HTMLPanel container;
    @UiField(provided = true) protected NotificationsMessages msg = null;
    @UiField protected Anchor modify, send;
    @UiField TextArea message;
    @UiField TextBox subject;

    List<BaseDTO> selection = new ArrayList<BaseDTO>();

    public NotificationSenderPage() {

        msg = GWT.create(NotificationsMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
        EventBus.getInstance().addHandler(AbstractNotificationPagesEvent.TYPE, this);
        container.getElement().setId("edit-notification");
        message.getElement().setId("message");
        subject.getElement().setId("subject");

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
             @Override
             public void execute() {

                 Element destinataires = Document.get().getElementById("destinataires");


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

                 destinataires.setInnerHTML(dest);

                 subject.setText(getTitle());
             }
         }
        );
    }

    public void setSelection(List<BaseDTO> selection) {
        this.selection = selection;
    }

    @UiHandler("modify")
    protected void modify(ClickEvent event) {
        back();
    }

    @UiHandler("send")
    protected void sendNotification(ClickEvent event) {
        NotificationDTO notification = new NotificationDTO(message.getText());
        EventBus.getInstance().fireEvent(new SendNotificationEvent(notification, selection, subject.getText()));
    }

    @Override
    public void onAllowedUsersAndGroupsLoaded(AllowedUsersAndGroupsLoadedEvent allowedUsersAndGroupsLoadedEvent) {}

    @Override
    public void onNotificationSended(NotificationSendedEvent event) {
        back();
        back();
        new Popin(msg.sended()).show();
    }

    @Override
    public void stop() {
        super.stop();
        EventBus.getInstance().removeHandler(AbstractNotificationPagesEvent.TYPE, this);
    }
}