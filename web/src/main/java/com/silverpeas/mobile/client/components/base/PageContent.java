package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.silverpeas.mobile.client.common.navigation.PageHistory;

public class PageContent extends Composite {

	protected boolean clicked = false;
	protected String pageTitle = "Silverpeas";
	
	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public void goTo(PageContent page) {
		PageHistory.getInstance().goTo(page);
	}
	
	public void goBack(Object object) {
		// TODO Auto-generated method stub		
	}
	
	public void back() {
		PageHistory.getInstance().back();
	}

	public void clickGesture(Command call) {
		if (!clicked) {
			clicked = true;
			call.execute();			
			Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
				@Override
				public boolean execute() {
					clicked = false;					
					return false;
				}}, 400);	
		}		
	}
}
