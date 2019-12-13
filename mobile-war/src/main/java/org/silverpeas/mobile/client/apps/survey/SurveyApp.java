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

package org.silverpeas.mobile.client.apps.survey;

import com.google.gwt.core.client.GWT;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.survey.events.app.AbstractSurveyAppEvent;
import org.silverpeas.mobile.client.apps.survey.events.app.SurveyAppEventHandler;
import org.silverpeas.mobile.client.apps.survey.events.app.SurveysLoadEvent;
import org.silverpeas.mobile.client.apps.survey.resources.SurveyMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

public class SurveyApp extends App implements SurveyAppEventHandler, NavigationEventHandler {

  private SurveyMessages msg;
  private ApplicationInstanceDTO instance;

  public SurveyApp(){
    super();
    msg = GWT.create(SurveyMessages.class);
    EventBus.getInstance().addHandler(AbstractSurveyAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start(){
    // always start
  }

  @Override
  public void stop() {
    // nevers stop
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.survey.name())) {
      this.instance = event.getInstance();
      /*SurveysPage page = new SurveysPage();
      page.setPageTitle(event.getInstance().getLabel());
      page.show();*/
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals("Component") && event.getContent().getInstanceId().startsWith(Apps.survey.name())) {
      super.showContent(event);
    } else {
      //TODO
    }
  }

  @Override
  public void loadSurveys(final SurveysLoadEvent event) {
    //TODO
  }
}
