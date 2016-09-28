package com.silverpeas.mobile.client.apps.news.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.news.resources.NewsMessages;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;

public class NewsItem extends Composite {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);

  @UiField HTMLPanel container;

  @UiField HeadingElement title;
  @UiField ParagraphElement description, updateDate;
  @UiField Image vignette;
  @UiField Anchor more;
  @UiField(provided = true) protected NewsMessages msg = null;

  private NewsDTO data;

  interface ContactItemUiBinder extends UiBinder<Widget, NewsItem> {
  }

  public NewsItem() {
    msg = GWT.create(NewsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(NewsDTO data) {
    this.data = data;
    // TODO : display
    title.setInnerHTML(data.getTitle());
    description.setInnerHTML(data.getDescription());
    updateDate.setInnerHTML(data.getUpdateDate());
    vignette.setUrl(data.getVignette());
  }

  @UiHandler("more")
  void viewMore(ClickEvent event) {
    App app = new DocumentsApp();
    app.startWithContent(data.getInstanceId(), ContentsTypes.Publication.toString(), String.valueOf(data.getId()));
  }
}
