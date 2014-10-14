package com.silverpeas.mobile.client.apps.status.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.status.events.app.StatusPostEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.AbstractStatusPagesEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPagesEventHandler;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostedEvent;
import com.silverpeas.mobile.client.apps.status.resources.StatusMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.components.base.PageContent;

public class StatusPage extends PageContent implements StatusPagesEventHandler {

  private static StatusPageUiBinder uiBinder = GWT.create(StatusPageUiBinder.class);

  @UiField(provided = true) protected StatusMessages msg = null;
  @UiField protected HTMLPanel container;
  @UiField protected TextArea status;
  @UiField protected Anchor publish;

  interface StatusPageUiBinder extends UiBinder<Widget, StatusPage> {
  }

  public StatusPage() {
    msg = GWT.create(StatusMessages.class);
    setPageTitle(msg.title().asString());
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("update-statut");
    status.getElement().setAttribute("x-webkit-speech", "x-webkit-speech");
    status.getElement().setAttribute("speech", "speech");
    EventBus.getInstance().addHandler(AbstractStatusPagesEvent.TYPE, this);
  }

  @UiHandler("publish")
  void publish(ClickEvent event) {
	// send event to app
    EventBus.getInstance().fireEvent(new StatusPostEvent(status.getText()));
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractStatusPagesEvent.TYPE, this);
  }

  @Override
  public void onStatusPost(StatusPostedEvent event) {
    back();
  }
}