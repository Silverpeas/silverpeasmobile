package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.search.ResultDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.SearchException;

import java.util.List;

@RemoteServiceRelativePath("Search")
public interface ServiceSearch extends RemoteService{
  List<ResultDTO> search(String query) throws SearchException, AuthenticationException;
}
