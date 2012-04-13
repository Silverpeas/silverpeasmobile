/**
 * Copyright (C) 2000 - 2011 Silverpeas
 *
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.client.apps.status.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.HeaderPanel;
import com.gwtmobile.ui.client.widgets.TextArea;
import com.silverpeas.mobile.client.apps.status.events.controller.StatusPostEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;

public class PostPage extends Page implements View {

  private static PostPageUiBinder uiBinder = GWT.create(PostPageUiBinder.class);

  @UiField
  TextArea textField;
  @UiField
  HeaderPanel header;

  interface PostPageUiBinder extends UiBinder<Widget, PostPage> {
  }

  public PostPage() {
    initWidget(uiBinder.createAndBindUi(this));
    textField.setHeight(Window.getClientHeight() / 2 + "px");
    header.setRightButtonClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        final String text = textField.getText();
        EventBus.getInstance().fireEvent(new StatusPostEvent(text));
        goBack(null);
      }
    });
  }

  @Override
  public void stop() {
  }
}
