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
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationReadenEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.resources.NotificationsMessages;
import org.silverpeas.mobile.client.apps.orgchartgroup.OrgChartGroupApp;
import org.silverpeas.mobile.client.apps.orgchartgroup.pages.OrgChartGroupPage;
import org.silverpeas.mobile.client.apps.orgchartgroup.resources.OrgChartGroupMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.widgets.SelectableItem;
import org.silverpeas.mobile.shared.dto.PropertyDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationBoxDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationSendedDTO;
import org.silverpeas.mobile.shared.dto.orgchart.GroupOrgChartDTO;

public class OrgaChartGroupItem extends Composite {

  private static OrgChartGroupItemUiBinder uiBinder = GWT.create(OrgChartGroupItemUiBinder.class);

  private GroupOrgChartDTO data;

  @UiField HTMLPanel container;

  @UiField
  SpanElement name, bossPlace;

  @UiField
  Anchor link;

  @UiField(provided = true) protected OrgChartGroupMessages msg = null;


  interface OrgChartGroupItemUiBinder extends UiBinder<Widget, OrgaChartGroupItem> {
  }

  @UiHandler("link")
  protected void navigate(ClickEvent event) {
    OrgChartGroupPage page = new OrgChartGroupPage();
    page.setPageTitle(name.getInnerText());
    page.setData(data);
    page.show();
  }

  public OrgaChartGroupItem() {
    msg = GWT.create(OrgChartGroupMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(GroupOrgChartDTO data) {
    this.data = data;
    name.setInnerText(data.getName());
    String htmlBoss = "";
    for (UserDTO boss : data.getBoss()) {
      String label = "";
      for (PropertyDTO prop : boss.getProperties()) {
        if (prop.getKey().equalsIgnoreCase("bossTitle")) {
          label = prop.getValue();
        }
      }
      htmlBoss += "<div class='label-boss'>" + label + "</div><div class='boss'><img src='/silverpeas" + boss.getAvatar() + "'></img><span>" + boss.getFirstName() + " " + boss.getLastName() + "</span></div>";
    }
    bossPlace.setInnerHTML(htmlBoss);
  }
}
