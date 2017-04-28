package com.silverpeas.mobile.client.pages.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.media.MediaApp;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.ContentDTO;
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
      final Anchor link = new Anchor();
      link.setStyleName("ui-btn ui-btn-icon-right ui-icon-carat-r");
      link.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(final ClickEvent event) {
          ContentDTO content = new ContentDTO();
          content.setId(result.getId());
          content.setType(result.getType());
          content.setInstanceId(result.getComponentId());
          if (result.getType().equals(ContentsTypes.Attachment.toString())) {
            Anchor l = ((Anchor)event.getSource());
            if (l.getHref().isEmpty()) {
              content.setLink(l);
            }
          }
          EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));




        }
      });
      link.setText(result.getTitle());
      if (result.getType().equals(ContentsTypes.Publication.toString())) {
        list.add(link, "publication");
      } else if (result.getType().equals(ContentsTypes.Photo.toString()) || result.getType().equals(ContentsTypes.Sound.toString()) || result.getType().equals(ContentsTypes.Video.toString()) || result.getType().equals(ContentsTypes.Streaming.toString())) {
        list.add(link, "media");
      } else {
        list.add(link);
      }
    }
  }
}