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

package org.silverpeas.mobile.client.apps.contacts.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.contacts.resources.ContactsResources;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;

public class ContactItem extends Composite {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);
  @UiField Anchor mail;
  @UiField HTMLPanel user, tel, container;

  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  private ContactsResources resourcesContact = GWT.create(ContactsResources.class);

  interface ContactItemUiBinder extends UiBinder<Widget, ContactItem> {
  }

  public ContactItem() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(DetailUserDTO userData) {
    if (userData.getAvatar().isEmpty()) {
      Image avatar = new Image(resources.avatar());
      avatar.getElement().removeAttribute("height");
      avatar.getElement().removeAttribute("width");
      user.add(avatar);
    } else {
      user.add(new Image(userData.getAvatar()));
    }
    String html = userData.getFirstName() + " " + userData.getLastName() + " <span class='status'>" + userData.getStatus() + "</span>";
    if (userData.getConnected()) html += "<span class='connected'></span>";
    user.add(new HTML(html));
    mail.setText(userData.geteMail());
    if (userData.geteMail() == null || userData.geteMail().isEmpty()) {
      mail.setHTML("&nbsp");
    }
    mail.setHref("mailto:" + userData.geteMail());

    int nbTel = 0;
    if (userData.getPhoneNumber() != null && !userData.getPhoneNumber().isEmpty()) {
      Anchor tel1 = new Anchor();
      tel1.setStyleName("tel-link");
      tel1.setText(userData.getPhoneNumber());
      tel1.setHref("tel:" + userData.getPhoneNumber());
      tel.add(tel1);
      nbTel++;
    }
    if (userData.getCellularPhoneNumber() != null && !userData.getCellularPhoneNumber().isEmpty()) {
      if (nbTel == 1) {
        tel.add(new InlineHTML(" | "));
      }
      Anchor tel2 = new Anchor();
      tel2.setStyleName("tel-link");
      tel2.setText(userData.getCellularPhoneNumber());
      tel2.setHref("tel:" + userData.getCellularPhoneNumber());
      tel.add(tel2);

      Anchor sms = new Anchor();
      sms.setHref("sms:" + userData.getCellularPhoneNumber());
      Image smsImg = new Image(resourcesContact.sms());
      sms.getElement().appendChild(smsImg.getElement());
      tel.add(sms);

      nbTel++;
    }
    if (userData.getFaxPhoneNumber() != null && !userData.getFaxPhoneNumber().isEmpty()) {
      if (nbTel == 2) {
        tel.add(new InlineHTML(" | "));
      }
      Anchor tel3 = new Anchor();
      tel3.setStyleName("tel-link");
      tel3.setText(userData.getFaxPhoneNumber());
      tel3.setHref("tel:" + userData.getFaxPhoneNumber());
      tel.add(tel3);
      nbTel++;
    }
    if (nbTel == 0) {
      tel.add(new InlineHTML("&nbsp;"));
    }

    for (String prop :userData.getProperties()) {

      String value = userData.getPropertieValue(prop);
      if (isPhoneNumber(value)) {
        HTMLPanel field = new HTMLPanel("");
        Anchor tel = new Anchor();
        tel.setStyleName("tel-link");
        tel.setText(value);
        tel.setHref("tel:" + value);
        field.add(tel);

        Anchor sms = new Anchor();
        sms.setHref("sms:" + userData.getCellularPhoneNumber());
        Image smsImg = new Image(resourcesContact.sms());
        sms.getElement().appendChild(smsImg.getElement());
        field.add(sms);

        container.add(field);
      } else {
        HTML field = new HTML(value);
        container.add(field);
      }
    }
  }

  private boolean isPhoneNumber(String value) {
    if (value == null) return false;
    value = value.replaceAll(" ", "");
    value= value.replaceAll("-", "");
    value= value.replaceFirst("\\+", "");
    if (value.length() != 10 && value.length() != 12) return false;

    try {
      Double.parseDouble(value);
    } catch(Exception e) {
      return false;
    }
    return true;
  }

  public void hideData() {
    tel.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
    mail.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
    user.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
  }

}
