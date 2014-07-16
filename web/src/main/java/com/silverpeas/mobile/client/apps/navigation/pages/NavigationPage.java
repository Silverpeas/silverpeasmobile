package com.silverpeas.mobile.client.apps.navigation.pages;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.controller.LoadSpacesAndAppsEvent;
import com.silverpeas.mobile.client.apps.navigation.events.pages.AbstractNavigationPagesEvent;
import com.silverpeas.mobile.client.apps.navigation.events.pages.ClickItemEvent;
import com.silverpeas.mobile.client.apps.navigation.events.pages.NavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.pages.SpacesAndAppsLoadedEvent;
import com.silverpeas.mobile.client.apps.navigation.pages.widgets.NavigationItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import com.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

public class NavigationPage extends PageContent implements NavigationPagesEventHandler {

	private static NavigationPageUiBinder uiBinder = GWT.create(NavigationPageUiBinder.class);
	private String rootSpaceId;
	private boolean dataLoaded = false;
	
	@UiField UnorderedList list;

	interface NavigationPageUiBinder extends UiBinder<Widget, NavigationPage> {
	}

	public NavigationPage() {
		initWidget(uiBinder.createAndBindUi(this));
		setPageTitle("Nav");
		EventBus.getInstance().addHandler(AbstractNavigationPagesEvent.TYPE, this);
		Notification.activityStart();
	}

	@Override
	public void spacesAndAppsLoaded(SpacesAndAppsLoadedEvent event) {
		
		if (isVisible() && dataLoaded == false) {
			
			list.clear();
			List<SilverpeasObjectDTO> objectsList = event.getObjectsList();
			for (SilverpeasObjectDTO silverpeasObjectDTO : objectsList) {
				NavigationItem item = new NavigationItem();				
				item.setData(silverpeasObjectDTO);				
				list.add(item);				
			}
			dataLoaded = true;		
		}
		Notification.activityStop();
	}
	
	public void setRootSpaceId(String rootSpaceId) {
		this.rootSpaceId = rootSpaceId;
		EventBus.getInstance().fireEvent(new LoadSpacesAndAppsEvent(rootSpaceId));
	}


	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractNavigationPagesEvent.TYPE, this);
	}

	@Override
	public void clickItem(ClickItemEvent event) {		
		if (isVisible()) {			
			if (event.getData() instanceof SpaceDTO) {			
				NavigationPage subPage = new NavigationPage();				
				subPage.setPageTitle(this.getPageTitle());
				subPage.setRootSpaceId(event.getData().getId());				
				goTo(subPage);
			} else {
				EventBus.getInstance().fireEvent(new NavigationAppInstanceChangedEvent((ApplicationInstanceDTO)event.getData()));
			}
		}		
	}


}
