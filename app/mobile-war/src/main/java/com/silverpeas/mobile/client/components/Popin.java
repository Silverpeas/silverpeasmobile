package com.silverpeas.mobile.client.components;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author: svu
 */
public class Popin extends PopupPanel {


    public Popin(String message) {
        setWidget(new Label(message));
        center();
        setAutoHideEnabled(true);

        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
            @Override
            public boolean execute() {
                hide();
                return false;
            }
        }, 2000);


    }
}
