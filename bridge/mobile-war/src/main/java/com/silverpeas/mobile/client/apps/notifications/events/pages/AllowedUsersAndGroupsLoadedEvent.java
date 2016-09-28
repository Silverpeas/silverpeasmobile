package com.silverpeas.mobile.client.apps.notifications.events.pages;

import com.silverpeas.mobile.shared.dto.BaseDTO;

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
