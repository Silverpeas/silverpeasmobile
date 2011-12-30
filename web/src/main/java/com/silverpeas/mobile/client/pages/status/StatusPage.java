package com.silverpeas.mobile.client.pages.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
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

public class StatusPage extends Page {
	
	private static StatusPageUiBinder uiBinder = GWT.create(StatusPageUiBinder.class);
	
	@UiField TextBox textField;
	@UiField Button post;
	@UiField Label labelStatus;
	@UiField ListPanel panelStatus;
	@UiField Button more;
	private ArrayList<String> listDescription;
	private ArrayList<Date> listDate;
	private DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
	
	interface StatusPageUiBinder extends UiBinder<Widget, StatusPage> {
	}

	public StatusPage() {
		initWidget(uiBinder.createAndBindUi(this));	
		getLastStatus();
		getStatus(0);
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
		getStatus(1);
	}
	
	public void getStatus(int indicator){
		ServicesLocator.serviceRSE.getStatus(indicator, new AsyncCallback<Map<Date, String>>(){
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
				Window.alert(caught.toString());
			}
			public void onSuccess(Map<Date, String> result){
				listDescription = new ArrayList<String>();
				listDate = new ArrayList<Date>();
				for (Iterator<Date> i = result.keySet().iterator() ; i.hasNext() ; ){
					Date date = (Date)i.next();
				    String description = result.get(date);
				    
				    listDate.add(date);
				    listDescription.add(description);
				}
				for(int i=listDate.size()-1;i>=0;i--){
					ListItem li = new ListItem();
				    Label la = new Label("Le "+ fmt.format(listDate.get(i)) + " : " + listDescription.get(i));
				    li.add(la);
					panelStatus.add(li);
				}
			}
		});
	}
}