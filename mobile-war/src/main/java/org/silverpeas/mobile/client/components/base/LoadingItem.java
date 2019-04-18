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

package org.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.resources.ApplicationMessages;


/**
 * @author: svu
 */
public class LoadingItem extends Composite {
  public void hide() {
    getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
  }

  interface LoadingItemUiBinder extends UiBinder<Widget, LoadingItem> {
    }

    @UiField(provided = true) protected ApplicationMessages msg = null;
    @UiField protected InlineHTML content;

    private static LoadingItemUiBinder uiBinder = GWT.create(LoadingItemUiBinder.class);

    public LoadingItem() {
        msg = GWT.create(ApplicationMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
        getElement().getStyle().setWidth(Window.getClientWidth(), Style.Unit.PX);
        getElement().getStyle().setDisplay(Style.Display.FLEX);
        getElement().getStyle().setLineHeight(3, Style.Unit.EM);
        content.getElement().getStyle().setWidth(Window.getClientWidth(), Style.Unit.PX);
        content.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
    }
}