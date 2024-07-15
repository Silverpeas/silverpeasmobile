/*
 * Copyright (C) 2000 - 2024 Silverpeas
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author: svu
 */
public class PopinInformation extends PopupPanel implements ClickHandler{

  private Command callback;

  public PopinInformation(String message) {
    FlowPanel content = new FlowPanel();
    Label msg = new Label();
    msg.setText(message);
    content.add(msg);

    FlowPanel buttons = new FlowPanel();
    buttons.setStylePrimaryName("popin-btns");
    Anchor ok = new Anchor();
    ok.setStylePrimaryName("popin-btn");
    ok.setText("Ok");
    ok.addClickHandler(this);
    buttons.add(ok);

    content.add(buttons);

    setWidget(content);
    center();
  }

  public void setYesCallback(Command callback) {
    this.callback = callback;
  }

  @Override
  public void onClick(final ClickEvent clickEvent) {
    if (callback != null) callback.execute();
    hide();
  }
}
