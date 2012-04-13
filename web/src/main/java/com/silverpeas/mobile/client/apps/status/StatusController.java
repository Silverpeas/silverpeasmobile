/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.client.apps.status;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.status.events.controller.AbstractStatusControllerEvent;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusControllerEventHandler;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusLoadEvent;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusPostEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusLoadedEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.StatusDTO;

public class StatusController implements Controller, StatusControllerEventHandler {

  public StatusController() {
    super();
    EventBus.getInstance().addHandler(AbstractStatusControllerEvent.TYPE, this);
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractStatusControllerEvent.TYPE, this);
  }

  @Override
  public void loadStatus(final StatusLoadEvent event) {
    Notification.activityStart();
    ServicesLocator.serviceRSE.getStatus(event.getCurrentPage(),
        new AsyncCallback<List<StatusDTO>>() {
          public void onFailure(Throwable caught) {
            EventBus.getInstance().fireEvent(new ErrorEvent(caught));
          }

          public void onSuccess(List<StatusDTO> result) {
            EventBus.getInstance().fireEvent(new StatusLoadedEvent(result));
            Notification.activityStop();
          }
                });
  }

  @Override
  public void postStatus(StatusPostEvent event) {
    if (event.getPostStatus() != null && event.getPostStatus().length() > 0) {
      ServicesLocator.serviceRSE.updateStatus(event.getPostStatus(), new AsyncCallback<String>() {
          public void onFailure(Throwable caught) {
          EventBus.getInstance().fireEvent(new ErrorEvent(caught));
          }

          public void onSuccess(String result) {
          StatusDTO status = new StatusDTO();
          status.setCreationDate(new Date());
          status.setDescription(result);
          EventBus.getInstance().fireEvent(new StatusPostedEvent(status));
          }
                });
    }
  }
}
