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

package org.silverpeas.mobile.client.apps.media.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.media.events.app.MediasLoadMediaItemsEvent;
import org.silverpeas.mobile.client.apps.media.resources.MediaMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.components.base.ActionItem;
import org.silverpeas.mobile.client.resources.ApplicationMessages;


/**
 * @author: svu
 */
public class AddMediaButton extends ActionItem {
    interface AddMediaButtonUiBinder extends UiBinder<Widget, AddMediaButton> {
    }

    @UiField FileUpload file;
    @UiField Anchor link;
    @UiField(provided = true) protected MediaMessages msg = null;


    private String instanceIdValue, albumIdValue;
    private ApplicationMessages globalMsg = GWT.create(ApplicationMessages.class);

    private static AddMediaButtonUiBinder uiBinder = GWT.create(AddMediaButtonUiBinder.class);

    public AddMediaButton() {
        msg = GWT.create(MediaMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
        file.getElement().setAttribute("accept", "audio/*, video/*, image/*");
        file.getElement().setAttribute("multiple", "multiple");
        file.getElement().setAttribute("length", "40");
        file.getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    public void init(String instanceId, String albumId) {
        this.instanceIdValue = instanceId;
        this.albumIdValue = albumId;
    }

    @UiHandler("file")
    void upload(ChangeEvent event) {
      Notification.activityStartImmediately();
      String url = UrlUtils.getUploadLocation();
      url +=  "MediaAction";
      upload(this, file.getElement(), instanceIdValue, albumIdValue, url, SpMobil.getUserToken());
    }

    public void mediaUploadedSuccessfully() {
      EventBus.getInstance().fireEvent(new MediasLoadMediaItemsEvent(instanceIdValue, albumIdValue));
    }

    public void mediaNotUploaded(int codeError) {
      GWT.log("error " + codeError);
      if (codeError == 413) {
        Notification.alert(msg.maxUploadError());
      } else if (codeError == 415) {
        Notification.alert(msg.mediaNotSupportedError());
      }
      Notification.activityStop();
    }

    @UiHandler("link")
    void upload(ClickEvent event) {
        if (NetworkHelper.isOnline()) {
            clickOnInputFile(file.getElement());
        } else {
            Notification.alert(globalMsg.needToBeOnline());
        }
    }

    private static native void upload(AddMediaButton button, Element input, String componentId, String albumId, String url, String token) /*-{
      var xhr = new XMLHttpRequest();
      var fd = new FormData();
      xhr.open("POST", url, false);
      xhr.setRequestHeader("X-Silverpeas-Session", token);
      xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
          // Every thing ok, file uploaded
          button.@org.silverpeas.mobile.client.apps.media.pages.widgets.AddMediaButton::mediaUploadedSuccessfully()();
        } else {
          button.@org.silverpeas.mobile.client.apps.media.pages.widgets.AddMediaButton::mediaNotUploaded(I)(xhr.status);
        }
      };
      fd.append("componentId", componentId);
      fd.append("albumId", albumId);
      for(var i = 0; i < input.files.length ; i++) {
        fd.append("upload_file"+i, input.files[i]);
      }
      xhr.send(fd);
    }-*/;

    private static native void clickOnInputFile(Element elem) /*-{
        elem.click();
    }-*/;

}