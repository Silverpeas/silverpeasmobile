package com.silverpeas.mobile.client.apps.news.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.news.resources.NewsMessages;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;

public class NewsItem {

  private Anchor titleLink, imageLink;
  private LIElement container;
  private boolean visible = true;
  private String id, componentId;

  private ClickHandler more = new ClickHandler() {
    @Override
    public void onClick(ClickEvent event) {
      Anchor a = (Anchor) event.getSource();
      App app = new DocumentsApp();
      app.startWithContent(componentId, ContentsTypes.Publication.toString(), id);
    }
  };

  private NewsItem() {
  }

  protected NewsItem(HTMLPanel content, Element element) {
    id = element.getAttribute("data-id");
    componentId = element.getAttribute("data-app-id");

    NodeList<Element> links = element.getElementsByTagName("a");
    Element link = links.getItem(0);
    imageLink = new Anchor();
    imageLink.setHTML(link.getInnerHTML());
    imageLink.setHref("javascript:;");
    content.addAndReplaceElement(imageLink, link);
    imageLink.addClickHandler(more);

    link = links.getItem(1);
    titleLink = new Anchor();
    titleLink.setHTML(link.getInnerHTML());
    titleLink.setHref("javascript:;");
    content.addAndReplaceElement(titleLink, link);
    titleLink.addClickHandler(more);
    container = element.cast();
  }

  public static NewsItem wrap(HTMLPanel content, Element element) {
    NewsItem item = new NewsItem(content, element);
    return item;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
    if (!visible) {
      container.getStyle().setDisplay(Style.Display.NONE);
    } else {
      container.getStyle().clearDisplay();
    }
  }

  public boolean isVisible() {
    return visible;
  }

}
