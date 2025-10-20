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

package org.silverpeas.mobile.client.components.homepage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.checkerframework.checker.guieffect.qual.UI;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.navigation.pages.widgets.FavoriteItem;
import org.silverpeas.mobile.client.apps.navigation.pages.widgets.HomePageItem;
import org.silverpeas.mobile.client.apps.navigation.pages.widgets.NavigationItem;
import org.silverpeas.mobile.client.apps.navigation.pages.widgets.NewsItem;
import org.silverpeas.mobile.client.apps.navigation.pages.widgets.ShortCutItem;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.HomePageDTO;
import org.silverpeas.mobile.shared.dto.MyLinkDTO;
import org.silverpeas.mobile.shared.dto.ShortCutLinkDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.configuration.Config;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.util.List;

public class HomePageContent extends Composite implements SwipeEndHandler {

  private SwipeRecognizer swipeRecognizer;
  private static HomePageUiBinder uiBinder = GWT.create(HomePageUiBinder.class);
  private HomePageDTO data;
  private int currentNewsIndex = 0;
  private Config config = null;

  @UiField(provided = true)
  protected ApplicationMessages msg = null;

  @UiField
  UnorderedList favoris, lastPublications, spaces, news, lastEvents, shortcuts, shortcutstools;
  @UiField
  HTMLPanel container, lastPublicationsSection, lastEventsSection, favorisSection, shortCutsSection, shortCutsToolsSection, freeZoneSection, freeZoneThinSection;
  @UiField
  FocusPanel actus;

  @UiField
  HTML introduction;

  @UiField
  Image picture;

  interface HomePageUiBinder extends UiBinder<Widget, HomePageContent> {}

  public HomePageContent() {
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setAttribute("id", "homePageContent");
    actus.getElement().setAttribute("id", "actus");
    favorisSection.getElement().setAttribute("id", "favoris");
    lastPublicationsSection.getElement().setAttribute("id", "lastPublications");
    lastEventsSection.getElement().setAttribute("id", "lastEvents");
    shortCutsSection.getElement().setAttribute("id", "shortCuts");
    shortCutsToolsSection.getElement().setAttribute("id", "shortCutsTools");
    freeZoneSection.getElement().setAttribute("id", "freeZone");
    freeZoneThinSection.getElement().setAttribute("id", "freeZoneThin");
    Config conf = SpMobil.getConfiguration();
    setConfig(conf);
    EventBus.getInstance().addHandler(SwipeEndEvent.getType(), this);
  }

  public void setConfig(final Config config) {
    this.config = config;
    favorisSection.setVisible(config.isFavoritesDisplay());
    lastPublicationsSection.setVisible(config.isLastPublicationsDisplay());
    news.setVisible(config.isNewsDisplay());
    freeZoneSection.setVisible(config.isFreeZoneDisplay());
    freeZoneThinSection.setVisible(config.isFreeZoneThinDisplay());
    shortCutsSection.setVisible(config.isShortCutsDisplay());
    lastEventsSection.setVisible(config.isLastEventsDisplay());
    shortCutsToolsSection.setVisible(config.isShortCutsToolsDisplay());
  }

