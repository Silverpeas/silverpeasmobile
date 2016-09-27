package com.silverpeas.mobile.client.apps.navigation.events.pages;

import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import com.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

public class ClickItemEvent extends AbstractNavigationPagesEvent {

	private SilverpeasObjectDTO data;
	
	@Override
	protected void dispatch(NavigationPagesEventHandler handler) {
		handler.clickItem(this);
	}

	public ClickItemEvent(SilverpeasObjectDTO data) {
		super();
		this.data = data;
	}

	public SilverpeasObjectDTO getData() {
		return data;
	}
	
	public boolean isSpace() {
		return (data instanceof SpaceDTO);
	}
}
