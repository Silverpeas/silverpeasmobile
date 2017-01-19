package com.silverpeas.mobile.client.common.app;

import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.components.base.events.apps.AbstractAppEvent;
import com.silverpeas.mobile.client.components.base.events.apps.AppEvent;
import com.silverpeas.mobile.client.components.base.events.apps.AppEventHandler;

public abstract class App implements AppEventHandler {

  private PageContent mainPage;

  public App() {
    EventBus.getInstance().addHandler(AbstractAppEvent.TYPE, this);
  }

  public void start() {
    PageHistory.getInstance().goTo(mainPage);
  }

  public void startAsWidget(){

  }

  public void startWithContent(String appId, String contentType, String contentId) {

  }

  public void stop() {
    EventBus.getInstance().removeHandler(AbstractAppEvent.TYPE, this);
  }

  public PageContent getMainPage() {
    return mainPage;
  }

  protected void setMainPage(PageContent mainPage) {
    this.mainPage = mainPage;
    this.mainPage.setApp(this);

  }

  @Override
  public void receiveEvent(AppEvent event) {
    // for compatibility
  }
}
