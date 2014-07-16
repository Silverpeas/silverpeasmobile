package com.silverpeas.mobile.client.apps.documents.pages;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadGedItemsEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.AbstractGedNavigationPagesEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemClickEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedNavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.pages.widgets.GedItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;

public class GedNavigationPage extends PageContent implements View, GedNavigationPagesEventHandler {
	
	
	@UiField UnorderedList list;	
		
	private String rootTopicId, instanceId;
	private boolean dataLoaded = false;
	
	private static GedNavigationPageUiBinder uiBinder = GWT.create(GedNavigationPageUiBinder.class);

	interface GedNavigationPageUiBinder extends UiBinder<Widget, GedNavigationPage> {
	}

	public GedNavigationPage() {
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractGedNavigationPagesEvent.TYPE, this);
	}

	@Override
	public void stop() {		
		EventBus.getInstance().removeHandler(AbstractGedNavigationPagesEvent.TYPE, this);		
	}
	
	public void setTopicId(String rootTopicId) {
		Notification.activityStart();
		this.rootTopicId = rootTopicId;
		EventBus.getInstance().fireEvent(new DocumentsLoadGedItemsEvent(instanceId, rootTopicId));
	}
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public void onLoadedTopics(GedItemsLoadedEvent event) {
		Notification.activityStart();
		if (isVisible() && dataLoaded == false) {
			
			list.clear();
			List<Object> dataItems = event.getTopicsAndPublications();
			for (Object dataItem : dataItems) {
				GedItem item = new GedItem();				
				item.setData(dataItem);				
				list.add(item);				
			}
			dataLoaded = true;		
		}
		Notification.activityStop();	
	}

	@Override
	public void onGedItemClicked(GedItemClickEvent event) {
		if (isVisible()) {
			if (event.getGedItem() instanceof TopicDTO) {
				GedNavigationPage page = new GedNavigationPage();
				page.setPageTitle(getPageTitle());				
				page.setInstanceId(instanceId);
				page.setTopicId(((TopicDTO)event.getGedItem()).getId());
				page.show();
			} else if (event.getGedItem() instanceof PublicationDTO) {
				PublicationPage page = new PublicationPage();
				page.setPageTitle("Publication");			
				page.show();
				EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(((PublicationDTO) event.getGedItem()).getId()));
			}		
		}
	}
}
