package com.silverpeas.mobile.client.pages.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.contacts.ContactsApp;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.media.MediaApp;
import com.silverpeas.mobile.client.apps.news.NewsApp;
import com.silverpeas.mobile.client.apps.status.StatusApp;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.NetworkHelper;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;

public class AppList extends PageContent {

  private static AppListUiBinder uiBinder = GWT.create(AppListUiBinder.class);

  @UiField(provided = true) protected ApplicationMessages msg = null;
  @UiField protected Anchor statut, contact, document, media;
  @UiField protected SimplePanel news;

  interface AppListUiBinder extends UiBinder<Widget, AppList> {
  }

  public AppList() {
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));

    // add widgets on main page
    //NewsApp app = NewsApp.getInstance();
    //app.startAsWidget(news);
  }

  @UiHandler("statut")
  void status(ClickEvent e) {
    App app = new StatusApp();
    app.start();
  }

  @UiHandler("contact")
  void contacts(ClickEvent e) {
    App app = new ContactsApp();
    app.start();
  }

  @UiHandler("media")
  void gallery(ClickEvent e) {
    App app = new MediaApp();
    app.start();
  }

  @UiHandler("document")
  void documents(ClickEvent e) {
    App app = new DocumentsApp();
    app.start();
  }
}
