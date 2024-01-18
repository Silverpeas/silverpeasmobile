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

package org.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.profile.ProfileApp;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Html5Utils;
import org.silverpeas.mobile.client.common.animation.RippleAnimation;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;

public class PageFooter extends Composite {

  private static PageFooterUiBinder uiBinder = GWT.create(PageFooterUiBinder.class);

  interface PageFooterUiBinder extends UiBinder<Widget, PageFooter> {
  }

  @UiField protected HTMLPanel footer;
  @UiField protected Anchor tchat, contact, tasks, favoris, browse;
  @UiField(provided = true) protected ApplicationMessages msg = null;
  protected ApplicationResources ressources = null;

  public PageFooter() {
    msg = GWT.create(ApplicationMessages.class);
    ressources = GWT.create(ApplicationResources.class);
    initWidget(uiBinder.createAndBindUi(this));

    footer.getElement().setId("navigation-footer");
    ressources.css().ensureInjected();

    tchat.setVisible(Boolean.parseBoolean(ResourcesManager.getParam("chat.enable")));
    addCustomButtons();
  }

  private void addCustomButtons() {
    int i = 1;
    String url = ResourcesManager.getParam("customButtonUrl" + i);
    while (!url.isEmpty()) {
      Anchor a = new Anchor();
      a.setHref(url);
      a.setStylePrimaryName("btn-custom" + i);
      footer.add(a);
      i++;
      url = ResourcesManager.getParam("customButtonUrl" + i);
    }
  }

  @UiHandler("browse")
  protected void browse(ClickEvent event) {
    NavigationMenu.goHome();
    RippleAnimation.play(event, 150);
  }

  @UiHandler("favoris")
  protected void goFavoris(ClickEvent event) {
    PageHistory.getInstance().goBackToFirst();
    ContentDTO content = new ContentDTO();
    content.setType(ContentsTypes.Favortis.toString());
    EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));
    RippleAnimation.play(event, 150);
  }

  @UiHandler("tasks")
  protected void goTasks(ClickEvent event) {
    PageHistory.getInstance().goBackToFirst();
    ContentDTO content = new ContentDTO();
    content.setType(ContentsTypes.Tasks.toString());
    EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));
    RippleAnimation.play(event, 150);
  }

  @UiHandler("contact")
  void contacts(ClickEvent event) {
    PageHistory.getInstance().goBackToFirst();
    ContentDTO content = new ContentDTO();
    content.setType(ContentsTypes.Contacts.toString());
    EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));
    RippleAnimation.play(event, 150);
  }

  public int getHeight() {
    return footer.getOffsetHeight();
  }
}
