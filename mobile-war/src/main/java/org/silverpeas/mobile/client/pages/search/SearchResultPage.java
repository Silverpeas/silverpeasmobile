/*
 * Copyright (C) 2000 - 2022 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.pages.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.search.ResultDTO;

import java.util.List;

/**
 * @author: svu
 */
public class SearchResultPage extends PageContent implements View {

  @UiField
  UnorderedList list;
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