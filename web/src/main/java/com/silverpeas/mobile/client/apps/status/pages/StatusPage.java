package com.silverpeas.mobile.client.apps.status.pages;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.silverpeas.mobile.client.apps.status.events.app.internal.StatusStopEvent;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusLoadEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.AbstractStatusPagesEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusLoadedEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPagesEventHandler;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostedEvent;
import com.silverpeas.mobile.client.apps.status.resources.StatusMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.StatusDTO;

public class StatusPage extends Page implements StatusPagesEventHandler, View {
	
	private static StatusPageUiBinder uiBinder = GWT.create(StatusPageUiBinder.class);
	
	@UiField(provided = true) protected StatusMessages msg = null;
	@UiField ListPanel panelStatus;
	@UiField Button more;
	
	private DateTimeFormat fmt = null;
	private int currentPage = 1;
	
	interface StatusPageUiBinder extends UiBinder<Widget, StatusPage> {
	}

	public StatusPage() {
		msg = GWT.create(StatusMessages.class);
		initWidget(uiBinder.createAndBindUi(this));	
		fmt = DateTimeFormat.getFormat(msg.dateFormat());
		EventBus.getInstance().addHandler(AbstractStatusPagesEvent.TYPE, this);
		
		// changement des derniers status
		EventBus.getInstance().fireEvent(new StatusLoadEvent(currentPage));
	}
	
	@UiHandler("panelStatus")
	void onSelectionChanged(SelectionChangedEvent event) {
		if (event.getSelection() == 0) {
			PostPage postPage = new PostPage();
			goTo(postPage);
		}		
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
				// ajout les status non affichés (cas du post ajouté, puis navigation dans les précédents)
				ListItem li = createItem(statusDTO);
				panelStatus.add(li);
			}					
		}			
	}

	private ListItem createItem(StatusDTO statusDTO) {
		ListItem li = new ListItem();
		Label la = new Label(msg.prefixDate() + fmt.format(statusDTO.getCreationDate()) + msg.postfixDate() + statusDTO.getDescription());
		li.getElement().setId(String.valueOf(statusDTO.getId()));
		li.add(la);
		return li;
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

	@Override
	public void onStatusPost(StatusPostedEvent event) {
		ListItem li = createItem(event.getNewStatus());
		panelStatus.insert(li, 1);
	}
}