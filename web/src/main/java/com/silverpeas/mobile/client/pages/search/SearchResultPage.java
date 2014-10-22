package com.silverpeas.mobile.client.pages.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.media.MediaApp;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.search.ResultDTO;

import java.util.List;

/**
 * @author: svu
 */
public class SearchResultPage extends PageContent implements View {

  @UiField UnorderedList list;
  private List<ResultDTO> results;

  interface SearchResultPageUiBinder extends
      UiBinder<HTMLPanel, SearchResultPage> {
  }

  private static SearchResultPageUiBinder uiBinder = GWT.create(SearchResultPageUiBinder.class);

  public SearchResultPage() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setResults(final List<ResultDTO> results){
    this.results = results;
    display();
  }

  private void display() {
    for (final ResultDTO result : results) {
      Anchor link = new Anchor();
      link.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(final ClickEvent event) {
          if (result.getType().equals(ContentsTypes.Attachment.toString())) {
            App app = new DocumentsApp();
            app.startWithContent(result.getComponentId(), result.getType(), result.getId());
          } else if (result.getType().equals(ContentsTypes.Publication.toString())) {
            App app = new DocumentsApp();
            app.startWithContent(result.getComponentId(), result.getType(), result.getId());
          } else if (result.getType().equals(ContentsTypes.Photo.toString())) {
            App app = new MediaApp();
            app.startWithContent(result.getComponentId(), result.getType(), result.getId());
          }
        }
      });
      link.setText(result.getTitle());
      list.add(link);
    }
  }
}