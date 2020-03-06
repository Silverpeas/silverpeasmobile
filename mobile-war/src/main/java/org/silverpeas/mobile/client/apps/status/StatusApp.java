/*
 * Copyright (C) 2000 - 2019 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.apps.status;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.status.events.StatusEvents;
import org.silverpeas.mobile.client.apps.status.pages.StatusPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.base.events.apps.AppEvent;
import org.silverpeas.mobile.client.components.base.events.page.PageEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.StatusDTO;
import org.silverpeas.mobile.shared.dto.password.PasswordControlDTO;
import org.silverpeas.mobile.shared.dto.password.PasswordDTO;

import java.util.Date;

public class StatusApp extends App {

    private static ApplicationMessages msg;

    public StatusApp(){
        super();
        msg = GWT.create(ApplicationMessages.class);
    }

    public void start(){
        setMainPage(new StatusPage());
        super.start();
    }

    @Override
    public void receiveEvent(AppEvent event) {
        if (event.getSender() instanceof StatusPage) {
            if (event.getName().equals(StatusEvents.POST.toString())) {
                final String postStatus = (String) event.getData();
                if (postStatus != null && postStatus.length() > 0) {
                    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<String>() {

                        @Override
                        public void attempt() {
                            ServicesLocator.getServiceRSE().updateStatus(postStatus, this);
                        }

                        public void onSuccess(String result) {
                            super.onSuccess(result);
                            StatusDTO status = new StatusDTO();
                            status.setCreationDate(new Date());
                            status.setDescription(result);
                            EventBus.getInstance().fireEvent(new PageEvent(StatusApp.this, StatusEvents.POSTED.toString(), status));
                        }
                    };
                    action.attempt();
                }
            } else if (event.getName().equals(StatusEvents.CHANGEPWD.toString())) {
              final String pwd = (String) event.getData();
              PasswordDTO dto = new PasswordDTO();
              dto.setValue(pwd);
              ServicesLocator.getServicePassword().checking(dto, new MethodCallback<PasswordControlDTO>() {
                @Override
                public void onFailure(final Method method, final Throwable throwable) {
                  EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
                }

                @Override
                public void onSuccess(final Method method, final PasswordControlDTO passwordControlDTO) {
                    if (passwordControlDTO.isCorrect()) {
                      AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<Void>() {
                        @Override
                        public void attempt() {
                          ServicesLocator.getServiceConnection().changePwd(pwd, this);
                        }

                        public void onSuccess(Void result) {
                          super.onSuccess(result);
                          EventBus.getInstance().fireEvent(new PageEvent(StatusApp.this, StatusEvents.POSTED.toString(), ""));
                        }
                      };
                      action.attempt();
                    } else{
                      Notification.alert(msg.pwdNotValid());
                    }
                }
              });
            }
        }
    }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {

  }
}
