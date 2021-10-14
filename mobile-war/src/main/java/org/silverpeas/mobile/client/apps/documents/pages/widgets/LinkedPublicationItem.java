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

package org.silverpeas.mobile.client.apps.documents.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import org.silverpeas.mobile.client.apps.documents.pages.PublicationPage;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;

public class LinkedPublicationItem extends Composite {

  private PublicationDTO data;
  private static LinkedPublicationItemUiBinder uiBinder = GWT.create(LinkedPublicationItemUiBinder.class);
  @UiField Anchor link;
  protected DocumentsMessages msg = null;


  interface LinkedPublicationItemUiBinder extends UiBinder<Widget, LinkedPublicationItem> {
  }

  public LinkedPublicationItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(DocumentsMessages.class);
  }

  public void setData(PublicationDTO data) {
    this.data = data;
    link.setHref("#");
    link.setText(data.getName() + " - " + data.getUpdateDate());
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    ContentDTO content = new ContentDTO();
    content.setId(data.getId());
    content.setType(ContentsTypes.Publication.toString());
    DocumentsLoadPublicationEvent loadEvent = new DocumentsLoadPublicationEvent(content);

    PublicationPage page = new PublicationPage();
    page.setPageTitle(msg.publicationTitle());
    page.show();

    EventBus.getInstance().fireEvent(loadEvent);
  }
}
