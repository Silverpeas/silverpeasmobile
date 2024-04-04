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

package org.silverpeas.mobile.client.apps.documents.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import org.silverpeas.mobile.client.components.base.ActionItem;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.resources.ApplicationMessages;


/**
 * @author: svu
 */
public class EditButton extends ActionItem {
    interface EditButtonUiBinder extends UiBinder<Widget, EditButton> {
    }

    @UiField Anchor link;
    @UiField(provided = true) protected DocumentsMessages msg = null;

    private ApplicationMessages globalMsg = GWT.create(ApplicationMessages.class);

    private static EditButtonUiBinder uiBinder = GWT.create(EditButtonUiBinder.class);

    public EditButton() {
        msg = GWT.create(DocumentsMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("link")
    void upload(ClickEvent event) {
        getCallback().execute();
        ActionsMenu.close(getElement());
    }
}