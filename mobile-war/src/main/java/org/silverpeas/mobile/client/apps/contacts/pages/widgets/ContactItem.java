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

package org.silverpeas.mobile.client.apps.contacts.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.contacts.events.app.ContactLoadEvent;
import org.silverpeas.mobile.client.apps.contacts.resources.ContactsResources;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.PropertyDTO;

public class ContactItem extends Composite implements ClickHandler {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);
  @UiField Anchor mail;
  @UiField HTMLPanel user, tel, container;

  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  private ContactsResources resourcesContact = GWT.create(ContactsResources.class);

  @Override
  public void onClick(ClickEvent event) {
    Widget w = (Widget) event.getSource();
    EventBus.getInstance().fireEvent(new ContactLoadEvent(w.getElement().getAttribute("user-id")));
  }

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
      avatar.getElement().setAttribute("user-id", userData.getId());
      avatar.addClickHandler(this);
      user.add(avatar);
    } else {
      Image avatar = new Image(userData.getAvatar());
      avatar.getElement().setAttribute("user-id", userData.getId());
      avatar.addClickHandler(this);
      user.add(avatar);
    }

    Boolean firstnameAtFirst = Boolean.parseBoolean(ResourcesManager.getParam("directory.display.firstname.at.first"));
    String html;
    if (firstnameAtFirst) {
      html = userData.getFirstName() + " " + userData.getLastName();
    } else {
      html = userData.getLastName() + " " + userData.getFirstName();
    }
    html += " <span class='status'>" + userData.getStatus() + "</span>";

    if (userData.getConnected()) html += "<span class='connected'></span>";
    HTML h = new HTML(html);
    h.getElement().setAttribute("user-id", userData.getId());
    h.addClickHandler(this);
    user.add(h);
    mail.setText(userData.geteMail());
    if (userData.geteMail() == null || userData.geteMail().isEmpty()) {
      mail.setHTML("&nbsp");
    }
    mail.setHref("mailto:" + userData.geteMail());

    int nbTel = 0;
    if (userData.getPhoneNumber() != null && !userData.getPhoneNumber().isEmpty()) {
      createPhoneFragment(tel, userData.getPhoneNumber());
      nbTel++;
    }
    if (userData.getCellularPhoneNumber() != null && !userData.getCellularPhoneNumber().isEmpty()) {
      if (nbTel == 1) {
        tel.add(new InlineHTML(" | "));
      }
      createPhoneFragment(tel, userData.getCellularPhoneNumber());
      createSmsFragment(tel, userData.getCellularPhoneNumber());
      nbTel++;
    }
    if (userData.getFaxPhoneNumber() != null && !userData.getFaxPhoneNumber().isEmpty()) {
      if (nbTel == 2) {
        tel.add(new InlineHTML(" | "));
      }
      createPhoneFragment(tel, userData.getPhoneNumber());
      createPhoneFragment(tel, userData.getFaxPhoneNumber());
      nbTel++;
    }
    if (nbTel == 0) {
      tel.setVisible(false);
    }

    for (PropertyDTO prop :userData.getProperties()) {
      String value = prop.getValue();
      if (value != null & !value.isEmpty()) {
        if (isPhoneNumber(value)) {
          HTMLPanel field = new HTMLPanel("");
          createPhoneFragment(field, value);
          createSmsFragment(field, value);
          container.add(field);
        } else {
          HTML field = new HTML(value);
          field.setStylePrimaryName(prop.getKey());
          container.add(field);
        }
      }
    }
  }

  private void createPhoneFragment(HTMLPanel parent, String value) {
    Anchor tel = new Anchor();
    tel.setStyleName("tel-link");
    tel.setHref("tel:" + value);
    tel.getElement().setInnerHTML(resources.call().getText());
    SpanElement text = Document.get().createSpanElement();
    text.setInnerText(value);
    tel.getElement().appendChild(text);
    parent.add(tel);
  }
  private void createSmsFragment(HTMLPanel parent, String value) {
    Anchor sms = new Anchor();
    sms.setStyleName("sms-link");
    sms.setHref("sms:" + value);
    sms.getElement().setInnerHTML(resources.sms().getText());
    parent.add(sms);
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
