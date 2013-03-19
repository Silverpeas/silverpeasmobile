package com.silverpeas.mobile.client.common.app;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;

public class PageView extends com.gwtmobile.ui.client.page.Page {
	@Override
	protected void initWidget(Widget widget) {		
		super.initWidget(widget);
		if (MobilUtils.isRetina() && MobilUtils.isPhoneGap() == false) {
			addStyleName("webappIosRetina");
		} else if (MobilUtils.isIOS() && MobilUtils.isPhoneGap() == false) {			
			addStyleName("webappIos");
		}
	}

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
