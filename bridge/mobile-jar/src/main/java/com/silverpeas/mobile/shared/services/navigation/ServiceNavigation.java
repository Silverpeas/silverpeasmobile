package com.silverpeas.mobile.shared.services.navigation;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.RightDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.NavigationException;

@RemoteServiceRelativePath("Navigation")
public interface ServiceNavigation extends RemoteService {
  public List<SilverpeasObjectDTO> getSpacesAndApps(String rootSpaceId, String appType) throws NavigationException, AuthenticationException;
  public ApplicationInstanceDTO getApp(String instanceId) throws NavigationException, AuthenticationException;
}
