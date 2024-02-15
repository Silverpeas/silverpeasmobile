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

package org.silverpeas.mobile.client.apps.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import org.apache.ecs.html.Div;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.profile.ProfileApp;
import org.silverpeas.mobile.client.apps.profile.events.ProfileEvents;
import org.silverpeas.mobile.client.apps.profile.resources.ProfileMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.events.apps.AppEvent;
import org.silverpeas.mobile.client.components.base.events.page.PageEvent;

public class ProfilePage extends PageContent {

  private static StatusPageUiBinder uiBinder = GWT.create(StatusPageUiBinder.class);

  @UiField(provided = true) protected ProfileMessages msg = null;
  @UiField protected HTMLPanel container;
  @UiField protected TextArea status;
  @UiField protected Anchor publish, changePwd;
  @UiField protected PasswordTextBox pwd1, pwd2;

  @UiField protected DivElement changePwdArea;

  interface StatusPageUiBinder extends UiBinder<Widget, ProfilePage> {
  }

  public ProfilePage() {
    msg = GWT.create(ProfileMessages.class);
    setPageTitle(msg.title().asString());
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("update-statut");
    status.getElement().setAttribute("x-webkit-speech", "x-webkit-speech");
    status.getElement().setAttribute("speech", "speech");
    pwd1.getElement().setAttribute("autocomplete", "new-password");
    pwd2.getElement().setAttribute("autocomplete", "new-password");
    if (!SpMobil.getUser().isLdap()) {
      changePwdArea.getStyle().setDisplay(Style.Display.NONE);
    }
  }

  @UiHandler("publish")
  void publish(ClickEvent event) {
	// send event to apps
    EventBus.getInstance().fireEvent(new AppEvent(this, ProfileEvents.POST.name(), status.getText()));
  }

  @UiHandler("changePwd")
  void changePassword(ClickEvent event) {
    // send event to apps

    if (pwd1.getText().equals(pwd2.getText())) {
      EventBus.getInstance().fireEvent(new AppEvent(this, ProfileEvents.CHANGEPWD.name(), pwd1.getText()));
    } else {
      Notification.alert(msg.pwdNotTheSame());
    }
  }

  @Override
  public void receiveEvent(PageEvent event) {
    if (event.getSender() instanceof ProfileApp && event.getName().equals(ProfileEvents.POSTED.toString())) {
      back();
    }
  }
}