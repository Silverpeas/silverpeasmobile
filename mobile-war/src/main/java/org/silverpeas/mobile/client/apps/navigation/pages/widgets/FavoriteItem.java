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

package org.silverpeas.mobile.client.apps.navigation.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationReadenEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.navigation.LinksManager;
import org.silverpeas.mobile.client.components.base.widgets.SelectableItem;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.FavoriteDTO;
import org.silverpeas.mobile.shared.dto.MyLinkDTO;
import org.silverpeas.mobile.shared.dto.hyperlink.HyperLinkDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationSendedDTO;

public class FavoriteItem extends SelectableItem {

  private MyLinkDTO data;
  private static FavoriteItemUiBinder uiBinder = GWT.create(FavoriteItemUiBinder.class);

  @UiField
  HTMLPanel container;
  @UiField Anchor link;
  protected ApplicationMessages msg = null;

  private ApplicationResources resources = GWT.create(ApplicationResources.class);

  @UiField
  SpanElement icon, title, desc;

  private boolean minimalView = true;

  interface FavoriteItemUiBinder extends UiBinder<Widget, FavoriteItem> {
  }

  public FavoriteItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
    icon.setInnerHTML(resources.favorite().getText());
    setContainer(container);
  }

  public void setMinimalView(boolean minimalView) {
    this.minimalView = minimalView;
    if (minimalView) {
      desc.getStyle().setDisplay(Style.Display.NONE);
    } else {
      desc.getStyle().setDisplay(Style.Display.BLOCK);
    }
  }

  public void setData(MyLinkDTO data) {
    this.data = data;
    title.setInnerHTML(data.getName());
    if (!data.getName().equals(data.getDescription())) {
      desc.setInnerHTML(data.getDescription());
    }

    if(data.getUrl().startsWith("/")) {
      // internal link
      link.setHref("javaScript:;");
    } else {
      link.setHref(data.getUrl());
      link.setTarget("_blank");
    }
  }

  public MyLinkDTO getData() {
    return data;
  }

  @UiHandler("link")
  protected void startTouch(TouchStartEvent event) {
    startTouch(event, minimalView);
  }

  @UiHandler("link")
  protected void moveTouch(TouchMoveEvent event) {
    super.moveTouch(event);
  }

  @UiHandler("link")
  protected void endTouch(TouchEndEvent event) {
    endTouch(event, minimalView, new Command() {
      @Override
      public void execute() {
        HyperLinkDTO link = new HyperLinkDTO();
        link.setUrl(data.getUrl());
        LinksManager.processLink(link);
      }
    });
  }

}
