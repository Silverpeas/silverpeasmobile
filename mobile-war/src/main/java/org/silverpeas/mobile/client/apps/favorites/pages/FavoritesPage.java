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

package org.silverpeas.mobile.client.apps.favorites.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.favorites.events.app.FavoritesDeleteEvent;
import org.silverpeas.mobile.client.apps.favorites.events.app.FavoritesLoadEvent;
import org.silverpeas.mobile.client.apps.favorites.events.pages.AbstractFavoritesPagesEvent;
import org.silverpeas.mobile.client.apps.favorites.events.pages.FavoritesLoadedEvent;
import org.silverpeas.mobile.client.apps.favorites.events.pages.FavoritesPagesEventHandler;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.CategoryItem;
import org.silverpeas.mobile.client.apps.favorites.resources.FavoritesMessages;
import org.silverpeas.mobile.client.apps.navigation.pages.widgets.FavoriteItem;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.PopinConfirmation;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.widgets.DeleteButton;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.MyLinkCategoryDTO;
import org.silverpeas.mobile.shared.dto.MyLinkDTO;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPage extends PageContent implements FavoritesPagesEventHandler {

  private static FavoritesPageUiBinder uiBinder = GWT.create(FavoritesPageUiBinder.class);

  @UiField(provided = true) protected ApplicationMessages msg = null;
  @UiField
  UnorderedList favorites;

  private DeleteButton buttonDelete = new DeleteButton();

  private FavoritesMessages msgApp = GWT.create(FavoritesMessages .class);

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
  public void onFavoritesLoaded(final FavoritesLoadedEvent event) {
    favorites.clear();
    List favoritesList = event.getFavorites();
    CategoryItem currentCategory = null;
    for (Object it : favoritesList) {
      if (it instanceof MyLinkDTO) {
        FavoriteItem item = new FavoriteItem();
        item.setParent(this);
        item.setData((MyLinkDTO) it);
        currentCategory.addFavorite(item);
        item.getElement().addClassName("item-open");
        favorites.add(item);
      } else if (it instanceof MyLinkCategoryDTO) {
        CategoryItem item = new CategoryItem();
        item.setData((MyLinkCategoryDTO) it);
        favorites.add(item);
        currentCategory = item;
      }
    }
  }

  @Override
  public void setSelectionMode(boolean selectionMode) {
    super.setSelectionMode(selectionMode);
    if (selectionMode) {
      clearActions();
      buttonDelete.setCallback(new Command() {@Override public void execute() {deleteSelectedFavoris();}});
      buttonDelete.setId("delete-favoris");
      addActionShortcut(buttonDelete);
    } else {
      clearActions();
    }
  }

  private void deleteSelectedFavoris() {
    PopinConfirmation popin = new PopinConfirmation(msgApp.deleteConfirmation());
    popin.setYesCallback(new Command() {
      @Override
      public void execute() {
        List<MyLinkDTO> selection = getSelectedFavorites();
        FavoritesDeleteEvent deleteEvent = new FavoritesDeleteEvent();
        deleteEvent.setSelection(selection);
        if (!selection.isEmpty()) EventBus.getInstance().fireEvent(deleteEvent);
        clearActions();
      }
    });
    popin.show();
  }

  private List<MyLinkDTO> getSelectedFavorites() {
    List<MyLinkDTO> selection = new ArrayList<>();
    for (int i = 0; i < favorites.getCount(); i++) {
      FavoriteItem item = (FavoriteItem) favorites.getWidget(i);
      if (item.isSelected()) {
        selection.add(item.getData());
      }
    }
    return selection;
  }
}