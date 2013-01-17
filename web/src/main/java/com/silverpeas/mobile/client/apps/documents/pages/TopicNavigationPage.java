package com.silverpeas.mobile.client.apps.documents.pages;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.PageHistory;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.HorizontalPanel;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadTopicsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.AbstractTopicsNavigationPagesEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.TopicSelectedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.TopicsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.TopicsNavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.PageView;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;

public class TopicNavigationPage extends PageView implements View, TopicsNavigationPagesEventHandler {

	@UiField(provided = true) protected DocumentsMessages msg = null;
	@UiField(provided = true) protected ApplicationMessages globalMsg = null;
	@UiField(provided = true) protected DocumentsResources ressources = null;
	
	@UiField ListPanel listPanel;
	@UiField Label title;
	
	private boolean stopped = false;
	private String rootTopicId, instanceId;
	private List<TopicDTO> topicsList = null;
	
	private SelectionHandler selectionHandler = new SelectionHandler();
	
	private static TopicNavigationPageUiBinder uiBinder = GWT.create(TopicNavigationPageUiBinder.class);

	interface TopicNavigationPageUiBinder extends UiBinder<Widget, TopicNavigationPage> {
	}
	
	public class SelectionHandler implements ClickHandler {

		@Override
		public void onClick(final ClickEvent event) {
			clickGesture(new Command() {
				
				@Override
				public void execute() {
					String id = ((Widget)event.getSource()).getElement().getParentElement().getId();		
					for (TopicDTO topic : topicsList) {
						if (topic.getId().equals(id)) {
							EventBus.getInstance().fireEvent(new TopicSelectedEvent(topic));		
							break;
						}
					}					
				}
			});					
		}		
	}

	public TopicNavigationPage() {
		ressources = GWT.create(DocumentsResources.class);		
		ressources.css().ensureInjected();
		msg = GWT.create(DocumentsMessages.class);
		globalMsg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		EventBus.getInstance().addHandler(AbstractTopicsNavigationPagesEvent.TYPE, this);
	}
	
	@UiHandler("listPanel")
	void onSelectionChanged(SelectionChangedEvent event) {
		TopicDTO topic = topicsList.get(event.getSelection());
		if (!topic.isTerminal()) {
			TopicNavigationPage topicNav = new TopicNavigationPage();
			topicNav.setInstanceId(instanceId);
			topicNav.setTopicId(topic.getId());
			goTo(topicNav);
		}		
	}

	@Override
	public void stop() {
		if (!stopped) {
			EventBus.getInstance().removeHandler(AbstractTopicsNavigationPagesEvent.TYPE, this);
			stopped = true;
		}	
	}
	
	@Override
	public void goBack(Object returnValue) {
		stop();		
		super.goBack(returnValue);
	}
	
	public void setTopicId(String rootTopicId) {
		Notification.activityStart();
		this.rootTopicId = rootTopicId;
		EventBus.getInstance().fireEvent(new DocumentsLoadTopicsEvent(instanceId, rootTopicId));
	}
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public void onLoadedTopics(TopicsLoadedEvent event) {
		
		if (topicsList == null) {
			topicsList = event.getTopics();
			for (TopicDTO topic : topicsList) {
				// add to ihm list
				ListItem item = new ListItem();				
				HorizontalPanel panel = new HorizontalPanel();
				panel.getElement().setId(topic.getId());
				Button view = new Button();
				view.setText(msg.viewTopic());								
				panel.add(view);
				view.addClickHandler(selectionHandler);
				
				Label la = new Label(topic.getName());				
			    panel.add(la);
			    la.addStyleName(ressources.css().topicLabel());
			    
			    item.add(panel);
			    item.setShowArrow(!topic.isTerminal());			    
				listPanel.add(item);
			}
			Notification.activityStop();
		}		
	}

	@Override
	public void onTopicSelected(TopicSelectedEvent event) {
		if (PageHistory.Instance.current().equals(this)) {
			// remove navigation pages from history
			Page lastPage = null;
			while (true) {
				if (PageHistory.Instance.current() instanceof TopicNavigationPage) {
					lastPage = PageHistory.Instance.back();
					((View) lastPage).stop();
				} else {
					PageHistory.Instance.add(lastPage);
					break;
				}				
			}
			goBack(null);		
		}		
	}
}
