/*
 * Copyright (C) 2000 - 2025 Silverpeas
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.contacts.resources.ContactsResources;
import org.silverpeas.mobile.client.resources.ApplicationResources;

public class FieldItem extends Composite {

  private static FieldItemUiBinder uiBinder = GWT.create(FieldItemUiBinder.class);
  @UiField HTMLPanel container;

  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  private ContactsResources resourcesContact = GWT.create(ContactsResources.class);

  public void setContent(Widget pic) {
    container.add(pic);
  }

  interface FieldItemUiBinder extends UiBinder<Widget, FieldItem> {
  }

  public FieldItem() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(String name, String label, String value) {
    setStyleName(name);
    InlineHTML lb = new InlineHTML();
    lb.setStyleName("");
    lb.setText(label + " : ");
    container.add(lb);
    InlineHTML vl = new InlineHTML();
    vl.setStyleName("");
    vl.setText(value);
    container.add(vl);

  }
}
