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

package org.silverpeas.mobile.client.apps.agenda.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.network.OfflineHelper;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventAttendeeDTO;
import org.silverpeas.mobile.shared.dto.almanach.ParticipationStatusDTO;
import org.silverpeas.mobile.shared.dto.documents.AttachmentDTO;

public class AttendeeItem extends Composite {

    private static AttendeeUiBinder uiBinder = GWT.create(AttendeeUiBinder.class);
    private CalendarEventAttendeeDTO data;

    @UiField(provided = true) protected AgendaMessages msg = null;

    @UiField
    HTMLPanel container;

    interface AttendeeUiBinder extends UiBinder<Widget, AttendeeItem> {
    }

    public AttendeeItem() {
        msg = GWT.create(AgendaMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setData(CalendarEventAttendeeDTO data) {
      this.data = data;

      if (data.getParticipationStatus().equals(ParticipationStatusDTO.ACCEPTED)) {
        container.getElement().setInnerText(data.getFullName());
      } else {
        container.setStyleName("pending");

        String status = "";
        if (data.getParticipationStatus().equals(ParticipationStatusDTO.ACCEPTED)) {
          status = msg.ACCEPTED();
        } else if (data.getParticipationStatus().equals(ParticipationStatusDTO.AWAITING)) {
          status = msg.AWAITING();
        } else if (data.getParticipationStatus().equals(ParticipationStatusDTO.DECLINED)) {
          status = msg.DECLINED();
        } else if (data.getParticipationStatus().equals(ParticipationStatusDTO.TENTATIVE)) {
          status = msg.TENTATIVE();
        }
        container.getElement().setInnerText(data.getFullName() + " (" + status + ")");
      }

    }
}
