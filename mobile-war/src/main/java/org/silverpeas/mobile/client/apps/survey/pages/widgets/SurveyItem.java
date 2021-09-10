/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.survey.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.survey.pages.SurveyPage;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.survey.SurveyDTO;

public class SurveyItem extends Composite implements ClickHandler {

  private SurveyDTO data;
  private static FavoriteItemUiBinder uiBinder = GWT.create(FavoriteItemUiBinder.class);
  @UiField
  InlineHTML name, date, nbParticipants;

  @UiField
  HTMLPanel container;
  protected ApplicationMessages msg = null;

  interface FavoriteItemUiBinder extends UiBinder<Widget, SurveyItem> {}

  public SurveyItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
    name.addClickHandler(this);
    date.addClickHandler(this);
    nbParticipants.addClickHandler(this);
  }

  public void setData(SurveyDTO data) {
    this.data = data;
    name.setText(data.getName());
    date.setText(data.getEndDate());
    nbParticipants.setText(data.getNbVotes());
  }

  public SurveyDTO getData() {
    return data;
  }

  @Override
  public void onClick(final ClickEvent event) {
    if (data.getId() != null) {
      SurveyPage page = new SurveyPage();
      page.setData(data.getId());
      page.show();
    }
  }
}
