package com.silverpeas.mobile.client.apps.dashboard.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.SocialInformationDTO;

public class DashboardLoadedEvent extends AbstractDashboardPagesEvent{

	private List<SocialInformationDTO> listSocialInformationDTO;
	
	public DashboardLoadedEvent(List<SocialInformationDTO> listSocialInformationDTO){
		super();
		this.listSocialInformationDTO = listSocialInformationDTO;
	}
	
	@Override
	protected void dispatch(DashboardPagesEventHandler handler) {
		handler.onDashboardLoaded(this);
	}
	
	public List<SocialInformationDTO> getListSocialInformations(){
		return listSocialInformationDTO;
	}
}
