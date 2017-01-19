package com.silverpeas.mobile.client.apps.news.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.apps.news.events.app.NewsLoadEvent;
import com.silverpeas.mobile.client.apps.news.events.pages.AbstractNewsPagesEvent;
import com.silverpeas.mobile.client.apps.news.events.pages.NewsLoadedEvent;
import com.silverpeas.mobile.client.apps.news.events.pages.NewsPagesEventHandler;
import com.silverpeas.mobile.client.apps.news.pages.widgets.NewsItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.util.Iterator;

/**
 * @author: svu
 */
public class NewsPage extends Composite implements NewsPagesEventHandler {

  interface NewsPageUiBinder extends UiBinder<HTMLPanel, NewsPage> {}
  private static NewsPageUiBinder uiBinder = GWT.create(NewsPageUiBinder.class);

  @UiField HTMLPanel container;
  @UiField UnorderedList list;

  public NewsPage() {
    initWidget(uiBinder.createAndBindUi(this));
    this.getElement().setId("last-news");
    EventBus.getInstance().fireEvent(new NewsLoadEvent());
    EventBus.getInstance().addHandler(AbstractNewsPagesEvent.TYPE, this);
  }

  @Override
  public void onNewsLoad(final NewsLoadedEvent event) {
    Notification.activityStop();
    Iterator<NewsDTO> i = event.getNews().iterator();
    while (i.hasNext()) {
      NewsDTO news = i.next();
      if (news != null) {
        NewsItem item = new NewsItem();
        item.setData(news);
        list.add(item);
      }
    }
  }

  /*@Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractNewsPagesEvent.TYPE, this);
  }*/
}