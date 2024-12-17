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

package org.silverpeas.mobile.client.apps.orgchartgroup.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.orgchartgroup.pages.widgets.OrgaChartGroupItem;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.orgchart.GroupOrgChartDTO;

public class OrgChartGroupPage extends PageContent {

  private static OrgChartGroupPageUiBinder uiBinder = GWT.create(OrgChartGroupPageUiBinder.class);

  private GroupOrgChartDTO data;

  @UiField(provided = true) protected ApplicationMessages msg = null;
  @UiField
  UnorderedList units;

  public void setData(GroupOrgChartDTO groupOrgChartDTO) {
    this.data = groupOrgChartDTO;
    setTitle(data.getName());
    data.getSubGroups().forEach(group -> {
      OrgaChartGroupItem item = new OrgaChartGroupItem();
      item.setName(group.getName());
      units.add(item);
    });

  }

  interface OrgChartGroupPageUiBinder extends UiBinder<Widget, OrgChartGroupPage> {
  }

  public OrgChartGroupPage() {
    msg = GWT.create(ApplicationMessages.class);
    setPageTitle(msg.notifications());
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void stop() {
    super.stop();

  }
}