package com.silverpeas.mobile.client.pages.status;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.gwtmobile.ui.client.widgets.TextBox;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.StatusDTO;

public class StatusPage extends Page {
	
	private static StatusPageUiBinder uiBinder = GWT.create(StatusPageUiBinder.class);
	
	@UiField TextBox textField;
	@UiField Button post;
	@UiField Label labelStatus;
	@UiField ListPanel panelStatus;
	@UiField Button more;	
	private DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
	private int currentPage = 1;
	
	interface StatusPageUiBinder extends UiBinder<Widget, StatusPage> {
	}

	public StatusPage() {
		initWidget(uiBinder.createAndBindUi(this));	
		getLastStatus();
		getStatus(currentPage);
	}
	
	@UiHandler("post")
	void Post(ClickEvent e) {
		final String text = textField.getText();
		if(text != null && text.length()>0){
			ServicesLocator.serviceRSE.updateStatus(text, new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					EventBus.getInstance().fireEvent(new ErrorEvent(caught));
	            }
				public void onSuccess(String result) {
						labelStatus.setText(result);
						Date date = new Date();
						ListItem li = new ListItem();
						Label la = new Label("Le "+ fmt.format(date) + " : " + result);
					    li.add(la);
					    panelStatus.insert(li, 3);					    
				}
			});
		}
		textField.setText("");	
	}
	
	public void getLastStatus(){
		ServicesLocator.serviceRSE.getLastStatusService(new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
			}
			public void onSuccess(String result) {
				labelStatus.setText(result);
			}
		});
	}
	
	@UiHandler("more")
	void MoreButton(ClickEvent e){
		currentPage++;
		getStatus(currentPage);
	}
	
	public void getStatus(int step){
		ServicesLocator.serviceRSE.getStatus(step, new AsyncCallback<List<StatusDTO>>(){
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));				
			}
			public void onSuccess(List<StatusDTO> result) {				
				Iterator<StatusDTO> iResult = result.iterator();
				while (iResult.hasNext()) {
					StatusDTO statusDTO = (StatusDTO) iResult.next();					
					if (isStatusNotDisplay(statusDTO.getId())) {
						// ajout les status non affichés (cas du post ajouté, puis navigation dans les précédents)
						ListItem li = new ListItem();
						Label la = new Label("Le "+ fmt.format(statusDTO.getCreationDate()) + " : " + statusDTO.getDescription());
						li.getElement().setId(String.valueOf(statusDTO.getId()));
						li.add(la);
						panelStatus.add(li);
					}					
				}			
			}
		});
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
}