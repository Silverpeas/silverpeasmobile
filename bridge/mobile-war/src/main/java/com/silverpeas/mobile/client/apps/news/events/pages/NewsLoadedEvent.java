package com.silverpeas.mobile.client.apps.news.events.pages;

import com.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.util.List;

public class NewsLoadedEvent extends AbstractNewsPagesEvent {

  private List<NewsDTO> news;

  public NewsLoadedEvent(List<NewsDTO> news){
    super();
    this.news = news;
  }

  @Override
  protected void dispatch(NewsPagesEventHandler handler) {
    handler.onNewsLoad(this);
  }

  public List<NewsDTO> getNews() {
    return news;
  }
}
