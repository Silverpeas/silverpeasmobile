/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author: svu
 */
@Deprecated
public class PopinConfirmation extends PopupPanel implements ClickHandler{

  private Command callback;

  public PopinConfirmation(String message) {
    //HTML content = new HTML();
    FlowPanel content = new FlowPanel();
    Label msg = new Label();
    msg.setText(message);
    content.add(msg);

    FlowPanel buttons = new FlowPanel();
    buttons.setStylePrimaryName("popin-btns");
    Anchor yes = new Anchor();
    yes.setStylePrimaryName("popin-btn");
    yes.setText("Oui");
    yes.addClickHandler(this);
    buttons.add(yes);
    Anchor no = new Anchor();
    no.setStylePrimaryName("popin-btn");
    no.setText("Non");
    no.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent clickEvent) {
        hide();
      }
    });

    buttons.add(no);
    content.add(buttons);
    setWidget(content);
    recenter();
    getElement().setAttribute("style", getElement().getAttribute("style") + " height: -webkit-fill-available;");
  }

  @Override
  public void show() {
    super.show();
    int popupHeight = getElement().getFirstChildElement().getOffsetHeight();
    int popupPaddingTop = Integer.parseInt(getElement().getStyle().getPaddingTop().replace("px",""));
    getElement().getStyle().setPaddingTop(popupPaddingTop - (popupHeight/2) , Style.Unit.PX);
  }

  public void recenter() {
    getElement().getStyle().setPaddingTop(getElement().getAbsoluteTop() + (Window.getClientHeight() /2), Style.Unit.PX);
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
