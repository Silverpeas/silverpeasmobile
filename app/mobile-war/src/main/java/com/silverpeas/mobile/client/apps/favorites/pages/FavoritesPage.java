/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.client.apps.favorites.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.favorites.events.app.FavoritesLoadEvent;
import com.silverpeas.mobile.client.apps.favorites.events.pages.AbstractFavoritesPagesEvent;
import com.silverpeas.mobile.client.apps.favorites.events.pages.FavoritesLoadedEvent;
import com.silverpeas.mobile.client.apps.favorites.events.pages.FavoritesPagesEventHandler;
import com.silverpeas.mobile.client.apps.navigation.pages.widgets.FavoriteItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.FavoriteDTO;

import java.util.List;

public class FavoritesPage extends PageContent implements FavoritesPagesEventHandler {

  private static FavoritesPageUiBinder uiBinder = GWT.create(FavoritesPageUiBinder.class);

  @UiField(provided = true) protected ApplicationMessages msg = null;
  @UiField
  UnorderedList favorites;

  interface FavoritesPageUiBinder extends UiBinder<Widget, FavoritesPage> {
  }

  public FavoritesPage() {
    msg = GWT.create(ApplicationMessages.class);
    setPageTitle(msg.favorites().asString());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractFavoritesPagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new FavoritesLoadEvent());
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractFavoritesPagesEvent.TYPE, this);
  }

  @Override
  public void onContactsLoaded(final FavoritesLoadedEvent event) {

    List<FavoriteDTO> favoritesList = event.getFavorites();
    for (FavoriteDTO favoriteDTO : favoritesList) {
      FavoriteItem item = new FavoriteItem();
      item.setData(favoriteDTO);
      favorites.add(item);
    }
  }
}