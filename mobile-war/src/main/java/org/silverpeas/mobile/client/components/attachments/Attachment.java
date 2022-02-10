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

package org.silverpeas.mobile.client.components.attachments;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.storage.CacheStorageHelper;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.documents.SimpleDocumentDTO;

public class Attachment extends Composite {

  private static AttachmentUiBinder uiBinder = GWT.create(AttachmentUiBinder.class);
  @UiField
  Anchor link;
  @UiField
  SpanElement size, name, description;
  @UiField
  ImageElement icon;

  protected DocumentsResources ressources = null;
  private DocumentsMessages msg = null;
  private ApplicationMessages globalMsg = null;
  private SimpleDocumentDTO data = null;

  interface AttachmentUiBinder extends UiBinder<Widget, Attachment> {}

  public Attachment() {
    msg = GWT.create(DocumentsMessages.class);
    globalMsg = GWT.create(ApplicationMessages.class);
    ressources = GWT.create(DocumentsResources.class);
    ressources.css().ensureInjected();
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setAttachment(SimpleDocumentDTO data) {
    this.data = data;
    render();
  }

  private void render() {
    Image img = null;
    String sizeValue;
      /*if (!data..isDownloadAllowed()) {
        link.setStylePrimaryName("not-downloadable");
      }*/
    if (data.getSize() < 1024 * 1024) {
      sizeValue = String.valueOf(data.getSize() / 1024);
      size.setInnerHTML(msg.sizeK(sizeValue));
    } else {
      sizeValue = String.valueOf(data.getSize() / (1024 * 1024));
      size.setInnerHTML(msg.sizeM(sizeValue));
    }
    String title = data.getFileName();
    if (data.getTitle() != null && !data.getTitle().isEmpty()) {
      title = data.getTitle();
    }
    name.setInnerHTML(title);
    if (data.getDescription() != null && !data.getDescription().isEmpty()){
      description.setInnerHTML("<br/>"+data.getDescription());
    } else {
      description.getStyle().setDisplay(Style.Display.NONE);
    }

    if (data.getContentType().contains("msword")) {
      img = new Image(ressources.msword());
    } else if (data.getContentType().contains("sheet")) {
      img = new Image(ressources.msexcel());
    } else if (data.getContentType().contains("pdf")) {
      img = new Image(ressources.pdf());
    } else if (data.getContentType().contains("image")) {
      img = new Image(ressources.image());
    } else if (data.getContentType().contains("presentation")) {
      img = new Image(ressources.mspowerpoint());
    } else {
      img = new Image(ressources.unknown());
    }
    icon.getParentElement().replaceChild(img.getElement(), icon);


    // link generation
    try {
      String url = UrlUtils.getAttachedFileLocation();
      url += "componentId/";
      url += data.getInstanceId();
      url += "/attachmentId/";
      url += data.getId();
      url += "/lang/";
      url += data.getLang();
      url += "/name/";
      url += data.getFileName();

      link.setHref(url);
      if (MobilUtils.isIOS()) {
        //link.setTarget("_blank");
        link.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(final ClickEvent clickEvent) {
            String u = ((Anchor) clickEvent.getSource()).getHref();
            CacheStorageHelper.store(u);
            Window.open(u, "_blank", "fullscreen=yes");
          }
        });
      } else {
        link.setTarget("_self");
        link.getElement().setAttribute("download", data.getFileName());
        link.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(final ClickEvent clickEvent) {
            CacheStorageHelper.store(((Anchor)clickEvent.getSource()).getHref());
          }
        });
      }
    } catch (JavaScriptException e) {
      Notification.alert(e.getMessage());
    }

  }
}
