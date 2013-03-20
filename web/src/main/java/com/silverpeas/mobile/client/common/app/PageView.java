package com.silverpeas.mobile.client.common.app;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;

public class PageView extends com.gwtmobile.ui.client.page.Page {
	

	protected boolean clicked = false;

	protected void clickGesture(Command call) {
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