  public void setData(HomePageDTO data) {
    this.data = data;
    getElement().setClassName("space-content");
    getElement().setId(data.getId());

    if (data.getAuroraConfig() != null) {
      introduction.setHTML(data.getAuroraConfig().getIntroduction());
      picture.setUrl(data.getAuroraConfig().getPicture());
    }

    news.clear();
    if (config.isNewsDisplay()) {
      List<NewsDTO> newsDTOList = data.getNews();
      int i = 1;
      boolean v = true;
      int max = newsDTOList.size();
      for (NewsDTO newsDTO : newsDTOList) {
        NewsItem item = new NewsItem();
        item.setDisplayPager(data.getNewsDisplayer().equals(HomePageDTO.NEWS_DISPLAYER_CARROUSEL));
        item.setData(i, max, newsDTO);
        item.setVisible(v);
        news.add(item);
        i++;
        if (data.getNewsDisplayer().equals(HomePageDTO.NEWS_DISPLAYER_CARROUSEL)) v = false;
      }
    }

    spaces.clear();
    List<SilverpeasObjectDTO> spacesList = data.getSpacesAndApps();
    for (SilverpeasObjectDTO space : spacesList) {
      NavigationItem item = new NavigationItem();
      item.setData(space);
      item.getElement().setId(space.getId());
      spaces.add(item);
    }


    favoris.clear();
    List<MyLinkDTO> favoritesList = data.getFavorites();
    favorisSection.setVisible(!favoritesList.isEmpty() && config.isFavoritesDisplay());
    for (MyLinkDTO favoriteDTO : favoritesList) {
      if (favoriteDTO.getVisible()) {
        FavoriteItem item = new FavoriteItem();
        item.setMinimalView(true);
        item.setData(favoriteDTO);
        favoris.add(item);
      }
    }

    shortcutstools.clear();
    List<ShortCutLinkDTO> shortCutLinkList = data.getTools();
    shortCutsToolsSection.setVisible(!shortCutLinkList.isEmpty() && config.isShortCutsToolsDisplay());
    int i = 1;
    for (ShortCutLinkDTO shortCutLinkDTO : shortCutLinkList) {
      ShortCutItem item = new ShortCutItem();
      item.setData(shortCutLinkDTO);
      item.setCssId("shortCutTools" + i);
      shortcutstools.add(item);
      i++;
    }

    shortcuts.clear();
    shortCutLinkList = data.getShortCuts();
    shortCutsSection.setVisible(!shortCutLinkList.isEmpty() && config.isShortCutsDisplay());
    i = 1;
    for (ShortCutLinkDTO shortCutLinkDTO : shortCutLinkList) {
      ShortCutItem item = new ShortCutItem();
      item.setData(shortCutLinkDTO);
      item.setCssId("shortCut" + i);
      shortcuts.add(item);
      i++;
    }

    lastPublications.clear();
    List<PublicationDTO> publicationsList = data.getLastPublications();
    lastPublicationsSection.setVisible(!publicationsList.isEmpty() && config.isLastPublicationsDisplay());
    for (PublicationDTO publicationDTO : publicationsList) {
      HomePageItem item = new HomePageItem();
      item.setData(publicationDTO);
      lastPublications.add(item);
    }

    lastEvents.clear();
    List<CalendarEventDTO> eventsList = data.getLastEvents();
    lastEventsSection.setVisible(!eventsList.isEmpty() && config.isLastEventsDisplay());
    for (CalendarEventDTO eventDTO : eventsList) {
      HomePageItem item = new HomePageItem();
      item.setData(eventDTO);
      lastEvents.add(item);
    }

    freeZoneSection.clear();
    HTML html = new HTML(data.getHtmlFreeZone());
    freeZoneSection.add(html);

    freeZoneThinSection.clear();
    HTML html2 = new HTML(data.getHtmlFreeZoneThin());
    freeZoneThinSection.add(html2);

    if (MobilUtils.isMobil() && data.getNewsDisplayer().equals(HomePageDTO.NEWS_DISPLAYER_CARROUSEL)) {
      //Element e = Document.get().getElementById("actus");
      //HTML actus = HTML.wrap(e);
      swipeRecognizer = new SwipeRecognizer(actus);
    }

    // Display zones order
    reorderZones(data);

    Notification.activityStop();
  }

