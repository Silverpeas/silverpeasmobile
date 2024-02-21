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
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.documents.pages.SharingPage;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import org.silverpeas.mobile.client.apps.notifications.NotificationsApp;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.common.storage.CacheStorageHelper;
import org.silverpeas.mobile.client.components.IframePage;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.documents.SimpleDocumentDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

public class Attachment extends Composite {

  private static AttachmentUiBinder uiBinder = GWT.create(AttachmentUiBinder.class);
  @UiField
  Anchor link, download;
  @UiField
  SpanElement size, name, description;
  @UiField
  ImageElement icon;

  @UiField
  HTMLPanel operations;

  @UiField
  HTML share, view, notify;
  private DocumentsMessages msg = null;
  private ApplicationMessages globalMsg = null;
  private SimpleDocumentDTO data = null;

  private int sharing;
  private boolean notifiable;

  public void setNotifiable(boolean notifiable) {
    this.notifiable = notifiable;
    if (!notifiable) {
      notify.setVisible(false);
    }
  }

  interface AttachmentUiBinder extends UiBinder<Widget, Attachment> {}

  public Attachment() {
    msg = GWT.create(DocumentsMessages.class);
    globalMsg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setSharing(int sharing) {
    this.sharing = sharing;
    if (sharing == 0) {
      share.setVisible(false);
    }
  }
  public void setAttachment(SimpleDocumentDTO data) {
    this.data = data;
    render();
  }

  private boolean isViewable() {
    boolean v = (data.getContentType().contains("msword") || data.getContentType().contains("word"));
    v = v || data.getContentType().contains("excel");
    v = v || data.getContentType().contains("pdf");
    v = v || data.getContentType().contains("presentationml");
    v = v || data.getContentType().contains("opendocument.text");
    v = v || data.getContentType().contains("opendocument.spreadsheet");
    v = v || data.getContentType().contains("opendocument.presentation");
    return v;
  }

  private void render() {
    operations.getElement().setId("operations");
    view.getElement().setId("view");
    share.getElement().setId("share");
    download.getElement().setId("download");
    notify.getElement().setId("notify");

    Image img = null;
    String sizeValue;
    if (!data.isDownloadable()) {
      download.setVisible(false);
    }
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

    if (data.getContentType().contains("msword") || data.getContentType().contains("word")) {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-word.svg");
    } else if (data.getContentType().contains("excel")) {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-excel.svg");
    } else if (data.getContentType().contains("pdf")) {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-pdf.svg");
    } else if (data.getContentType().contains("image")) {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-image.svg");
    } else if (data.getContentType().contains("presentationml")) {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-powerpoint.svg");
    } else if (data.getContentType().contains("zip")) {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-zip.svg");
    } else if (data.getContentType().contains("opendocument.text")) {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-writer.svg");
    } else if (data.getContentType().contains("opendocument.spreadsheet")) {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-calc.svg");
    } else if (data.getContentType().contains("opendocument.presentation")) {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-presentation.svg");
    } else {
      icon.setSrc(NetworkHelper.getContext() + "icons/files/file-type-unknown.svg");
    }

    // link generation
    try {
      String url = UrlUtils.getAttachedFileLocation();
      if (data.isDownloadable()) {
        url += "componentId/";
        url += data.getInstanceId();
        url += "/attachmentId/";
        url += data.getId();
        url += "/lang/";
        url += data.getLang();
        url += "/name/";
        url += data.getFileName();
      } else {
        url = "#";
      }
      download.setHref(url);
      link.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent clickEvent) {
          toogleOperations();
        }
      });

      if (!isViewable()) view.setVisible(false);

    } catch (JavaScriptException e) {
      Notification.alert(e.getMessage());
    }

  }

  private void toogleOperations() {
    if (operations.getStylePrimaryName().equalsIgnoreCase("ops-closed")) {
      operations.setStylePrimaryName("ops-open");
      link.setStylePrimaryName("expand-less");
    } else {
      operations.setStylePrimaryName("ops-closed");
      link.setStylePrimaryName("expand-more");
    }
  }

  private boolean isVisibleOperations() {
    return operations.getStylePrimaryName().equals("ops-open");
  }

  private void viewDocument() {
    AttachmentsManager.viewDocument(data.getId(), data.getLang());
  }

  @UiHandler("share")
  protected void share(ClickEvent event) {
    if (isVisibleOperations()) {
      SharingPage page = new SharingPage();
      page.setData("Attachment", data.getSpId(), data.getInstanceId());
      page.show();
    }
  }

  @UiHandler("download")
  protected void download(ClickEvent event) {
    if (isVisibleOperations()) {
      if (MobilUtils.isIOS()) {
        if (data.isDownloadable()) {
          String u = link.getHref();
          if (NetworkHelper.isOnline()) {
            CacheStorageHelper.store(u);
          }
          Window.open(u, "_blank", "fullscreen=yes");
        } else {
          viewDocument();
        }
      } else {
        if (data.isDownloadable()) {
          link.setTarget("_self");
          link.getElement().setAttribute("download", data.getFileName());
          if (NetworkHelper.isOnline()) {
            CacheStorageHelper.store(((Anchor) event.getSource()).getHref());
          }
        } else {
          viewDocument();
        }
      }
    }
  }

  @UiHandler("view")
  protected void view(ClickEvent event) {
    if (isVisibleOperations()) viewDocument();
  }

  @UiHandler("notify")
  protected void notify(ClickEvent event) {
    if (isVisibleOperations()) {
      NotificationsApp app = new NotificationsApp(data.getInstanceId(), data.getId(), NotificationDTO.TYPE_DOCUMENT, data.getFileName(), data.getFileName());
      app.start();
    }
  }
}
