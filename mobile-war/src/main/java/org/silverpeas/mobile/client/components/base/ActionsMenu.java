/*
 * Copyright (C) 2000 - 2019 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.components.base;

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
import org.silverpeas.mobile.client.components.UnorderedList;

/**
 * @author: svu
 */
public class ActionsMenu  extends Composite {

    interface ActionsMenuUiBinder extends UiBinder<Widget, ActionsMenu> {
    }

    private static ActionsMenuUiBinder uiBinder = GWT.create(ActionsMenuUiBinder.class);

    @UiField HTMLPanel container;
    @UiField
    UnorderedList listActions;
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
        return (listActions.getWidgetCount() == 0);
    }

    public void addAction(ActionItem action) {
        for (int i = 0; i < listActions.getWidgetCount(); i++) {
          ActionItem act = (ActionItem) listActions.getWidget(i);
          if (act.getId().equals(action.getId())) return;
        }
        listActions.add(action);
        container.getElement().getStyle().setDisplay(Style.Display.BLOCK);
    }

    public void removeAction(String id, boolean silently) {
      for (int i = 0; i < listActions.getWidgetCount(); i++) {
        ActionItem act = (ActionItem) listActions.getWidget(i);
        if (act.getId().equals(id)) {
          listActions.remove(act);
          if (!silently) showActions(null);
          break;
        }
      }
      if (isEmpty()) {
        container.getElement().getStyle().setDisplay(Style.Display.NONE);
      }
    }
}