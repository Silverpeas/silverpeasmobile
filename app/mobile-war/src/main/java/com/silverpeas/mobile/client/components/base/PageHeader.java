package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.common.resources.ResourcesManager;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;

public class PageHeader extends Composite {

  private static PageHeaderUiBinder uiBinder = GWT.create(PageHeaderUiBinder.class);

  interface PageHeaderUiBinder extends UiBinder<Widget, PageHeader> {
  }

  @UiField protected HTMLPanel header;
  @UiField protected Anchor menu, back;
  @UiField protected HeadingElement title;
  @UiField(provided = true) protected ApplicationMessages msg = null;
  protected ApplicationResources ressources = null;

  public PageHeader() {
    msg = GWT.create(ApplicationMessages.class);
    ressources = GWT.create(ApplicationResources.class);
    initWidget(uiBinder.createAndBindUi(this));
    header.getElement().setId("header");
    menu.getElement().setId("menu");
    ressources.css().ensureInjected();

    title.setInnerText(ResourcesManager.getLabel("mainpage.title"));
  }


  @UiHandler("menu")
  void onMenu(ClickEvent event) {
    SpMobil.getMainPage().toogleMenu();
  }

  @UiHandler("back")
  void onBack(ClickEvent event) {
    PageHistory.getInstance().back();
  }

  public void showOfflineIndicator() {
    menu.addStyleName(ressources.css().offline());
  }

  public void hideOfflineIndicator() {
    menu.removeStyleName(ressources.css().offline());
  }

  public void setVisibleBackButton(boolean visible) {
    // remove active state
    header.remove(back);
    header.add(back);
    // hide button
    back.setVisible(visible);
  }

  public void setPageTitle(String title) {
    this.title.setInnerHTML(title);
  }

  public int getHeight() {
    return header.getOffsetHeight();
  }

}
