/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.client.apps.classifieds.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.AbstractClassifiedsPagesEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.ClassifiedsLoadedEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.ClassifiedsPagesEventHandler;
import org.silverpeas.mobile.client.apps.classifieds.resources.ClassifiedsMessages;
import org.silverpeas.mobile.client.apps.comments.pages.widgets.CommentsButton;
import org.silverpeas.mobile.client.apps.notifications.pages.widgets.NotifyButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

public class ClassifiedPage extends PageContent implements ClassifiedsPagesEventHandler {

  private static ClassifiedsPageUiBinder uiBinder = GWT.create(ClassifiedsPageUiBinder.class);

  @UiField(provided = true) protected ClassifiedsMessages msg = null;

  @UiField
  ActionsMenu actionsMenu;

  @UiField
  HTML title, description, pictures, filters, price, audit;

  @UiField
  CommentsButton comments;

  private boolean hasComments;
  private ClassifiedDTO data;
  private String category, type;
  private NotifyButton notification = new NotifyButton();

  interface ClassifiedsPageUiBinder extends UiBinder<Widget, ClassifiedPage> {
  }

  public ClassifiedPage() {
    msg = GWT.create(ClassifiedsMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractClassifiedsPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractClassifiedsPagesEvent.TYPE, this);
    comments.stop();
  }

  public void setComments(boolean hasComments) {
    this.hasComments = hasComments;
  }

  public void setData(ClassifiedDTO data) {
    this.data = data;
    title.setHTML(data.getTitle());
    description.setHTML(data.getDescription());
    price.setHTML(data.getPrice() + " €");
    filters.setHTML("<span>" + category + "</span><span>" + type + "</span>");
    String date = data.getCreationDate();
    if (data.getUpdateDate() != null) date = data.getUpdateDate();
    audit.setHTML("<span>" + data.getCreatorName() + "</span><span>" + date + "</span>");
    String html = "";
    for (String pic : data.getPictures()) {
      html += "<img src='"+pic+"'/>";
    }

    pictures.setHTML(html);

    notification.init(getApp().getApplicationInstance().getId(), data.getId(), NotificationDTO.TYPE_EVENT, data.getTitle(), getPageTitle());
    actionsMenu.addAction(notification);

    //TODO : add  comments
    if (hasComments) {
      String contentType = "Classified";
      comments.init(data.getId(), getApp().getApplicationInstance().getId(), contentType,
          getPageTitle(), data.getTitle(), data.getCommentsNumber());
      comments.getElement().getStyle().clearDisplay();
    }
  }

  public void setCategory(final String category) {
    this.category = category;
  }

  public void setType(final String type) {
    this.type = type;
  }

  @Override
  public void onClassifiedsLoad(final ClassifiedsLoadedEvent event) {
  }

}