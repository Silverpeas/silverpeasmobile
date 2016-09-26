package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.silverpeas.mobile.shared.dto.search.ResultDTO;

import java.util.List;

public interface ServiceSearchAsync {
  void search(String query, AsyncCallback<List<ResultDTO>> callback);
}
