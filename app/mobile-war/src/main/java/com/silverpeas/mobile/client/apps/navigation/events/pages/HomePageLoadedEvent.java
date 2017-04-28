package com.silverpeas.mobile.client.apps.navigation.events.pages;

import java.util.List;

import com.silverpeas.mobile.shared.dto.HomePageDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;

public class HomePageLoadedEvent extends AbstractNavigationPagesEvent {
	
	private HomePageDTO data;
	
	public HomePageLoadedEvent(HomePageDTO data) {
		super();
		this.data = data;
	}

	@Override
	protected void dispatch(NavigationPagesEventHandler handler) {
		handler.homePageLoaded(this);
	}

	public HomePageDTO getData() {
		return data;
	}
}
