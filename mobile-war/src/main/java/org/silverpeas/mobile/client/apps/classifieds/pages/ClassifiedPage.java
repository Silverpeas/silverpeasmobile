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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.AbstractClassifiedsPagesEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.ClassifiedsLoadedEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.ClassifiedsPagesEventHandler;
import org.silverpeas.mobile.client.apps.classifieds.pages.widgets.PictureItem;
import org.silverpeas.mobile.client.apps.classifieds.resources.ClassifiedsMessages;
import org.silverpeas.mobile.client.apps.comments.pages.widgets.CommentsButton;
import org.silverpeas.mobile.client.apps.notifications.pages.widgets.NotifyButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

public class ClassifiedPage extends PageContent implements ClassifiedsPagesEventHandler,
    SwipeEndHandler {

  private static ClassifiedsPageUiBinder uiBinder = GWT.create(ClassifiedsPageUiBinder.class);

  private SwipeRecognizer swipeRecognizer;
  private int currentPictureIndex = 0;

  @UiField(provided = true) protected ClassifiedsMessages msg = null;

  @UiField
  ActionsMenu actionsMenu;

  @UiField
  HTML price, time, author;

  @UiField
  HTMLPanel title, description;

  @UiField
  CommentsButton comments;

  @UiField
  UnorderedList pictures;

  @UiField
  FocusPanel carroussel;

  @UiField
  Anchor contact;

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
    contact.setHTML("<span class='ui-btn-text'>" + msg.contact() + "</span>");
    EventBus.getInstance().addHandler(AbstractClassifiedsPagesEvent.TYPE, this);
    EventBus.getInstance().addHandler(SwipeEndEvent.getType(), this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractClassifiedsPagesEvent.TYPE, this);
    EventBus.getInstance().removeHandler(SwipeEndEvent.getType(), this);
    comments.stop();
  }

  public void setComments(boolean hasComments) {
    this.hasComments = hasComments;
  }

  public void setData(ClassifiedDTO data) {
    this.data = data;
    title.getElement().setInnerText(data.getTitle());
    description.getElement().setInnerText(data.getDescription());
    price.setHTML(data.getPrice() + " €");

    String date = data.getCreationDate();
    if (data.getUpdateDate() != null) date = msg.online() + " " + data.getUpdateDate();
    author.getElement().setInnerText(msg.by() + " " + data.getCreatorName());
    time.getElement().setInnerText(date);

    boolean v = true;
    for (String pic : data.getPictures()) {
      PictureItem item = new PictureItem();
      item.setData(pic);
      item.setVisible(v);
      pictures.add(item);
      v = false;
    }

    notification.init(getApp().getApplicationInstance().getId(), data.getId(), NotificationDTO.TYPE_EVENT, data.getTitle(), getPageTitle());
    actionsMenu.addAction(notification);

    //TODO : add  comments
    if (hasComments) {
      String contentType = "Classified";
      comments.init(data.getId(), getApp().getApplicationInstance().getId(), contentType,
          getPageTitle(), data.getTitle(), data.getCommentsNumber());
      comments.getElement().getStyle().clearDisplay();
    }

    if (MobilUtils.isMobil()) {
      swipeRecognizer = new SwipeRecognizer(carroussel);
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

  @Override
  public void onSwipeEnd(final SwipeEndEvent event) {
    if (isVisible()) {
      if (event.getDirection() == SwipeEvent.DIRECTION.RIGHT_TO_LEFT) {
        // next
        if (currentPictureIndex == pictures.getWidgetCount() - 1) {
          currentPictureIndex = 0;
        } else {
          currentPictureIndex++;
        }
        updatePicturesView();
      } else if (event.getDirection() == SwipeEvent.DIRECTION.LEFT_TO_RIGHT) {
        // previous
        if (currentPictureIndex == 0) {
          currentPictureIndex = pictures.getWidgetCount() - 1;
        } else {
          currentPictureIndex--;
        }
        updatePicturesView();
      }
    }
  }

  private void updatePicturesView() {
    for (int i = 0; i < pictures.getWidgetCount(); i++) {
      ((PictureItem) pictures.getWidget(i)).setVisible(i == currentPictureIndex);
    }
  }

  @UiHandler("contact")
  protected void contact(ClickEvent event) {
    MessagePage page = new MessagePage();
    page.setData(data);
    page.show();
  }
}