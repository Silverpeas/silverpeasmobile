/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.news.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

public class NewsItem extends Composite implements ClickHandler {

  private NewsDTO data;
  private int max, index;
  private static FavoriteItemUiBinder uiBinder = GWT.create(FavoriteItemUiBinder.class);
  @UiField
  Anchor picto;
  @UiField
  HTML content;
  @UiField
  HTMLPanel container;
  protected ApplicationMessages msg = null;

  interface FavoriteItemUiBinder extends UiBinder<Widget, NewsItem> {}

  public NewsItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(int index, int max, NewsDTO data) {
    this.index = index;
    this.max = max;
    this.data = data;
    String status = "";
    if (data.isManagable()) {
      if (data.isDraft()) {
        status = " ("+msg.draft()+")";
      } else if (!data.isVisible()) {
        status = " ("+msg.notVisible()+")";
      }
    }
    picto.setHTML("<h2><a href='#'>" + data.getTitle() + status + "</a></h2><img src='" + data.getVignette() + "'/>");
    picto.addClickHandler(this);
    content.setHTML(
         "<p>" + data.getDescription() + "</p><br class='clear'/>");
    content.addClickHandler(this);
  }

  @Override
  public void onClick(final ClickEvent event) {
    ContentDTO content = new ContentDTO();
    content.setId(data.getId());
    content.setContributionId(data.getIdNews());
    content.setType(ContentsTypes.News.toString());
    content.setInstanceId(data.getInstanceId());
    EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));

  }
}
