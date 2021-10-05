/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.faq.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.faq.events.pages.AbstractFaqPagesEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqAttachmentsLoadedEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqCategoriesLoadedEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqPagesEventHandler;
import org.silverpeas.mobile.client.apps.faq.pages.widgets.ReplyItem;
import org.silverpeas.mobile.client.apps.faq.resources.FaqMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.faq.ReplyDTO;

import java.util.List;

public class RepliesPage extends PageContent implements FaqPagesEventHandler {

  private static RepliesPageUiBinder uiBinder = GWT.create(RepliesPageUiBinder.class);

  private List<ReplyDTO> data;

  @UiField(provided = true) protected FaqMessages msg = null;

  @UiField
  HTMLPanel container;

  @UiField
  UnorderedList replies;

  public void setData(final List<ReplyDTO> data) {
    this.data = data;
    for (ReplyDTO r : data) {
      ReplyItem item = new ReplyItem();
      item.setData(r);
      replies.add(item);
    }
  }

  interface RepliesPageUiBinder extends UiBinder<Widget, RepliesPage> {
  }

  public RepliesPage() {
    msg = GWT.create(FaqMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractFaqPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractFaqPagesEvent.TYPE, this);
    for (int i = 0; i < replies.getCount(); i++) {
      ReplyItem item = (ReplyItem) replies.getWidget(i);
      item.stop();
    }
  }

  @Override
  public void onCategoriesLoaded(final FaqCategoriesLoadedEvent event) {
  }

  @Override
  public void onAttachmentsLoaded(final FaqAttachmentsLoadedEvent faqAttachmentsLoadedEvent) {
  }
}