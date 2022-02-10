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

package org.silverpeas.mobile.client.apps.blog.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.blog.events.app.BlogLoadEvent;
import org.silverpeas.mobile.client.apps.blog.events.pages.AbstractBlogPagesEvent;
import org.silverpeas.mobile.client.apps.blog.events.pages.BlogLoadedEvent;
import org.silverpeas.mobile.client.apps.blog.events.pages.BlogPagesEventHandler;
import org.silverpeas.mobile.client.apps.blog.pages.widgets.BlogItem;
import org.silverpeas.mobile.client.apps.blog.resources.BlogMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.blog.PostDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlogPage extends PageContent implements BlogPagesEventHandler {

  private static BlogPageUiBinder uiBinder = GWT.create(BlogPageUiBinder.class);

  @UiField(provided = true) protected BlogMessages msg = null;
  @UiField
  UnorderedList news;
  @UiField
  ActionsMenu actionsMenu;
  @UiField
  ListBox categories;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private String instanceId;

  interface BlogPageUiBinder extends UiBinder<Widget, BlogPage> {
  }

  public BlogPage() {
    msg = GWT.create(BlogMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractBlogPagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new BlogLoadEvent("all"));
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractBlogPagesEvent.TYPE, this);
  }

  @Override
  public void onBlogLoad(final BlogLoadedEvent event) {
    news.clear();
    String selectedCat = categories.getSelectedItemText();
    categories.clear();
    List<PostDTO> postsDTOList = event.getPosts();
    HashMap<String, String> cats = new HashMap<>();
    int i = 1;
    int max = postsDTOList.size();
    for (PostDTO postDTO : postsDTOList) {
      BlogItem item = new BlogItem();
      item.setData(i, max, postDTO);
      news.add(item);
      instanceId = postDTO.getInstanceId();
      cats.put(postDTO.getCategoryId(), postDTO.getCategoryName());
      i++;
    }

    // sort categories
    Set<Map.Entry<String, String>> set = cats.entrySet();
    List<Map.Entry<String, String>> list = new ArrayList<>(set);
    Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
      public int compare(Map.Entry<String, String> o1,
          Map.Entry<String, String> o2) {
        return o2.getKey().compareTo(o1.getKey());
      }
    });

    categories.addItem(msg.allCategories(), "all");
    int indexCat = 0;
    int oldSelection = 0;
    for (HashMap.Entry cat : list) {
      String v = (String) cat.getValue();
      if (!v.isEmpty()) {
        categories.addItem(v, (String) cat.getKey());
        indexCat++;
        if (v.equals(selectedCat)) {
          oldSelection = indexCat;
        }
      }
    }
    categories.setSelectedIndex(oldSelection);

    actionsMenu.addAction(favorite);
    favorite.init(instanceId, instanceId, ContentsTypes.Component.name(), getPageTitle());
  }

  @UiHandler("categories")
  protected void onChanged(ChangeEvent event) {
    EventBus.getInstance().fireEvent(new BlogLoadEvent(((ListBox)event.getSource()).getSelectedValue()));
  }
}