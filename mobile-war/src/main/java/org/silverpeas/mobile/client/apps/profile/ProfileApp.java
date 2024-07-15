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

package org.silverpeas.mobile.client.apps.profile;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.TextCallback;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.profile.events.ProfileEvents;
import org.silverpeas.mobile.client.apps.profile.pages.ProfilePage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.components.base.events.apps.AppEvent;
import org.silverpeas.mobile.client.components.base.events.page.PageEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.StatusDTO;
import org.silverpeas.mobile.shared.dto.password.PasswordControlDTO;
import org.silverpeas.mobile.shared.dto.password.PasswordDTO;

import java.util.Date;

public class ProfileApp extends App {

    private static ApplicationMessages msg;

    public ProfileApp(){
        super();
        msg = GWT.create(ApplicationMessages.class);
    }

    public void start(){
        setMainPage(new ProfilePage());
        super.start();
    }

    @Override
    public void receiveEvent(AppEvent event) {
        if (event.getSender() instanceof ProfilePage) {
            if (event.getName().equals(ProfileEvents.POST.toString())) {
                final String postStatus = (String) event.getData();
                if (postStatus != null && postStatus.length() > 0) {
                  TextCallback action = new TextCallback() {
                    @Override
                    public void onFailure(final Method method, final Throwable t) {
                      if (NetworkHelper.isOnline()) {
                        EventBus.getInstance().fireEvent(new ErrorEvent(t));
                      } else {
                        Notification.alert(msg.needToBeOnline());
                      }
                    }

                    @Override
                    public void onSuccess(final Method method, final String s) {
                      Notification.activityStop();
                      StatusDTO status = new StatusDTO();
                      status.setCreationDate(new Date());
                      status.setDescription(s);
                      EventBus.getInstance().fireEvent(new PageEvent(ProfileApp.this, ProfileEvents.POSTED.toString(), status));
                    }
                  };
                  if (NetworkHelper.isOnline()) {
                    ServicesLocator.getServiceRSE().updateStatus(postStatus, action);
                  } else {
                    Notification.alert(msg.needToBeOnline());
                  }
                }
            } else if (event.getName().equals(ProfileEvents.CHANGEPWD.toString())) {
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
                      MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
                        @Override
                        public void attempt() {
                          super.attempt();
                          ServicesLocator.getServiceConnection().changePwd(pwd, this);
                        }

                        @Override
                        public void onSuccess(final Method method, final Void unused) {
                          super.onSuccess(method, unused);
                          EventBus.getInstance().fireEvent(new PageEvent(ProfileApp.this, ProfileEvents.POSTED.toString(), ""));
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
