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

package org.silverpeas.mobile.client.apps.favorites.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.navigation.pages.widgets.FavoriteItem;
import org.silverpeas.mobile.client.components.base.widgets.SelectableItem;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.MyLinkCategoryDTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryItem extends SelectableItem {

  private MyLinkCategoryDTO data;
  private boolean expanded = true;

  private List<FavoriteItem> favorites = new ArrayList<>();
  private static CategoryItemUiBinder uiBinder = GWT.create(CategoryItemUiBinder.class);

  @UiField
  HTMLPanel container;
  @UiField Anchor link;
  protected ApplicationMessages msg = null;

  private ApplicationResources resources = GWT.create(ApplicationResources.class);

  @UiField
  SpanElement title, desc, icon;


  interface CategoryItemUiBinder extends UiBinder<Widget, CategoryItem> {
  }

  public CategoryItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
    setContainer(container);
    icon.setInnerHTML(resources.less().getText());

  }
  public void setData(MyLinkCategoryDTO data) {
    this.data = data;
    title.setInnerHTML(data.getName());
    if (!data.getName().equals(data.getDescription())) {
      desc.setInnerHTML(data.getDescription());
    }
  }

  public void addFavorite(FavoriteItem item) {
    favorites.add(item);
  }


  public MyLinkCategoryDTO getData() {
    return data;
  }

    @UiHandler("link")
    protected void onClick(ClickEvent event) {
      expanded = !expanded;
      if (expanded) {
        icon.setInnerHTML(resources.less().getText());
      } else {
        icon.setInnerHTML(resources.more().getText());
      }
      for (FavoriteItem item : favorites) {
        if (expanded) {
          item.getElement().addClassName("item-open");
          item.getElement().removeClassName("item-closed");

        } else {
          item.getElement().addClassName("item-closed");
          item.getElement().removeClassName("item-open");
        }
      }
    }
}
