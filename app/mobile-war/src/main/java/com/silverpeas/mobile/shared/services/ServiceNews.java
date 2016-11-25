package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.NewsException;

import java.util.List;

@RemoteServiceRelativePath("News")
public interface ServiceNews extends RemoteService {
  public List<NewsDTO> loadNews() throws NewsException, AuthenticationException;
}
