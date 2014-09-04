package com.silverpeas.mobile.client.pages.error;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.components.base.PageContent;

/**
 * @author: svu
 */
public class ErrorPage extends Composite {
  interface ErrorPageUiBinder extends
      UiBinder<HTMLPanel, ErrorPage> {
  }

  @UiField ParagraphElement textError;
  @UiField Anchor button;

  private static ErrorPageUiBinder uiBinder = GWT.create(ErrorPageUiBinder.class);

  public ErrorPage() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setText(String text) {
    textError.setId("text-error");
    textError.setInnerHTML(text);
  }

  @UiHandler("button")
  void close(ClickEvent event) {
    Window.Location.reload();
  }

}