package com.silverpeas.mobile.client.apps.almanach.pages;

import com.google.gwt.user.client.ui.IsWidget;
import com.gwtmobile.ui.client.page.Page;

public interface EventView extends IsWidget{
	void setLauncher(Launcher launcher);
	
	public interface Launcher {
        void goTo(Page page);
    }
}
