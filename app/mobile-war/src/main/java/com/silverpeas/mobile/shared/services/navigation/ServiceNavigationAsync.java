package com.silverpeas.mobile.shared.services.navigation;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.ContentDTO;
import com.silverpeas.mobile.shared.dto.HomePageDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;

public interface ServiceNavigationAsync {

  void getSpacesAndApps(String rootSpaceId, final AsyncCallback<List<SilverpeasObjectDTO>> async);

  void getApp(String instanceId, String contentId, String contentType, final AsyncCallback<ApplicationInstanceDTO> async);

  void getHomePageData(String spaceId, final AsyncCallback<HomePageDTO> async);
}
