package com.silverpeas.mobile.client.apps.dashboard.pages;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
import com.gwtmobile.ui.client.widgets.TabPanel;
import com.silverpeas.mobile.client.apps.dashboard.events.controller.DashboardLoadEvent;
import com.silverpeas.mobile.client.apps.dashboard.events.pages.AbstractDashboardPagesEvent;
import com.silverpeas.mobile.client.apps.dashboard.events.pages.DashboardLoadedEvent;
import com.silverpeas.mobile.client.apps.dashboard.events.pages.DashboardPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.SocialInformationDTO;

public class DashboardPage extends Page implements DashboardPagesEventHandler, View{

	private static DashboardPageUiBinder uiBinder = GWT.create(DashboardPageUiBinder.class);
	
	@UiField ListPanel panelAll;
	@UiField Button more;
	@UiField TabPanel navBar;
	@UiField ListPanel panelStatus;
	@UiField ListPanel panelRelationShip;
	@UiField ListPanel panelPublication;
	@UiField ListPanel panelPhoto;
	@UiField ListPanel panelEvent;
	
	private DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
	private int reinitialisationPage = 1;
	private String socialInformationType = "ALL";

	interface DashboardPageUiBinder extends UiBinder<Widget, DashboardPage> {
	}

	public DashboardPage() {
		initWidget(uiBinder.createAndBindUi(this));
		reinitialisationPage = 0;
		EventBus.getInstance().addHandler(AbstractDashboardPagesEvent.TYPE, this);
		
		navBar.addSelectionHandler(new SelectionHandler<Integer>() {
            public void onSelection(SelectionEvent<Integer> event) {
            	reinitialisationPage = 0;
            	panelAll.clear();
            	panelStatus.clear();
            	panelRelationShip.clear();
            	panelPublication.clear();
            	panelPhoto.clear();
            	panelEvent.clear();
                if(event.getSelectedItem()==0){
                	socialInformationType = "ALL";
                	EventBus.getInstance().fireEvent(new DashboardLoadEvent(reinitialisationPage, socialInformationType));
                }
                if(event.getSelectedItem()==1){
                    socialInformationType = "STATUS";
                    EventBus.getInstance().fireEvent(new DashboardLoadEvent(reinitialisationPage, socialInformationType));
                }
                if(event.getSelectedItem()==2){
                    socialInformationType = "RELATIONSHIP";
                    EventBus.getInstance().fireEvent(new DashboardLoadEvent(reinitialisationPage, socialInformationType));
                }
                if(event.getSelectedItem()==3){
                    socialInformationType = "PUBLICATION";
                    EventBus.getInstance().fireEvent(new DashboardLoadEvent(reinitialisationPage, socialInformationType));
                }
                if(event.getSelectedItem()==4){
                    socialInformationType = "PHOTO";
                    EventBus.getInstance().fireEvent(new DashboardLoadEvent(reinitialisationPage, socialInformationType));
                }
                if(event.getSelectedItem()==5){
                    socialInformationType = "EVENT";
                    EventBus.getInstance().fireEvent(new DashboardLoadEvent(reinitialisationPage, socialInformationType));
                }
            }
		});
	}
	
	@UiHandler("more")
	void MoreButton(ClickEvent e){
		reinitialisationPage = 1;
		EventBus.getInstance().fireEvent(new DashboardLoadEvent(reinitialisationPage, socialInformationType));
	}
	
	public void AffichagePanel(SocialInformationDTO socialInformationDTO){
		ListItem li = new ListItem();
		Label la = new Label("Le " + fmt.format(socialInformationDTO.getDate()) + " : " + socialInformationDTO.getType() + " : " + socialInformationDTO.getAuteur() + " : " + socialInformationDTO.getDescription());
		li.add(la);
		if(socialInformationType=="ALL"){
			panelAll.add(li);
		}
		if(socialInformationType=="STATUS"){
			panelStatus.add(li);
		}
		if(socialInformationType=="RELATIONSHIP"){
			panelRelationShip.add(li);
		}
		if(socialInformationType=="PUBLICATION"){
			panelPublication.add(li);
		}
		if(socialInformationType=="PHOTO"){
			panelPhoto.add(li);
		}
		if(socialInformationType=="EVENT"){
			panelEvent.add(li);
		}
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractDashboardPagesEvent.TYPE, this);
	}

	@Override
	public void onDashboardLoaded(DashboardLoadedEvent event) {
		Iterator<SocialInformationDTO> i = event.getListSocialInformations().iterator();
		while(i.hasNext()){
			SocialInformationDTO socialInformationDTO = i.next();
			AffichagePanel(socialInformationDTO);
		}
	}
}
