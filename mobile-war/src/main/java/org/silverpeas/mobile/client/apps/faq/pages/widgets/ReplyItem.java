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

package org.silverpeas.mobile.client.apps.faq.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.faq.events.app.FaqAttachmentsLoadEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.AbstractFaqPagesEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqAttachmentsLoadedEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqCategoriesLoadedEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqPagesEventHandler;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.attachments.Attachment;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.documents.SimpleDocumentDTO;
import org.silverpeas.mobile.shared.dto.faq.ReplyDTO;

public class ReplyItem extends Composite implements FaqPagesEventHandler {

  private ReplyDTO data;
  private static ReplyItemUiBinder uiBinder = GWT.create(ReplyItemUiBinder.class);

  @UiField
  HTML content;
  @UiField
  HTMLPanel container;

  @UiField
  UnorderedList attachments;

  protected ApplicationMessages msg = null;

  interface ReplyItemUiBinder extends UiBinder<Widget, ReplyItem> {}

  public ReplyItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
    attachments.getElement().setId("attachments");
    EventBus.getInstance().addHandler(AbstractFaqPagesEvent.TYPE, this);
  }

  public void setData(ReplyDTO data) {
    this.data = data;
    String html = "<h2 class='title'>" + data.getTitle() + "</h2>";
    html += "<span>" + data.getContent() + "</span>";
    content.setHTML(html);
    EventBus.getInstance().fireEvent(new FaqAttachmentsLoadEvent(data.getId()));
  }

  @Override
  public void onCategoriesLoaded(final FaqCategoriesLoadedEvent event) {}

  @Override
  public void onAttachmentsLoaded(final FaqAttachmentsLoadedEvent event) {
    if (event.getContentId().equals(data.getId())) {
      data.setAttachments(event.getAttachments());
      for (SimpleDocumentDTO attachment : event.getAttachments()) {
        Attachment a = new Attachment();
        a.setAttachment(attachment);
        attachments.add(a);
      }
    }
  }

  public void stop() {
    EventBus.getInstance().removeHandler(AbstractFaqPagesEvent.TYPE, this);
  }
}
