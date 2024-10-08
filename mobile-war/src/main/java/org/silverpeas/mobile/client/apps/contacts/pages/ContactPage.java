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

package org.silverpeas.mobile.client.apps.contacts.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.contacts.pages.widgets.FieldItem;
import org.silverpeas.mobile.client.apps.contacts.resources.ContactsMessages;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.PropertyDTO;

public class ContactPage extends PageContent {

  private static ContactPageUiBinder uiBinder = GWT.create(ContactPageUiBinder.class);

  @UiField(provided = true) protected ContactsMessages msg = null;
  @UiField HTMLPanel container;
  @UiField UnorderedList fields;

  private DetailUserDTO data;

  public void setData(DetailUserDTO data) {
    this.data = data;
    fields.clear();

    Image pic = new Image();
    pic.setUrl(data.getAvatar());
    pic.setStyleName("avatar");
    FieldItem avatar = new FieldItem();
    avatar.setContent(pic);
    fields.add(avatar);
    FieldItem mail = new FieldItem();
    mail.setData("email", "Email", data.geteMail());
    fields.add(mail);
    for (PropertyDTO propertie : data.getProperties()) {
      FieldItem f = new FieldItem();
      f.setData(propertie.getKey(), data.getPropertiesLabel().get(propertie.getKey()), propertie.getValue());
      fields.add(f);
    }
  }

  interface ContactPageUiBinder extends UiBinder<Widget, ContactPage> {
  }

  public ContactPage() {
    msg = GWT.create(ContactsMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));


  }
}