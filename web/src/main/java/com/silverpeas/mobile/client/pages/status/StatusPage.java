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
	private ArrayList<String> stringA = new ArrayList<String>();
	private ArrayList<Date> stringD = new ArrayList<Date>();
	private DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
	
	interface StatusPageUiBinder extends UiBinder<Widget, StatusPage> {
	}

	public StatusPage() {
		initWidget(uiBinder.createAndBindUi(this));	
		getLastStatus();
		getAllStatus();
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
	
	public void getAllStatus(){
		ServicesLocator.serviceRSE.getAllStatus(new AsyncCallback<Map<Date, String>>(){
			public void onFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new ErrorEvent(caught));
				Window.alert(caught.toString());
			}
			public void onSuccess(Map<Date, String> result) {
				for (Iterator<Date> i = result.keySet().iterator() ; i.hasNext() ; ){
				    Date date = (Date)i.next();
				    String description = result.get(date);
				    
				    ListItem li = new ListItem();
				    Label la = new Label("Le "+ fmt.format(date) + " : " + description);
				    li.add(la);
					panelStatus.add(li);
					
				    /*stringA.add(description);
				    stringD.add(date);*/
				}
				for(int i=stringA.size();i>=0;i--){
					ListItem li = new ListItem();
				    Label la = new Label("Le "+ fmt.format(stringD.get(i)) + " : " + stringA.get(i));
				    li.add(la);
					panelStatus.add(li);
				}
			}
		});
	}
}