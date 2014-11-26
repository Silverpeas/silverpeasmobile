package com.silverpeas.mobile.client.apps.news.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.TaskDTO;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;

public class NewsItem extends Composite {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);

  @UiField HTMLPanel container;
  @UiField Label title, description, updateDate;
  @UiField Image vignette;
  @UiField Anchor more;

  private NewsDTO data;

  interface ContactItemUiBinder extends UiBinder<Widget, NewsItem> {
  }

  public NewsItem() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(NewsDTO data) {
    this.data = data;
    // TODO : display
    title.setText(data.getTitle());
    description.setText(data.getDescription());
    updateDate.setText(data.getUpdateDate());
    vignette.setUrl(data.getVignette());
  }

  @UiHandler("more")
  void viewMore(ClickEvent event) {
    App app = new DocumentsApp();
    app.startWithContent(data.getInstanceId(), ContentsTypes.Publication.toString(), String.valueOf(data.getId()));
  }
}
