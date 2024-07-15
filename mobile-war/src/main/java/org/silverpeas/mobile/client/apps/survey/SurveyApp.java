/*
 * Copyright (C) 2000 - 2024 Silverpeas
 *
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
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
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.survey.events.app.AbstractSurveyAppEvent;
import org.silverpeas.mobile.client.apps.survey.events.app.SurveyAppEventHandler;
import org.silverpeas.mobile.client.apps.survey.events.app.SurveyLoadEvent;
import org.silverpeas.mobile.client.apps.survey.events.app.SurveySaveEvent;
import org.silverpeas.mobile.client.apps.survey.events.app.SurveysLoadEvent;
import org.silverpeas.mobile.client.apps.survey.events.pages.SurveyLoadedEvent;
import org.silverpeas.mobile.client.apps.survey.events.pages.SurveysLoadedEvent;
import org.silverpeas.mobile.client.apps.survey.events.pages.UpdateParticipationNumberEvent;
import org.silverpeas.mobile.client.apps.survey.pages.SurveyPage;
import org.silverpeas.mobile.client.apps.survey.pages.SurveysPage;
import org.silverpeas.mobile.client.apps.survey.resources.SurveyMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.survey.AnswerDTO;
import org.silverpeas.mobile.shared.dto.survey.QuestionDTO;
import org.silverpeas.mobile.shared.dto.survey.SurveyDTO;
import org.silverpeas.mobile.shared.dto.survey.SurveyDetailDTO;

import java.util.List;

public class SurveyApp extends App implements SurveyAppEventHandler, NavigationEventHandler {

  private SurveyMessages msg;

  public SurveyApp() {
    super();
    msg = GWT.create(SurveyMessages.class);
    EventBus.getInstance().addHandler(AbstractSurveyAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start() {
    // always start
  }

  @Override
  public void stop() {
    // nevers stop
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.survey.name()) ||
        event.getInstance().getType().equalsIgnoreCase(Apps.pollingStation.name())) {
      setApplicationInstance(event.getInstance());
      SurveysPage page = new SurveysPage();
      page.setPageTitle(event.getInstance().getLabel());
      page.show();
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.QuestionContainer.toString()) &&
        event.getContent().getInstanceId().startsWith(Apps.survey.name()) ||
        event.getContent().getType().startsWith(Apps.pollingStation.name())) {
      startWithContent(event.getContent());
    } else if (event.getContent().getType().equals(ContentsTypes.Component.toString()) &&
        event.getContent().getInstanceId().startsWith(Apps.survey.name()) ||
        event.getContent().getType().startsWith(Apps.pollingStation.name())) {
      ApplicationInstanceDTO appDTO = new ApplicationInstanceDTO();
      appDTO.setId(event.getContent().getInstanceId());
      appDTO.setType(Apps.survey.name());
      NavigationAppInstanceChangedEvent navEvent = new NavigationAppInstanceChangedEvent(appDTO);
      appInstanceChanged(navEvent);
    }
  }

  @Override
  public void startWithContent(final ContentDTO content) {
    ApplicationInstanceDTO appDTO = new ApplicationInstanceDTO();
    appDTO.setId(content.getInstanceId());
    setApplicationInstance(appDTO);
    if (content.getType().equals(ContentsTypes.QuestionContainer.toString())) {
      SurveyPage page = new SurveyPage();
      page.setData(content.getId());
      page.show();
    }
  }

  @Override
  public void loadSurveys(final SurveysLoadEvent event) {

    MethodCallbackOnlineOnly action =
        new MethodCallbackOnlineOnly<List<SurveyDTO>>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceSurvey().getSurveys(getApplicationInstance().getId(), this);
          }

          @Override
          public void onSuccess(final Method method, final List<SurveyDTO> result) {
            super.onSuccess(method, result);
            EventBus.getInstance()
                .fireEvent(new SurveysLoadedEvent(result, getApplicationInstance().getId()));
          }
        };
    action.attempt();
  }

  @Override
  public void loadSurvey(final SurveyLoadEvent event) {
    MethodCallbackOnlineOnly action =
        new MethodCallbackOnlineOnly<SurveyDetailDTO>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceSurvey()
                .getSurvey(getApplicationInstance().getId(), event.getId(), this);
          }

          @Override
          public void onSuccess(final Method method, final SurveyDetailDTO surveyDetailDTO) {
            super.onSuccess(method, surveyDetailDTO);
            EventBus.getInstance().fireEvent(new SurveyLoadedEvent(surveyDetailDTO));
          }
        };
    action.attempt();
  }

  @Override
  public void saveSurvey(final SurveySaveEvent event) {

    // remove image (for upload faster)
    for (QuestionDTO q : event.getData().getQuestions()) {
      for (AnswerDTO a : q.getAnswers()) {
        a.setImage("");
      }
    }

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceSurvey()
            .saveSurvey(getApplicationInstance().getId(), event.getData(), this);
      }

      @Override
      public void onSuccess(final Method method, final Void unused) {
        super.onSuccess(method, unused);
        EventBus.getInstance().fireEvent(new UpdateParticipationNumberEvent(event.getData()));
        PageHistory.getInstance().back();
      }
    };
    action.attempt();
  }
}
