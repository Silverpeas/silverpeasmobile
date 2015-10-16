package com.silverpeas.mobile.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author: svu
 */
public class HTMLPanelClickable extends HTMLPanel implements HasClickHandlers {
    public HTMLPanelClickable(String html) {
        super(html);
    }

    public HTMLPanelClickable(String tag, String html) {
        super(tag, html);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}
