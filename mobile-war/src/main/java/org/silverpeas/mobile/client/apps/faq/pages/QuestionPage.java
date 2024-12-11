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

package org.silverpeas.mobile.client.apps.faq.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.faq.events.app.FaqCategoriesLoadEvent;
import org.silverpeas.mobile.client.apps.faq.events.app.QuestionCreateEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.*;
import org.silverpeas.mobile.client.apps.faq.resources.FaqMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.faq.QuestionDetailDTO;

public class QuestionPage extends PageContent implements FaqPagesEventHandler {

  private static QuestionPageUiBinder uiBinder = GWT.create(QuestionPageUiBinder.class);

  @UiField(provided = true) protected FaqMessages msg = null;

  @UiField
  HTMLPanel container;

  @UiField
  ListBox categoryList;

  @UiField
  TextBox question;

  @UiField
  TextArea description;

  @UiField Anchor submit;

  interface QuestionPageUiBinder extends UiBinder<Widget, QuestionPage> {
  }

  public QuestionPage() {
    msg = GWT.create(FaqMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractFaqPagesEvent.TYPE, this);
    container.getElement().setId("questionForm");
    question.getElement().setAttribute("placeholder", msg.questionField());
    description.getElement().setAttribute("placeholder", msg.descriptionField());
    description.getElement().setAttribute("rows", "6");
    categoryList.addItem(msg.categoryField(), "");
    submit.getElement().addClassName("formIncomplete");

    EventBus.getInstance().fireEvent(new FaqCategoriesLoadEvent());
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractFaqPagesEvent.TYPE, this);
  }

  @Override
  public void setApp(final App app) {
    super.setApp(app);
  }

  @Override
  public void onCategoriesLoaded(FaqCategoriesLoadedEvent event) {
    event.getCategories().forEach(category -> {categoryList.addItem(category.getTitle(), category.getId());});
  }

  @Override
  public void onAttachmentsLoaded(final FaqAttachmentsLoadedEvent faqAttachmentsLoadedEvent) {
  }

  @UiHandler("question")
  protected void changeTitle(ChangeEvent event) {
    validateForm();
  }

  private boolean validateForm() {
    boolean valid = !question.getText().isEmpty();
    if (valid) {
      submit.getElement().removeClassName("formIncomplete");
    } else {
      submit.getElement().addClassName("formIncomplete");
    }
    return valid;
  }

  private QuestionDetailDTO populate() {
    QuestionDetailDTO dto = new QuestionDetailDTO();
    dto.setQuestion(question.getText());
    dto.setDescription(description.getText());
    dto.setCategoryId(categoryList.getSelectedValue());
    return dto;
  }

  @UiHandler("submit")
  protected void save(ClickEvent event) {
    if (!submit.getElement().hasClassName("formIncomplete")) {
      QuestionDetailDTO dto = populate();
      QuestionCreateEvent createEvent = new QuestionCreateEvent(dto);
      EventBus.getInstance().fireEvent(createEvent);
      back();
    }
  }
}