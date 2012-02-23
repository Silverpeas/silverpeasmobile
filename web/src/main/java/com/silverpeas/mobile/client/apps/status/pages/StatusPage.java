package com.silverpeas.mobile.client.apps.status.pages;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.silverpeas.mobile.client.apps.status.events.app.internal.StatusStopEvent;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusLoadEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.AbstractStatusPagesEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusLoadedEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.StatusDTO;

public class StatusPage extends Page implements StatusPagesEventHandler, View{
	
	private static StatusPageUiBinder uiBinder = GWT.create(StatusPageUiBinder.class);
	
	@UiField Label labelStatus;
	@UiField ListPanel panelStatus;
	@UiField Button more;	
	@UiField Label modifierItem;
	private DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
	private int currentPage = 1;
	private int lastStatusInd = 0;
	
	interface StatusPageUiBinder extends UiBinder<Widget, StatusPage> {
	}

	public StatusPage() {
		initWidget(uiBinder.createAndBindUi(this));	
		lastStatusInd = 0;
		EventBus.getInstance().addHandler(AbstractStatusPagesEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new StatusLoadEvent(currentPage));
		modifierItem.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				PostPage postPage = new PostPage();
				goTo(postPage);
			}
		});
	}
	
	@UiHandler("more")
	void MoreButton(ClickEvent e){
		currentPage++;
		EventBus.getInstance().fireEvent(new StatusLoadEvent(currentPage));
	}
	
	@Override
	public void onStatusLoaded(StatusLoadedEvent event) {
		Iterator<StatusDTO> iResult = event.getListStatus().iterator();
		while (iResult.hasNext()) {
			StatusDTO statusDTO = (StatusDTO) iResult.next();					
			if (isStatusNotDisplay(statusDTO.getId())) {
				if(currentPage==1 && lastStatusInd==0){
					labelStatus.setText(statusDTO.getDescription());
					lastStatusInd = 1;
				}
				// ajout les status non affichés (cas du post ajouté, puis navigation dans les précédents)
				ListItem li = new ListItem();
				Label la = new Label("Le "+ fmt.format(statusDTO.getCreationDate()) + " : " + statusDTO.getDescription());
				li.getElement().setId(String.valueOf(statusDTO.getId()));
				li.add(la);
				panelStatus.add(li);
			}					
		}			
	}
	
	private boolean isStatusNotDisplay(int id) {		
		Iterator<Widget> it = panelStatus.iterator();
		while (it.hasNext()) {
			Widget widget = (Widget) it.next();
			if (widget.getElement().getId().equals(String.valueOf(id))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractStatusPagesEvent.TYPE, this);	
	}
	
	@Override
	public void goBack(Object returnValue) {
		stop();
		EventBus.getInstance().fireEvent(new StatusStopEvent());		
		super.goBack(returnValue);
	}
}