/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.client.apps.survey.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.blog.events.app.BlogLoadEvent;
import org.silverpeas.mobile.client.apps.blog.events.pages.AbstractBlogPagesEvent;
import org.silverpeas.mobile.client.apps.blog.events.pages.BlogLoadedEvent;
import org.silverpeas.mobile.client.apps.blog.events.pages.BlogPagesEventHandler;
import org.silverpeas.mobile.client.apps.blog.pages.widgets.BlogItem;
import org.silverpeas.mobile.client.apps.blog.resources.BlogMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.survey.events.app.SurveysLoadEvent;
import org.silverpeas.mobile.client.apps.survey.events.pages.AbstractSurveyPagesEvent;
import org.silverpeas.mobile.client.apps.survey.events.pages.SurveyPagesEventHandler;
import org.silverpeas.mobile.client.apps.survey.events.pages.SurveysLoadedEvent;
import org.silverpeas.mobile.client.apps.survey.resources.SurveyMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.blog.PostDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SurveyPage extends PageContent implements SurveyPagesEventHandler {

  private static SurveyPageUiBinder uiBinder = GWT.create(SurveyPageUiBinder.class);

  @UiField(provided = true) protected SurveyMessages msg = null;
  @UiField
  UnorderedList surveys;
  @UiField
  ActionsMenu actionsMenu;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private String instanceId;

  interface SurveyPageUiBinder extends UiBinder<Widget, SurveyPage> {
  }

  public SurveyPage() {
    msg = GWT.create(SurveyMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractSurveyPagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new SurveysLoadEvent());
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractSurveyPagesEvent.TYPE, this);
  }

  @Override
  public void onSurveysLoad(final SurveysLoadedEvent event) {
    surveys.clear();

    actionsMenu.addAction(favorite);
    favorite.init(instanceId, instanceId, ContentsTypes.Component.name(), getPageTitle());
  }
}