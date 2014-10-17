package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.status.StatusApp;
import com.silverpeas.mobile.client.apps.status.events.pages.AbstractStatusPagesEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPagesEventHandler;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.search.ResultDTO;

import java.util.List;

public class NavigationMenu extends Composite implements StatusPagesEventHandler {


  //TODO retrieve and display user status

  private static NavigationMenuUiBinder uiBinder = GWT.create(NavigationMenuUiBinder.class);

  @UiField HTMLPanel container, user;
  @UiField Anchor home, disconnect, updateStatus;
  @UiField SpanElement status;
  @UiField TextBox search;

  @UiField(provided = true) protected ApplicationMessages msg = null;

  interface NavigationMenuUiBinder extends UiBinder<Widget, NavigationMenu> {
  }

  public NavigationMenu() {
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("silverpeas-navmenu-panel");
    container.getElement().getStyle().setHeight(Window.getClientHeight(), Unit.PX);
    user.getElement().setId("user");
    EventBus.getInstance().addHandler(AbstractStatusPagesEvent.TYPE, this);
  }

  @Override
  public void onStatusPost(final StatusPostedEvent event) {
    status.setInnerHTML(event.getNewStatus().getDescription());
  }

  public void toogleMenu() {
    if (container.getStyleName().contains("ui-panel-close")) {
      container.removeStyleName("ui-panel-close");
      container.addStyleName("ui-panel-open");
    } else {
      closeMenu();
    }
  }

  public void closeMenu() {
    container.removeStyleName("ui-panel-open");
    container.addStyleName("ui-panel-close");
  }

  public void resetSearchField() {
    search.setText("");
    search.setFocus(false);
  }

  @UiHandler("search")
  protected void search(KeyDownEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
      SpMobil.search(search.getText());


    }
  }

  @UiHandler("home")
  protected void goHome(ClickEvent event) {
    PageHistory.getInstance().goBackToFirst();
    closeMenu();
  }

  @UiHandler("updateStatus")
  protected void updateStatus(ClickEvent event) {
    App app = new StatusApp();
    app.start();
    closeMenu();
  }

  @UiHandler("disconnect")
  protected void disconnect(ClickEvent event) {
    closeMenu();
    SpMobil.clearIds();
    PageHistory.getInstance().clear();
    ConnexionPage connexionPage = new ConnexionPage();
    RootPanel.get().clear();
    RootPanel.get().add(connexionPage);
  }

  public void setUser(DetailUserDTO currentUser) {
    user.addAndReplaceElement(new Image(currentUser.getAvatar()), "avatar");
    user.addAndReplaceElement(
        new InlineHTML(" " + currentUser.getFirstName() + " " + currentUser.getLastName()),
        "userName");
    status.setInnerHTML(currentUser.getStatus());
  }
}
