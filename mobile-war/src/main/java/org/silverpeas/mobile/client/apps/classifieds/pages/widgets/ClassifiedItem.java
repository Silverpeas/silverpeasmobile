/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.classifieds.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.classifieds.pages.ClassifiedPage;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;

public class ClassifiedItem extends Composite implements ClickHandler {

  private ClassifiedDTO data;
  private String category, type;
  private static ClassifiedItemUiBinder uiBinder = GWT.create(ClassifiedItemUiBinder.class);
  private App application;
  private boolean hasComments;

  @UiField
  HTML content;
  @UiField
  HTMLPanel container;
  protected ApplicationMessages msg = null;


  interface ClassifiedItemUiBinder extends UiBinder<Widget, ClassifiedItem> {}

  public ClassifiedItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void hasComments(final boolean hasComments) {
    this.hasComments = hasComments;
  }

  public void setData(ClassifiedDTO data) {
    this.data = data;
    String pic = "";
    if (!data.getPictures().isEmpty()) pic = data.getPictures().get(0);
    String date = data.getCreationDate();
    if (data.getUpdateDate() != null) {
      date = data.getUpdateDate();
    }

    String html = "<div class='thumbnail'><img src='" + pic + "' width='200px'/></div>";
    html += "<h2>" + data.getTitle() + "</h2>";
    html += "<div class='classified_type'><span>" + category + "</span><span>" + type + "</span></div>";
    if (!data.getPrice().equalsIgnoreCase("0")) html += "<div class='classified_price'>" + data.getPrice() + " €" + "</div>";
    html += "<div class='classified_creationInfo'>" + date + "</div>";
    content.setHTML(html);
    content.addClickHandler(this);
  }

  public void setCategory(final String category) {
    this.category = category;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public void setApplication(final App application) {
    this.application = application;
  }

  @Override
  public void onClick(final ClickEvent event) {
    ClassifiedPage page = new ClassifiedPage();
    page.setComments(hasComments);
    page.setCategory(category);
    page.setType(type);
    page.setApp(application);
    page.setData(data);
    page.show();
  }
}
