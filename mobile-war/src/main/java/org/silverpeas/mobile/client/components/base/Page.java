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

package org.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.navigation.pages.NavigationPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.components.base.events.EndPageEvent;
import org.silverpeas.mobile.client.components.homepage.HomePageNewsSlider;
import org.silverpeas.mobile.client.pages.main.HomePage;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

import java.util.List;

public class Page extends Composite implements Window.ScrollHandler {

  private static PageUiBinder uiBinder = GWT.create(PageUiBinder.class);

  interface PageUiBinder extends UiBinder<Widget, Page> {
  }

  @UiField protected SimplePanel contentPlace, contentPlaceTmp;
  @UiField protected PageHeader header;
  @UiField protected NavigationMenu menu;
  @UiField protected DivElement contentContainer;
  @UiField protected PageFooter footer;
  @UiField protected HTMLPanel container;

  protected PageContent content;

  public Page() {
    initWidget(uiBinder.createAndBindUi(this));
    Window.addWindowScrollHandler(this);
    container.getElement().setId("home");
  }

  public DivElement getContentContainer() {
    return contentContainer;
  }

  public void setContent(PageContent content, boolean newContent) {
    if (this.content != null && this.content.hashCode() == content.hashCode()) return;
    String transitionClass;
    if (newContent) {
      transitionClass = "opening-page";
    } else {
      transitionClass = "closing-page";
    }

    getContentContainer().addClassName(transitionClass);
    contentPlaceTmp.setWidget(content);
    Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
      @Override
      public boolean execute() {
        displayContent(content);
        getContentContainer().removeClassName(transitionClass);
        return false;
      }
    }, 300);
  }

  private void displayContent(PageContent content) {
    this.content = content;
    //contentPlaceTmp.remove(content);
    contentPlace.setWidget(content);
    header.setPageTitle(content.getPageTitle());
    header.setVisibleBackButton(PageHistory.getInstance().size() > 1);

    if (content instanceof HomePage) {
      HomePageNewsSlider.getInstance().setCurrentHomePageContent(((HomePage) content).getContent());
    } else if (content instanceof NavigationPage) {
      HomePageNewsSlider.getInstance().setCurrentHomePageContent(((NavigationPage) content).getContent());
    }
  }

  public void toogleMenu() {
    menu.toogleMenu();
  }

  public void closeMenu() {
    menu.closeMenu();
    header.closeMenuButton();
  }

  public void setUser(DetailUserDTO user) {
    menu.setUser(user);
  }

  public void resetSearchField() {
    menu.resetSearchField();
  }

  public void hideFooter() {
    footer.setVisible(false);
  }

  public void showFooter() {
    footer.setVisible(true);
  }

  @Override
  public void onWindowScroll(final Window.ScrollEvent event) {
    if (Document.get().getScrollTop() + Document.get().getClientHeight() >= Document.get().getScrollHeight()) {
      EventBus.getInstance().fireEvent(new EndPageEvent());
   }
  }

  public int getHeaderHeight() {
    return header.getHeight();
  }
  public int getFooterHeight() { return footer.getOffsetHeight() ; }

  public PageHeader getHeader() {
    return header;
  }
  public void showOfflineIndicator() {
    header.showOfflineIndicator();
  }

  public void hideOfflineIndicator() {
    header.hideOfflineIndicator();
  }

  public void setPersonalApps(List<ApplicationInstanceDTO> applicationInstanceDTOS) {
    menu.setPersonalApps(applicationInstanceDTOS);
  }

  public boolean isPersonalAppsInitialized() {
    return menu.isPersonalAppsInitialized();
  }

  public void freeze() {
    container.getElement().addClassName("freeze");
  }

  public void unfreeze() {
    container.getElement().removeClassName("freeze");
  }

}
