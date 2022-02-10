/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.navigation.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.navigation.events.pages.ClickItemEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

public class NavigationItem extends Composite {

  private SilverpeasObjectDTO data;
  private static NavigationItemUiBinder uiBinder = GWT.create(NavigationItemUiBinder.class);
  @UiField Anchor link;
  protected ApplicationMessages msg = null;


  interface NavigationItemUiBinder extends UiBinder<Widget, NavigationItem> {
  }

  public NavigationItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(SilverpeasObjectDTO data) {
    this.data = data;
    link.setText(data.getLabel());
    if (data instanceof SpaceDTO) {
      if (((SpaceDTO)data).isPersonal()) {
        link.setText(msg.personalSpace());
      }
    } else {
      String type = ((ApplicationInstanceDTO) data).getType();
      if (type.equalsIgnoreCase(Apps.kmelia.name())) {
        setStyleName("folder-ged");
      } else if (type.equalsIgnoreCase(Apps.gallery.name())) {
        setStyleName("folder-galery");
      } else if (type.equalsIgnoreCase(Apps.quickinfo.name())) {
        setStyleName("app-actuality");
      } else if (type.equalsIgnoreCase(Apps.webPages.name())) {
        setStyleName("app-pageWeb");
      } else if (type.equalsIgnoreCase(Apps.blog.name())) {
        setStyleName("app-blog");
      } else if (((ApplicationInstanceDTO) data).isWorkflow()) {
        setStyleName("app-workflow");
      } else if (type.equalsIgnoreCase(Apps.hyperlink.name())) {
        setStyleName("app-link");
      } else if (type.equalsIgnoreCase(Apps.almanach.name())) {
        setStyleName("app-almanach");
      } else if (type.equalsIgnoreCase(Apps.formsOnline.name())) {
        setStyleName("app-formsOnline");
      } else if (type.equalsIgnoreCase(Apps.classifieds.name())) {
        setStyleName("app-classifieds");
      } else if (type.equalsIgnoreCase(Apps.survey.name())) {
        setStyleName("app-survey");
      } else if (type.equalsIgnoreCase(Apps.pollingStation.name())) {
        setStyleName("app-polling");
      } else if (type.equalsIgnoreCase(Apps.questionReply.name())) {
        setStyleName("app-faq");
      } else if (type.equalsIgnoreCase(Apps.resourcesManager.name())) {
        setStyleName("app-resourcesManager");
      }
    }
    link.setStyleName("ui-btn ui-btn-icon-right ui-icon-carat-r");
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    EventBus.getInstance().fireEvent(new ClickItemEvent(data));
  }
}
