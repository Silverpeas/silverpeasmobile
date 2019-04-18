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

package org.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.client.resources.ApplicationResources;

public class PageHeader extends Composite {

  private static PageHeaderUiBinder uiBinder = GWT.create(PageHeaderUiBinder.class);

  interface PageHeaderUiBinder extends UiBinder<Widget, PageHeader> {
  }

  @UiField protected HTMLPanel header;
  @UiField protected Anchor menu, back;
  @UiField protected HeadingElement title;
  @UiField(provided = true) protected ApplicationMessages msg = null;
  protected ApplicationResources ressources = null;

  public PageHeader() {
    msg = GWT.create(ApplicationMessages.class);
    ressources = GWT.create(ApplicationResources.class);
    initWidget(uiBinder.createAndBindUi(this));
    header.getElement().setId("header");
    menu.getElement().setId("menu");
    ressources.css().ensureInjected();

    title.setInnerText(ResourcesManager.getLabel("mainpage.title"));
  }


  @UiHandler("menu")
  void onMenu(ClickEvent event) {
    SpMobil.getMainPage().toogleMenu();
  }

  @UiHandler("back")
  void onBack(ClickEvent event) {
    PageHistory.getInstance().back();
  }

  public void showOfflineIndicator() {
    menu.addStyleName(ressources.css().offline());
  }

  public void hideOfflineIndicator() {
    menu.removeStyleName(ressources.css().offline());
  }

  public void setVisibleBackButton(boolean visible) {
    // remove active state
    header.remove(back);
    header.add(back);
    // hide button
    back.setVisible(visible);
  }

  public void setPageTitle(String title) {
    this.title.setInnerHTML(title);
  }

  public int getHeight() {
    return header.getOffsetHeight();
  }

}
