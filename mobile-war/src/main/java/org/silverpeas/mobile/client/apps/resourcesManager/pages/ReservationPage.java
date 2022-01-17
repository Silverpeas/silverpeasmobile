/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.resourcesManager.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.AddReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.AbstractResourcesManagerPagesEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.ResourcesManagerPagesEventHandler;
import org.silverpeas.mobile.client.apps.resourcesManager.resources.ResourcesManagerMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.Popin;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.reservations.ReservationDTO;

import java.util.ArrayList;

public class ReservationPage extends PageContent implements ResourcesManagerPagesEventHandler {

  private static ResourcesManagerPageUiBinder uiBinder = GWT.create(ResourcesManagerPageUiBinder.class);

  @UiField(provided = true) protected ResourcesManagerMessages msg = null;

  @UiField
  Label labelTitle, labelStartDate, labelEndDate;

  @UiField
  TextBox start, end, title;

  @UiField
  TextArea reason;

  interface ResourcesManagerPageUiBinder extends UiBinder<Widget, ReservationPage> {
  }

  public ReservationPage() {
    msg = GWT.create(ResourcesManagerMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractResourcesManagerPagesEvent.TYPE, this);
    start.getElement().setAttribute("type", "datetime-local");
    end.getElement().setAttribute("type", "datetime-local");
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractResourcesManagerPagesEvent.TYPE, this);
  }

  @UiHandler("validate")
  protected void validate(ClickEvent event) {
    ArrayList<String> errors = new ArrayList<String>();
    if (title.getText().isEmpty()) {
      errors.add(labelTitle.getText());
    }
    if (start.getText().isEmpty()) {
      errors.add(labelStartDate.getText());
    }
    if (end.getText().isEmpty()) {
      errors.add(labelEndDate.getText());
    }
    if (!errors.isEmpty()) {
      String message = "";
      for (String error : errors) {
        message += error + ",";
      }
      message = message.substring(0, message.length() - 1) + " ";
      if (errors.size() == 1) {
        message += msg.mandatoryOneField();
      } else {
        message += msg.mandatory();
      }

      new Popin(message).show();
    } else {
      ReservationDTO dto = new ReservationDTO();
      dto.setEvenement(title.getText());
      dto.setStartDate(start.getText());
      dto.setEndDate(end.getText());
      dto.setReason(reason.getText());
      AddReservationEvent eventToApp = new AddReservationEvent();
      eventToApp.setData(dto);
      EventBus.getInstance().fireEvent(eventToApp);
    }
  }

}