package com.silverpeas.mobile.shared.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.SocialInformation;

public interface ServiceDashboardAsync {
	void getALL(AsyncCallback <Map<Date, List<SocialInformation>>> callback);
}
