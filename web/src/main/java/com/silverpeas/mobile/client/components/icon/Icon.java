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

package com.silverpeas.mobile.client.components.icon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtmobile.ui.client.widgets.VerticalPanel;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class Icon extends Composite implements HasText, HasClickHandlers, ClickHandler {

  private VerticalPanel grid = new VerticalPanel();
  private SimplePanel image = new SimplePanel();
  private Label label = new Label();
  private ClickHandler handler = null;
  private ApplicationResources res = GWT.create(ApplicationResources.class);

  public Icon() {
    label.setHorizontalAlignment(Label.ALIGN_CENTER);
    grid.setSecondaryStyle(res.css().icon());
    grid.addDomHandler(this, ClickEvent.getType());
    grid.add(image);
    grid.add(label);
    initWidget(grid);
  }

  public String getText() {
    return label.getText();
  }

  public void setText(String text) {
    label.setText(text);
  }

  public void setHeight(String height) {
    grid.setHeight(height);
  }

  @Override
  public void setStyleName(String style) {
    image.setStyleName(style);
  }

  /**
   * Permet de s'abonner au clique sur l'icone.
   */
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    this.handler = handler;
    return new IconHandlerRegistration();
  }

  /**
   * Permet de se désabonner au clique sur l'icone.
   * @author svuillet
   */
  public class IconHandlerRegistration implements HandlerRegistration {
    public IconHandlerRegistration() {
      super();
    }

    public void removeHandler() {
      handler = null;
    }
  }

  public void onClick(ClickEvent event) {
    image.addStyleName(res.css().selected());
    if (handler != null)
      handler.onClick(event);
    Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

      @Override
      public boolean execute() {
        image.removeStyleName(res.css().selected());
        return false;
        }
    }, 300);

  }
}
