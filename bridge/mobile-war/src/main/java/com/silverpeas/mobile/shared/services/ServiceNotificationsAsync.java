package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

import java.util.List;

public interface ServiceNotificationsAsync {

    void send(NotificationDTO notification, List<BaseDTO> receivers, String subject, AsyncCallback<Void> async);

    void getAllowedUsersAndGroups(String componentId, String contentId, AsyncCallback<List<BaseDTO>> async);
}
