/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.client.pages.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.contacts.ContactsApp;
import com.silverpeas.mobile.client.apps.dashboard.DashboardApp;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.gallery.GalleryApp;
import com.silverpeas.mobile.client.apps.status.StatusApp;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.components.icon.Icon;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class MainPage extends Page {

  private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);

  @UiField(provided = true)
  protected ApplicationMessages msg = null;
  @UiField(provided = true)
  protected ApplicationResources res = null;
  @UiField
  protected Icon status, contacts;

  interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
  }

  public MainPage() {
    res = GWT.create(ApplicationResources.class);
    res.css().ensureInjected();
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("status")
  void status(ClickEvent e) {
    App app = new StatusApp();
    app.start(this);
  }

  @UiHandler("contacts")
  void contacts(ClickEvent e) {
    App app = new ContactsApp();
    app.start(this);
  }

  @UiHandler("agenda")
  void agenda(ClickEvent e) {

  }

  @UiHandler("gallery")
  void gallery(ClickEvent e) {
    App app = new GalleryApp();
    app.start(this);
  }

  @UiHandler("documents")
  void documents(ClickEvent e) {
    App app = new DocumentsApp();
    app.start(this);
  }

  @UiHandler("dashboard")
  void dashboard(ClickEvent e) {
    App app = new DashboardApp();
    app.start(this);
  }
}
