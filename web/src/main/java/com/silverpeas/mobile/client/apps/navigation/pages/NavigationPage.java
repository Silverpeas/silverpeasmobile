package com.silverpeas.mobile.client.apps.navigation.pages;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.PageHistory;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListItem.ShowArrow;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.internal.NavigationStopEvent;
import com.silverpeas.mobile.client.apps.navigation.events.controller.LoadSpacesAndAppsEvent;
import com.silverpeas.mobile.client.apps.navigation.events.pages.AbstractNavigationPagesEvent;
import com.silverpeas.mobile.client.apps.navigation.events.pages.NavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.pages.SpacesAndAppsLoadedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.PageView;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import com.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

public class NavigationPage extends PageView implements NavigationPagesEventHandler, View {

	private static NavigationPageUiBinder uiBinder = GWT.create(NavigationPageUiBinder.class);
	private String rootSpaceId;
	private boolean dataCached = false;
	private boolean stopped = false;
	private HashMap<String, SilverpeasObjectDTO> model = new HashMap<String, SilverpeasObjectDTO>();
	
	@UiField ListPanel listPanel;
	@UiField Label title;

	interface NavigationPageUiBinder extends UiBinder<Widget, NavigationPage> {
	}

	public NavigationPage() {
		initWidget(uiBinder.createAndBindUi(this));		
		EventBus.getInstance().addHandler(AbstractNavigationPagesEvent.TYPE, this);
		Notification.activityStart();
	}

	@Override
	public void spacesAndAppsLoaded(SpacesAndAppsLoadedEvent event) {
		title.setText(this.getTitle());
		if (dataCached == false) {
			List<SilverpeasObjectDTO> objectsList = event.getObjectsList();
			for (SilverpeasObjectDTO silverpeasObjectDTO : objectsList) {
				// add to model
				model.put(silverpeasObjectDTO.getId(), silverpeasObjectDTO);
				// add to ihm list
				ListItem item = new ListItem();
				Label la = new Label(silverpeasObjectDTO.getLabel());
				item.getElement().setId(silverpeasObjectDTO.getId());
			    item.add(la);				
				if (silverpeasObjectDTO instanceof SpaceDTO) {
					item.setDisplayArrow(ShowArrow.Visible);
					item.getElement().setAttribute("space", "true");
				} else {
					item.getElement().setAttribute("space", "false");
				}				
				listPanel.add(item);
			}
			dataCached = true;
		}
		Notification.activityStop();
	}

	@UiHandler("listPanel")
	void onSelectionChanged(SelectionChangedEvent event) {
		ListItem item = listPanel.getItem(event.getSelection());
		if (item.getElement().getAttribute("space").equals("true")) {
			NavigationPage subPage = new NavigationPage();
			subPage.setRootSpaceId(item.getElement().getId());
			goTo(subPage);
		} else {			
			EventBus.getInstance().fireEvent(new NavigationAppInstanceChangedEvent((ApplicationInstanceDTO)model.get(item.getElement().getId())));
			// remove navigation pages from history
			Page lastPage = null;
			while (true) {
				if (PageHistory.Instance.current() instanceof NavigationPage) {
					PageHistory.Instance.goBack(lastPage);
					((View) lastPage).stop();
				} else {
					//PageHistory.Instance. add(lastPage);
					//TODO : 
					break;
				}		
			}
			// return to initial launching page
			goBack(null);			
		}		
	}
	
	public void setRootSpaceId(String rootSpaceId) {
		this.rootSpaceId = rootSpaceId;
		EventBus.getInstance().fireEvent(new LoadSpacesAndAppsEvent(rootSpaceId));
	}
	
	@Override
	public void goBack(Object returnValue) {
		stop();
		if (rootSpaceId == null) {
			// stop app if first page
			EventBus.getInstance().fireEvent(new NavigationStopEvent());
		}
		super.goBack(returnValue);
	}

	@Override
	public void stop() {
		if (!stopped) {
			EventBus.getInstance().removeHandler(AbstractNavigationPagesEvent.TYPE, this);
			stopped = true;
		}		
	}
}
