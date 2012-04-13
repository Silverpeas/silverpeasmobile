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

package com.silverpeas.mobile.client.apps.documents.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;

public class AttachmentWidget extends Composite {

  private static AttachmentWidgetUiBinder uiBinder = GWT.create(AttachmentWidgetUiBinder.class);
  private AttachmentDTO attachement;

  @UiField(provided = true)
  protected DocumentsMessages msg = null;
  @UiField(provided = true)
  protected ApplicationMessages globalMsg = null;
  @UiField(provided = true)
  protected DocumentsResources ressources = null;

  @UiField
  Label title, size, date, icon;

  interface AttachmentWidgetUiBinder extends UiBinder<Widget, AttachmentWidget> {
  }

  public AttachmentWidget() {
    ressources = GWT.create(DocumentsResources.class);
    ressources.css().ensureInjected();
    msg = GWT.create(DocumentsMessages.class);
    globalMsg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("item")
  void download(ClickEvent event) {
    Window.open(attachement.getUrl(), "_blank", "");
    // TODO : use downloader phonegap plugin
  }

  public void setAttachment(AttachmentDTO attachmentDTO) {
    this.attachement = attachmentDTO;
    render();
  }

  private void render() {
    DateTimeFormat fmt = DateTimeFormat.getFormat("dd MMMM yyyy");
    title.setText(attachement.getTitle());

    if (attachement.getSize() < 1024 * 1024) {
      size.setText(attachement.getSize() / 1024 + " Ko");
    } else {
      size.setText(attachement.getSize() / (1024 * 1024) + " Mo");
    }
    date.setText(fmt.format(attachement.getCreationDate()));
    if (attachement.getType().contains("msword")) {
      icon.setStylePrimaryName(ressources.css().msword());
    } else if (attachement.getType().contains("sheet")) {
      icon.setStylePrimaryName(ressources.css().msexcel());
    } else if (attachement.getType().contains("pdf")) {
      icon.setStylePrimaryName(ressources.css().pdf());
    } else {
      icon.setStylePrimaryName(ressources.css().unknown());
    }

  }
}
