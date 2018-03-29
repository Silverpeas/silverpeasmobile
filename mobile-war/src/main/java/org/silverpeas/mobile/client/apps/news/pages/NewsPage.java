/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package org.silverpeas.mobile.client.apps.news.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.news.events.app.NewsLoadEvent;
import org.silverpeas.mobile.client.apps.news.events.pages.AbstractNewsPagesEvent;
import org.silverpeas.mobile.client.apps.news.events.pages.NewsLoadedEvent;
import org.silverpeas.mobile.client.apps.news.events.pages.NewsPagesEventHandler;
import org.silverpeas.mobile.client.apps.news.pages.widgets.NewsItem;
import org.silverpeas.mobile.client.apps.news.resources.NewsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.util.List;

public class NewsPage extends PageContent implements NewsPagesEventHandler {

  private static NewsPageUiBinder uiBinder = GWT.create(NewsPageUiBinder.class);

  @UiField(provided = true) protected NewsMessages msg = null;
  @UiField UnorderedList news;
  @UiField ActionsMenu actionsMenu;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private String instanceId;

  interface NewsPageUiBinder extends UiBinder<Widget, NewsPage> {
  }

  public NewsPage() {
    msg = GWT.create(NewsMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractNewsPagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new NewsLoadEvent());
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractNewsPagesEvent.TYPE, this);
  }

  @Override
  public void onNewsLoad(final NewsLoadedEvent event) {
    news.clear();
    List<NewsDTO> newsDTOList = event.getNews();
    int i = 1;
    int max = newsDTOList.size();
    for (NewsDTO newsDTO : newsDTOList) {
      NewsItem item = new NewsItem();
      item.setData(i, max, newsDTO);
      news.add(item);
      instanceId = newsDTO.getInstanceId();
      i++;
    }
    actionsMenu.addAction(favorite);
    favorite.init(instanceId, null, ContentsTypes.App.name(), getPageTitle());
  }

}