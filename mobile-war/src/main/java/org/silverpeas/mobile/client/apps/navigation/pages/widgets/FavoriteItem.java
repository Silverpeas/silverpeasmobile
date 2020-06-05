/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.client.apps.navigation.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.common.navigation.LinksManager;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.MyLinkDTO;

public class FavoriteItem extends Composite {

  private MyLinkDTO data;
  private static FavoriteItemUiBinder uiBinder = GWT.create(FavoriteItemUiBinder.class);
  @UiField Anchor link;
  protected ApplicationMessages msg = null;


  interface FavoriteItemUiBinder extends UiBinder<Widget, FavoriteItem> {
  }

  public FavoriteItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(MyLinkDTO data) {
    this.data = data;
    link.setText(data.getName());

    if(data.getUrl().startsWith("/")) {
      // internal link
      link.setHref("javaScript:;");
    } else {
      link.setHref(data.getUrl());
      link.setTarget("_blank");
    }
    link.setStyleName("ui-btn ui-icon-carat-r");
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    LinksManager.processLink(data.getUrl());
  }

}
