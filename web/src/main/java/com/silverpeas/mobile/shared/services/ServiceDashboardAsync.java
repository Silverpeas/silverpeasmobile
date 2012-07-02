package com.silverpeas.mobile.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.SocialInformationDTO;

public interface ServiceDashboardAsync {
	void getAll(int reinitialisationPage, String socialInformationType, AsyncCallback <List<SocialInformationDTO>> callback);
}
