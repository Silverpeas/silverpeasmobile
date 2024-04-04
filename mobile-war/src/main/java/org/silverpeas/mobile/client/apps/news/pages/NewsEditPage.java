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

package org.silverpeas.mobile.client.apps.news.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.news.events.app.NewsCreateEvent;
import org.silverpeas.mobile.client.apps.news.events.pages.*;
import org.silverpeas.mobile.client.apps.news.resources.NewsMessages;
import org.silverpeas.mobile.client.common.Ckeditor;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.components.PopinConfirmation;
import org.silverpeas.mobile.client.components.PopinInformation;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

public class NewsEditPage extends PageContent implements NewsPagesEventHandler {

  private static NewsEditPageUiBinder uiBinder = GWT.create(NewsEditPageUiBinder.class);

  @UiField(provided = true) protected NewsMessages msg = null;

  @UiField
  HTMLPanel container;
  @UiField TextBox title;

  @UiField TextArea description;

  @UiField CheckBox important;

  @UiField DivElement newsContent, thumbnailContainer;

  @UiField
  FileUpload thumbnail;

  @UiField Anchor submit;

  ImageElement preview;

  interface NewsEditPageUiBinder extends UiBinder<Widget, NewsEditPage> {
  }

  public NewsEditPage() {
    msg = GWT.create(NewsMessages.class);
    setPageTitle(msg.newTitle());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractNewsPagesEvent.TYPE, this);
    container.getElement().setId("newsForm");
    title.getElement().setAttribute("placeholder", msg.titleField());
    description.getElement().setAttribute("placeholder", msg.descriptionField());
    description.getElement().setAttribute("rows", "6");
    submit.getElement().addClassName("formIncomplete");
    Ckeditor.createEditor(newsContent);
  }

  @Override
  public void setApp(App app) {
    super.setApp(app);
    //TODO : mandatory case
    String mandatory = app.getApplicationInstance().getParamters().get("thumbnailMandatory");
    if (Boolean.parseBoolean(mandatory)) {
      thumbnailContainer.addClassName("mandatory");
    }
  }

  @UiHandler("submit")
  protected void save(ClickEvent event) {
    NewsDTO dto = new NewsDTO();
    dto.setTitle(title.getText());
    dto.setDescription(description.getText());
    dto.setImportant(important.getValue());
    dto.setContent(Ckeditor.getCurrentData());
    dto.setVignette(preview.getSrc());
    NewsCreateEvent createEvent = new NewsCreateEvent(dto);
    EventBus.getInstance().fireEvent(createEvent);
  }
  @Override
  public void onNewsSaved(NewsSavedEvent event) {
    if (event.isInError()) {
      PopinInformation popin = new PopinInformation(msg.notSaved());
      popin.show();
    } else {
      Ckeditor.destroyEditor();
      back();
    }
  }

  @UiHandler("title")
  protected void changeTitle(ChangeEvent event) {
    validateForm();
  }

  private boolean validateForm() {
    boolean valid = !title.getText().isEmpty();
    if (valid) {
      submit.getElement().removeClassName("formIncomplete");
    } else {
      submit.getElement().addClassName("formIncomplete");
    }
    return valid;
  }

  @UiHandler("thumbnail")
  void upload(ChangeEvent event) {
    thumbnailContainer.setInnerHTML("");
    thumbnailContainer.removeClassName("thumbnail");
    preview = Document.get().createImageElement();
    preview.setId("preview");
    thumbnailContainer.appendChild(preview);
    previewFile(thumbnail.getElement(), preview);
  }

  public static native void previewFile(Element thumbnail, ImageElement v) /*-{
    var file = thumbnail.files[0];
    var reader = new FileReader();
    if (file) {
      reader.readAsDataURL(file);
    }
    reader.addEventListener("load", function () {
      v.src = reader.result;
    });
  }-*/;

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractNewsPagesEvent.TYPE, this);
  }

  @Override
  public void onNewsLoad(NewsLoadedEvent event) {}
}