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

package org.silverpeas.mobile.client.apps.navigation.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.navigation.events.pages.ClickItemEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

public class NavigationItem extends Composite {

  private SilverpeasObjectDTO data;
  private static NavigationItemUiBinder uiBinder = GWT.create(NavigationItemUiBinder.class);
  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  @UiField Anchor link;
  @UiField InlineHTML icon;

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
      link.setStyleName("ui-btn ui-btn-icon-right ui-icon-carat-r");
    } else {
      String type = ((ApplicationInstanceDTO) data).getType();
      if (type.equalsIgnoreCase(Apps.kmelia.name())) {
        setStyleName("app-ged");
        icon.setHTML(resources.ged().getText());
      } else if (type.equalsIgnoreCase(Apps.gallery.name())) {
        setStyleName("app-medialib");
        icon.setHTML(resources.mediaLib().getText());
      } else if (type.equalsIgnoreCase(Apps.quickinfo.name())) {
        setStyleName("app-actuality");
        icon.setHTML(resources.news().getText());
      } else if (type.equalsIgnoreCase(Apps.webPages.name())) {
        setStyleName("app-pageWeb");
        icon.setHTML(resources.webpage().getText());
      } else if (type.equalsIgnoreCase(Apps.blog.name())) {
        setStyleName("app-blog");
        icon.setHTML(resources.blog().getText());
      } else if (((ApplicationInstanceDTO) data).getWorkflow()) {
        setStyleName("app-workflow");
        icon.setHTML(resources.workflow().getText());
      } else if (type.equalsIgnoreCase(Apps.hyperlink.name())) {
        setStyleName("app-link");
        icon.setHTML(resources.link().getText());
      } else if (type.equalsIgnoreCase(Apps.almanach.name())) {
        setStyleName("app-almanach");
        icon.setHTML(resources.calendar().getText());
      } else if (type.equalsIgnoreCase(Apps.formsOnline.name())) {
        setStyleName("app-formsOnline");
        icon.setHTML(resources.form().getText());
      } else if (type.equalsIgnoreCase(Apps.classifieds.name())) {
        setStyleName("app-classifieds");
        icon.setHTML(resources.classifieds().getText());
      } else if (type.equalsIgnoreCase(Apps.survey.name())) {
        setStyleName("app-survey");
        icon.setHTML(resources.quizz().getText());
      } else if (type.equalsIgnoreCase(Apps.pollingStation.name())) {
        setStyleName("app-polling");
        icon.setHTML(resources.poll().getText());
      } else if (type.equalsIgnoreCase(Apps.questionReply.name())) {
        setStyleName("app-faq");
        icon.setHTML(resources.faq().getText());
      } else if (type.equalsIgnoreCase(Apps.resourcesManager.name())) {
        setStyleName("app-resourcesManager");
        icon.setHTML(resources.bookonline().getText());
      } else if(type.equalsIgnoreCase(Apps.orgchartGroup.name())) {
        setStyleName("app-orgaChartGroup");
        icon.setHTML(resources.orgchartGroup().getText());
      }
      link.setStyleName("ui-btn ui-icon-carat-r");
    }
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    EventBus.getInstance().fireEvent(new ClickItemEvent(data));
  }
}
