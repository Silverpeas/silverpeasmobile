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
import com.google.gwt.user.client.ui.HTML;
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
import org.silverpeas.mobile.shared.dto.reservations.ReservationStatus;
import org.silverpeas.mobile.shared.dto.reservations.ResourceDTO;

import java.util.ArrayList;

public class ReservationDetailPage extends PageContent implements ResourcesManagerPagesEventHandler {

  private static ReservationDetailPageUiBinder uiBinder = GWT.create(ReservationDetailPageUiBinder.class);
  private ReservationDTO data;

  @UiField(provided = true) protected ResourcesManagerMessages msg = null;

  @UiField
  Label labelTitle, labelStartDate, labelEndDate, reason;

  @UiField
  HTML resources;

  public void setData(final ReservationDTO data) {
    this.data = data;
    labelTitle.setText(labelTitle.getText() + " : " + data.getEvenement());
    labelStartDate.setText(labelStartDate.getText() + " : " + data.getStartDate());
    labelEndDate.setText(labelEndDate.getText() + " : " +data.getEndDate());
    reason.setText(data.getReason());
    String html = "<ul class='reservationRessources'>";
    for (ResourceDTO res : data.getResources()) {
      String status = res.getReservationStatus();
      if (status.equals(ReservationStatus.A.toString())) status = "waitingForValidation";
      if (status.equals(ReservationStatus.V.toString())) status = "validated";
      html += "<li class='" + status + "'>" + res.getName() + "</li>";
    }
    html += "</ul>";
    resources.setHTML(html);
  }

  interface ReservationDetailPageUiBinder extends UiBinder<Widget, ReservationDetailPage> {
  }

  public ReservationDetailPage() {
    msg = GWT.create(ResourcesManagerMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractResourcesManagerPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractResourcesManagerPagesEvent.TYPE, this);
  }


}