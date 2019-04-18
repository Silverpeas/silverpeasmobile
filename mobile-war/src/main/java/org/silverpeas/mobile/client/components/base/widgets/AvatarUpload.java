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

package org.silverpeas.mobile.client.components.base.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.network.OfflineHelper;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;


/**
 * @author: svu
 */
public class AvatarUpload extends Composite {


  interface AddAvatarButtonUiBinder extends UiBinder<Widget, AvatarUpload> {}

  @UiField
  FileUpload file;
  @UiField
  Image avatar;

  private ApplicationMessages msg = GWT.create(ApplicationMessages.class);

  private static AddAvatarButtonUiBinder uiBinder = GWT.create(AddAvatarButtonUiBinder.class);

  public AvatarUpload() {
    initWidget(uiBinder.createAndBindUi(this));
    file.getElement().setAttribute("accept", "image/*");
    file.getElement().setAttribute("multiple", "multiple");
    file.getElement().setAttribute("length", "40");
    file.getElement().getStyle().setDisplay(Style.Display.NONE);
  }

  @UiHandler("file")
  void upload(ChangeEvent event) {
    Notification.activityStartImmediately();
    String url = UrlUtils.getUploadLocation();
    url += "AvatarAction";
    upload(this, file.getElement(), url, SpMobil.getUserToken());
  }

  public void avatarUploadedSuccessfully(String avatarData) {
    Notification.activityStop();
    avatar.setUrl(avatarData);
    AuthentificationManager.getInstance().updateAvatarInCache(avatarData);
  }

  public void avatarNotUploaded(int codeError) {
    GWT.log("error " + codeError);
    if (codeError == 413) {
      Notification.alert(msg.maxUploadError());
    } else if (codeError == 415) {
      Notification.alert(msg.mediaNotSupportedError());
    } else if (codeError == 500) {
      Notification.alert(msg.systemError());
    }
    Notification.activityStop();
  }

  @UiHandler("avatar")
  void upload(ClickEvent event) {
    if (OfflineHelper.isOffLine() == false) {
      clickOnInputFile(file.getElement());
    } else {
      Notification.alert(msg.needToBeOnline());
    }

  }

  public void setUser(final DetailUserDTO currentUser, ApplicationResources resources) {
    if (currentUser.getAvatar().isEmpty()) {
      avatar.setResource(resources.avatar());
      avatar.getElement().removeAttribute("height");
      avatar.getElement().removeAttribute("width");

    } else {
      avatar.setUrl(currentUser.getAvatar());
    }
  }

  private static native void upload(AvatarUpload button, Element input, String url, String token) /*-{
    var xhr = new XMLHttpRequest();
    var fd = new FormData();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("X-Silverpeas-Session", token);
    xhr.onreadystatechange = function() {
      if (xhr.readyState == 4 && xhr.status == 200) {
        // Every thing ok, file uploaded
        var avatar = xhr.getResponseHeader("avatar");
        button.@org.silverpeas.mobile.client.components.base.widgets.AvatarUpload::avatarUploadedSuccessfully(Ljava/lang/String;)(avatar);
      } else {
        button.@org.silverpeas.mobile.client.components.base.widgets.AvatarUpload::avatarNotUploaded(I)(xhr.status);
      }
    };
    for (var i = 0; i < input.files.length; i++) {
      fd.append("upload_file" + i, input.files[i]);
    }
    xhr.send(fd);
  }-*/;

  private static native void clickOnInputFile(Element elem) /*-{
    elem.click();
  }-*/;

}