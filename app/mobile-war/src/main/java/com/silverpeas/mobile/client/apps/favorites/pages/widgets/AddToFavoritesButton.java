/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.client.apps.favorites.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.apps.favorites.events.app.AddFavoriteEvent;
import com.silverpeas.mobile.client.apps.favorites.resources.FavoritesMessages;
import com.silverpeas.mobile.client.common.EventBus;

/**
 * @author: svu
 */
public class AddToFavoritesButton extends Composite {
    interface AddToFavoritesButtonUiBinder extends UiBinder<HTMLPanel, AddToFavoritesButton> {
    }

    private static AddToFavoritesButtonUiBinder uiBinder = GWT.create(AddToFavoritesButtonUiBinder.class);

    @UiField  HTMLPanel container;
    @UiField  Anchor addToFavorites;

    @UiField(provided = true) protected FavoritesMessages msg = null;
    private String instanceId, contentId, contentType, title;


    public AddToFavoritesButton() {
        msg = GWT.create(FavoritesMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void init(String instanceId, String contentId, String contentType, String title) {
        this.instanceId = instanceId;
        this.contentId = contentId;
        this.contentType = contentType;
        this.title = title;
    }

    @UiHandler("addToFavorites")
    void displayNotificationPage(ClickEvent event){
        AddFavoriteEvent addEvent = new AddFavoriteEvent();
        addEvent.setDescription(title);
        addEvent.setInstanceId(instanceId);
        addEvent.setObjectId(contentId);
        addEvent.setObjectType(contentType);
        EventBus.getInstance().fireEvent(addEvent);

        // hide menu
        getElement().getParentElement().removeAttribute("style");
    }

}