  private void reorderZones(final HomePageDTO data) {
    Boolean reorder = Boolean.parseBoolean(ResourcesManager.getParam("homepage.zone.changeorder"));
    if (reorder && data.getId().equalsIgnoreCase("root")) {
      container.getElement().setAttribute("style", "display:flex; flex-direction:column;");
      String spacesOrder = ResourcesManager.getParam("homepage.spaces.order");
      String freeZoneOrder = ResourcesManager.getParam("homepage.freeZone.order");
      String newsOrder = ResourcesManager.getParam("homepage.news.order");
      String favoritesOrder = ResourcesManager.getParam("homepage.favorites.order");
      String shortcutstoolsOrder = ResourcesManager.getParam("homepage.shortcutstools.order");
      String shortcutsOrder = ResourcesManager.getParam("homepage.shortcuts.order");
      String lastPublicationsOrder = ResourcesManager.getParam("homepage.lastPublications.order");
      String lastEventsOrder = ResourcesManager.getParam("homepage.lastEvents.order");
      String freeZoneThinSectionOrder = ResourcesManager.getParam("homepage.freeZoneThinSection.order");

      container.getElementById("spaces").getStyle().setProperty("order", spacesOrder);
      freeZoneSection.getElement().getStyle().setProperty("order", freeZoneOrder);
      freeZoneThinSection.getElement().getStyle().setProperty("order", freeZoneThinSectionOrder);
      lastPublicationsSection.getElement().getStyle().setProperty("order", lastPublicationsOrder);
      lastEventsSection.getElement().getStyle().setProperty("order", lastEventsOrder);
      shortCutsSection.getElement().getStyle().setProperty("order", shortcutsOrder);
      shortCutsToolsSection.getElement().getStyle().setProperty("order", shortcutstoolsOrder);
      favorisSection.getElement().getStyle().setProperty("order", favoritesOrder);
      actus.getElement().getStyle().setProperty("order", newsOrder);
    }
    reorder = Boolean.parseBoolean(ResourcesManager.getParam("spacehomepage.zone.changeorder"));
    if (reorder && !data.getId().equalsIgnoreCase("root")) {
      container.getElement().setAttribute("style", "display:flex; flex-direction:column;");
      String spacesOrder = ResourcesManager.getParam("spacehomepage.spaces.order");
      String freeZoneOrder = ResourcesManager.getParam("spacehomepage.freeZone.order");
      String newsOrder = ResourcesManager.getParam("spacehomepage.news.order");
      String favoritesOrder = ResourcesManager.getParam("spacehomepage.favorites.order");
      String shortcutstoolsOrder = ResourcesManager.getParam("spacehomepage.shortcutstools.order");
      String shortcutsOrder = ResourcesManager.getParam("spacehomepage.shortcuts.order");
      String lastPublicationsOrder = ResourcesManager.getParam("spacehomepage.lastPublications.order");
      String lastEventsOrder = ResourcesManager.getParam("spacehomepage.lastEvents.order");
      String freeZoneThinSectionOrder = ResourcesManager.getParam("spacehomepage.freeZoneThinSection.order");

      container.getElementById("spaces").getStyle().setProperty("order", spacesOrder);
      freeZoneSection.getElement().getStyle().setProperty("order", freeZoneOrder);
      freeZoneThinSection.getElement().getStyle().setProperty("order", freeZoneThinSectionOrder);
      lastPublicationsSection.getElement().getStyle().setProperty("order", lastPublicationsOrder);
      lastEventsSection.getElement().getStyle().setProperty("order", lastEventsOrder);
      shortCutsSection.getElement().getStyle().setProperty("order", shortcutsOrder);
      shortCutsToolsSection.getElement().getStyle().setProperty("order", shortcutstoolsOrder);
      favorisSection.getElement().getStyle().setProperty("order", favoritesOrder);
      actus.getElement().getStyle().setProperty("order", newsOrder);
    }
  }

  public void slideToRight() {
    if (news.getWidgetCount() > 0) {
      if (currentNewsIndex == news.getWidgetCount() - 1) {
        currentNewsIndex = 0;
      } else {
        currentNewsIndex++;
      }
      updateNewsView();
    }
  }

  @Override
  public void onSwipeEnd(final SwipeEndEvent event) {
    if (isVisible()) {
      HomePageNewsSlider.getInstance().stopAutoSlider();
      if (event.getDirection() == SwipeEvent.DIRECTION.RIGHT_TO_LEFT) {
        // next
        if (currentNewsIndex == news.getWidgetCount() - 1) {
          currentNewsIndex = 0;
        } else {
          currentNewsIndex++;
        }
        updateNewsView();
      } else if (event.getDirection() == SwipeEvent.DIRECTION.LEFT_TO_RIGHT) {
        // previous
        if (currentNewsIndex == 0) {
          currentNewsIndex = news.getWidgetCount() - 1;
        } else {
          currentNewsIndex--;
        }
        updateNewsView();
      }
    }
  }

  public void stop() {
    EventBus.getInstance().removeHandler(SwipeEndEvent.getType(), this);
  }

  private void updateNewsView() {
    if(data.getNewsDisplayer().equals(HomePageDTO.NEWS_DISPLAYER_CARROUSEL)) {
      for (int i = 0; i < news.getWidgetCount(); i++) {
        ((NewsItem) news.getWidget(i)).setVisible(i == currentNewsIndex);
      }
    }
  }
}
