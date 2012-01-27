package com.silverpeas.mobile.client.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.ApplicationInstanceDTO;

public class Navigation extends Composite{
	
	interface MyUiBinder extends UiBinder<Widget, Navigation> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	@UiField Label chemin;
	@UiField ListPanel listPanel;
	
	public Navigation(String spaceType, int level){
		initWidget(uiBinder.createAndBindUi(this));
		listPanel.clear();
		Navigate(spaceType, level);
	}
	
	public void Navigate(String spaceType, int level){
		ServicesLocator.serviceAdmin.getAllSpaces(spaceType, level, new AsyncCallback<List<ApplicationInstanceDTO>> (){
			
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));	
			}
			
			public void onSuccess(List<ApplicationInstanceDTO> result) {
				for (ApplicationInstanceDTO applicationInstanceDTO : result) {
					ListItem li = new ListItem();
					Label la = new Label(applicationInstanceDTO.getType() + " : " + applicationInstanceDTO.getLabel());
					li.add(la);
					DOM.setElementAttribute(li.getElement(), "id", applicationInstanceDTO.getType()+applicationInstanceDTO.getId());
					listPanel.add(li);
				}
			}
		});
	}
}
