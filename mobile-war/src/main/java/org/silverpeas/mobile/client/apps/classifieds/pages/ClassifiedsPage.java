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

package org.silverpeas.mobile.client.apps.classifieds.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.classifieds.events.app.ClassifiedsLoadEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.AbstractClassifiedsPagesEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.ClassifiedsLoadedEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.ClassifiedsPagesEventHandler;
import org.silverpeas.mobile.client.apps.classifieds.pages.widgets.ClassifiedItem;
import org.silverpeas.mobile.client.apps.classifieds.resources.ClassifiedsMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedsDTO;

public class ClassifiedsPage extends PageContent implements ClassifiedsPagesEventHandler {

  private static ClassifiedsPageUiBinder uiBinder = GWT.create(ClassifiedsPageUiBinder.class);

  @UiField(provided = true) protected ClassifiedsMessages msg = null;
  @UiField
  UnorderedList classifieds;
  @UiField
  ListBox categories, types;


  private ClassifiedsDTO data;
  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  interface ClassifiedsPageUiBinder extends UiBinder<Widget, ClassifiedsPage> {
  }

  public ClassifiedsPage() {
    msg = GWT.create(ClassifiedsMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractClassifiedsPagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new ClassifiedsLoadEvent());
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractClassifiedsPagesEvent.TYPE, this);
  }

  @Override
  public void onClassifiedsLoad(final ClassifiedsLoadedEvent event) {
    data = event.getClassifieds();
    types.clear();
    types.addItem("","");
    for (String type : data.getTypes().keySet()) {
      types.addItem(data.getTypes().get(type), type);
    }
    categories.clear();
    categories.addItem("","");
    for (String cat : data.getCategories().keySet()) {
      categories.addItem(data.getCategories().get(cat), cat);
    }
    displayList();

    addActionMenu(favorite);
    favorite.init(getApp().getApplicationInstance().getId(), getApp().getApplicationInstance().getId(), ContentsTypes.Component.name(), getPageTitle());
  }

  private void displayList() {
    classifieds.clear();
    String cat = categories.getSelectedValue();
    String type = types.getSelectedValue();
    for (ClassifiedDTO classified : data.getClassifieds()) {
      if ((classified.getCategory().equals(cat) || cat.isEmpty()) && (classified.getType().equals(type) || type.isEmpty())) {
        ClassifiedItem item = new ClassifiedItem();
        item.setCategory(data.getCategories().get(classified.getCategory()));
        item.setType(data.getTypes().get(classified.getType()));
        item.setApplication(getApp());
        item.setData(classified);
        item.hasComments(data.getHasComments());
        classifieds.add(item);
      }
    }
  }

  @UiHandler("categories")
  protected void onChangedCategory(ChangeEvent event) {
    displayList();
  }

  @UiHandler("types")
  protected void onChangedType(ChangeEvent event) {
    displayList();
  }
}