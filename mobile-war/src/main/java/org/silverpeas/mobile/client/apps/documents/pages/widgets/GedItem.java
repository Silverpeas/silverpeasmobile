/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.client.apps.documents.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemClickEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.documents.TopicDTO;

public class GedItem extends Composite {

  private Object data;
  private TopicDTO dataTopic;
  private PublicationDTO dataPublication;
  private static GedItemUiBinder uiBinder = GWT.create(GedItemUiBinder.class);
  @UiField Anchor link;
  protected ApplicationMessages msg = null;


  interface GedItemUiBinder extends UiBinder<Widget, GedItem> {
  }

  public GedItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(Object data) {
    this.data = data;
    if (data instanceof TopicDTO) {
      dataTopic = (TopicDTO) data;
      if (dataTopic.getId().equals("1")) {
        setStyleName("trash");
        link.setText(dataTopic.getName());
      } else {
        setStyleName("folder-ged");
        link.setText(dataTopic.getName() + " (" + dataTopic.getPubCount() + ")");
      }
    } else if (data instanceof PublicationDTO) {
      dataPublication = (PublicationDTO) data;
      link.setHTML(dataPublication.getName());
      setStyleName("publication");
    }
    link.setStyleName("ui-btn ui-btn-icon-right ui-icon-carat-r");
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    EventBus.getInstance().fireEvent(new GedItemClickEvent(data));
  }
}
