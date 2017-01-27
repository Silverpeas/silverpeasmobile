package com.silverpeas.mobile.client.apps.news.events.pages;

import com.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.util.List;

public class NewsLoadedEvent extends AbstractNewsPagesEvent {

  private String news;

  public NewsLoadedEvent(String news){
    super();
    this.news = news;
  }

  @Override
  protected void dispatch(NewsPagesEventHandler handler) {
    handler.onNewsLoad(this);
  }

  public String getNews() {
    return news;
  }
}
