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

package org.silverpeas.mobile.client.apps.orgchartgroup.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.contacts.events.pages.AbstractContactsPagesEvent;
import org.silverpeas.mobile.client.apps.contacts.pages.ContactPage;
import org.silverpeas.mobile.client.apps.orgchartgroup.events.app.AbstractOrgChartGroupAppEvent;
import org.silverpeas.mobile.client.apps.orgchartgroup.events.app.ContactLoadEvent;
import org.silverpeas.mobile.client.apps.orgchartgroup.events.ui.AbstractOrgChartGroupeUIEvent;
import org.silverpeas.mobile.client.apps.orgchartgroup.events.ui.ContactLoadedEvent;
import org.silverpeas.mobile.client.apps.orgchartgroup.events.ui.OrgChartGroupUIEventHandler;
import org.silverpeas.mobile.client.apps.orgchartgroup.pages.OrgChartGroupPage;
import org.silverpeas.mobile.client.apps.orgchartgroup.resources.OrgChartGroupMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.PropertyDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.orgchart.GroupOrgChartDTO;

public class OrgaChartUserItem extends Composite implements OrgChartGroupUIEventHandler {

  private static OrgChartUserItemUiBinder uiBinder = GWT.create(OrgChartUserItemUiBinder.class);

  private UserDTO data;

  @UiField HTMLPanel container;

  @UiField
  SpanElement name;

  @UiField
  DivElement infos;

  @UiField
  ImageElement avatar;

  @UiField
  Anchor link;

  @UiField(provided = true) protected OrgChartGroupMessages msg = null;

  interface OrgChartUserItemUiBinder extends UiBinder<Widget, OrgaChartUserItem> {
  }

  @UiHandler("link")
  protected void navigate(ClickEvent event) {
    EventBus.getInstance().fireEvent(new ContactLoadEvent(data.getId()));
  }

  public OrgaChartUserItem() {
    msg = GWT.create(OrgChartGroupMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractOrgChartGroupeUIEvent.TYPE, this);
  }

  @Override
  public void onContactLoaded(ContactLoadedEvent event) {
    if (event.getUser().getId().equals(data.getId())) {
      ContactPage page = new ContactPage();
      page.setData(event.getUser());
      page.show();
    }
  }

  public void setData(UserDTO data) {
    this.data = data;
    name.setInnerText(data.getFirstName() + " " + data.getLastName());
    avatar.setSrc("/silverpeas" + data.getAvatar());
    String html = "";
    for (PropertyDTO p : data.getProperties()) {
      if (p.getValue() != null && !p.getValue().isEmpty()) {
        html += "<div>" + p.getKey() + " : " + p.getValue() + "</div>";
      }
    }
    infos.setInnerHTML(html);
  }

  public void stop() {
    EventBus.getInstance().removeHandler(AbstractOrgChartGroupeUIEvent.TYPE, this);
  }
}
