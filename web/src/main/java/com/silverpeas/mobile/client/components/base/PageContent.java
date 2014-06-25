package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.navigation.PageHistory;

public class PageContent extends Composite {

	protected boolean clicked = false;
	
	public void goTo(PageContent page) {
		PageHistory.getInstance().goTo(page);		
		SpMobil.mainPage.setContent(page);
	}
	
	public void goBack(Object object) {
		// TODO Auto-generated method stub		
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
