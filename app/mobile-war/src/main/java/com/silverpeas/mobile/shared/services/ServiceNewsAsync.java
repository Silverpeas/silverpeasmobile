package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.util.List;

public interface ServiceNewsAsync {
  void loadNews(final AsyncCallback<List<NewsDTO>> async);
}
