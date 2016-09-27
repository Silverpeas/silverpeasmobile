package com.silverpeas.mobile.client.apps.notifications.events.pages;

import com.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsPagesEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsPagesEventHandler;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

import java.util.List;

public class AllowedUsersAndGroupsLoadedEvent extends AbstractNotificationPagesEvent {

	List<BaseDTO> list;

	public AllowedUsersAndGroupsLoadedEvent(List<BaseDTO> list){
		super();
		this.list = list;
	}
	
	@Override
	protected void dispatch(NotificationPagesEventHandler handler) {
		handler.onAllowedUsersAndGroupsLoaded(this);
	}
	
	public List<BaseDTO> getListAllowedUsersAndGroups(){
		return list;
	}
}
