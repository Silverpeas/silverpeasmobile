/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.faq.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.faq.events.app.FaqCategoriesLoadEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.AbstractFaqPagesEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqAttachmentsLoadedEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqCategoriesLoadedEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqPagesEventHandler;
import org.silverpeas.mobile.client.apps.faq.pages.widgets.FaqItem;
import org.silverpeas.mobile.client.apps.faq.resources.FaqMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.faq.CategoryDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDTO;

import java.util.List;

public class FaqPage extends PageContent implements FaqPagesEventHandler {

  private static FaqPageUiBinder uiBinder = GWT.create(FaqPageUiBinder.class);

  @UiField(provided = true) protected FaqMessages msg = null;
  @UiField
  ActionsMenu actionsMenu;

  @UiField
  HTMLPanel container;

  @UiField
  UnorderedList faqs;

  @UiField
  ListBox categories;

  private List<QuestionDTO> data;
  private List<CategoryDTO> cats;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();


  interface FaqPageUiBinder extends UiBinder<Widget, FaqPage> {
  }

  public FaqPage() {
    msg = GWT.create(FaqMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractFaqPagesEvent.TYPE, this);

  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractFaqPagesEvent.TYPE, this);
  }

  @Override
  public void setApp(final App app) {
    super.setApp(app);
    actionsMenu.addAction(favorite);
    favorite.init(getApp().getApplicationInstance().getId(), getApp().getApplicationInstance().getId(), ContentsTypes.Component.name(), getPageTitle());
  }

  public void setData(List<QuestionDTO> data) {
    this.data = data;
    EventBus.getInstance().fireEvent(new FaqCategoriesLoadEvent());
  }

  @Override
  public void onCategoriesLoaded(final FaqCategoriesLoadedEvent event) {
    this.cats = event.getCategories();
    categories.clear();
    categories.addItem("","");
    for (CategoryDTO cat : cats) {
      categories.addItem(cat.getTitle(), cat.getId());
    }
    displayFaqs(data);
  }

  @Override
  public void onAttachmentsLoaded(final FaqAttachmentsLoadedEvent faqAttachmentsLoadedEvent) {
  }

  @UiHandler("categories")
  protected void onChanged(ChangeEvent event) {
    displayFaqs(data);
  }

  private void displayFaqs(final List<QuestionDTO> data) {
    String id = categories.getSelectedValue();
    faqs.clear();
    for (QuestionDTO question : data) {
      if (question.getCategory().getId().equals(id) || id.isEmpty()) {
        FaqItem item = new FaqItem();
        item.setData(question);
        faqs.add(item);
      }
    }
  }
}