package com.silverpeas.mobile.client.apps.dashboard.events.controller;

public class DashboardLoadEvent extends AbstractDashboardControllerEvent{

	private int reinitialisationPage;
	private String socialInformationType;
	
	public DashboardLoadEvent(int reinitialisationPage, String socialInformationType){
		super();
		this.reinitialisationPage = reinitialisationPage;
		this.socialInformationType = socialInformationType;
	}
	
	@Override
	protected void dispatch(DashboardControllerEventHandler handler) {
		handler.getSocialInformations(this);
	}
	
	public int getReinitailisationPage(){
		return reinitialisationPage;
	}
	
	public String getSocialInformationType(){
		return socialInformationType;
	}
}
