package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.components.UnorderedList;

/**
 * @author: svu
 */
public class ActionsMenu  extends Composite {

    interface ActionsMenuUiBinder extends UiBinder<Widget, ActionsMenu> {
    }

    private static ActionsMenuUiBinder uiBinder = GWT.create(ActionsMenuUiBinder.class);

    @UiField HTMLPanel container;
    @UiField UnorderedList listActions;
    @UiField Button actions;

    public ActionsMenu() {
        initWidget(uiBinder.createAndBindUi(this));
        listActions.setId("action-bloc");
        actions.getElement().setId("action-button");
        container.getElement().setId("actions");
        container.getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    @UiHandler("actions")
    protected void showActions(ClickEvent event) {
        if (listActions.getStyleDisplay().equalsIgnoreCase("none") || listActions.getStyleDisplay().isEmpty()) {
            listActions.setStyledisplay(Style.Display.BLOCK);
        } else {
            listActions.setStyledisplay(Style.Display.NONE);
        }
    }

    public boolean isEmpty() {
        return listActions.isEmpty();
    }

    public void addAction(Widget action) {
        listActions.add(action);
        container.getElement().getStyle().setDisplay(Style.Display.BLOCK);
    }
}