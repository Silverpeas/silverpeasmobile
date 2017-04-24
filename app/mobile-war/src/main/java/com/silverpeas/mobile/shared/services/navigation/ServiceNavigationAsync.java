package com.silverpeas.mobile.shared.services.navigation;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.HomePageDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;

public interface ServiceNavigationAsync {
	void getSpacesAndApps(String rootSpaceId, AsyncCallback<List<SilverpeasObjectDTO>> callback);

  void getApp(String instanceId, final AsyncCallback<ApplicationInstanceDTO> async);

  void getHomePageData(String spaceId, final AsyncCallback<HomePageDTO> async);
}
