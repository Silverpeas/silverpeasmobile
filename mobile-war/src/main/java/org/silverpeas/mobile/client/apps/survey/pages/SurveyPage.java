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

package org.silverpeas.mobile.client.apps.survey.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.survey.events.app.SurveyLoadEvent;
import org.silverpeas.mobile.client.apps.survey.events.app.SurveySaveEvent;
import org.silverpeas.mobile.client.apps.survey.events.pages.AbstractSurveyPagesEvent;
import org.silverpeas.mobile.client.apps.survey.events.pages.SurveyLoadedEvent;
import org.silverpeas.mobile.client.apps.survey.events.pages.SurveyPagesEventHandler;
import org.silverpeas.mobile.client.apps.survey.events.pages.SurveysLoadedEvent;
import org.silverpeas.mobile.client.apps.survey.events.pages.UpdateParticipationNumberEvent;
import org.silverpeas.mobile.client.apps.survey.pages.widgets.QuestionItem;
import org.silverpeas.mobile.client.apps.survey.resources.SurveyMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.survey.QuestionDTO;
import org.silverpeas.mobile.shared.dto.survey.SurveyDetailDTO;

import java.util.Iterator;

public class SurveyPage extends PageContent implements SurveyPagesEventHandler {

  private static SurveyPageUiBinder uiBinder = GWT.create(SurveyPageUiBinder.class);

  @UiField(provided = true) protected SurveyMessages msg = null;
  @UiField(provided = true) protected ApplicationMessages msgApp = null;

  @UiField
  UnorderedList questions;

  @UiField
  Anchor ok, cancel, newParticipation;

  @UiField
  TextArea comments;

  @UiField
  CheckBox anonymComment;

  @UiField
  HTMLPanel secondPart, participationPart;

  @UiField
  InlineHTML participations;

  private SurveyDetailDTO data;

  interface SurveyPageUiBinder extends UiBinder<Widget, SurveyPage> {
  }

  public SurveyPage() {
    msgApp = GWT.create(ApplicationMessages.class);
    msg = GWT.create(SurveyMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    ok.setText(msgApp.ok());
    cancel.setText(msgApp.cancelBtnLabel());
    EventBus.getInstance().addHandler(AbstractSurveyPagesEvent.TYPE, this);
  }

  public void setData(String id) {
    SurveyLoadEvent event = new SurveyLoadEvent();
    event.setId(id);
    EventBus.getInstance().fireEvent(event);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractSurveyPagesEvent.TYPE, this);
  }

  @Override
  public void onSurveysLoad(final SurveysLoadedEvent event) {}

  @Override
  public void onSurveyLoad(final SurveyLoadedEvent event) {
    this.data = event.getSurvey();
    participations.setText(msg.nbParticipation(""+data.getNbParticipation()));
    if (data.isCanParticipate() && data.getNbParticipation() < 1) {
      insertQuestions();
      participationPart.setVisible(false);
    } else {
      secondPart.setVisible(false);
      participationPart.setVisible(true);
      if (data.getNbParticipation() > 1) {
        newParticipation.setVisible(true);
      } else {
        newParticipation.setVisible(false);
      }
    }
    Notification.activityStop();
  }

  @Override
  public void onUpdateParticipationNumber(
      final UpdateParticipationNumberEvent updateParticipationNumberEvent) {
  }

  private void insertQuestions() {
    for (QuestionDTO q : data.getQuestions()) {
      QuestionItem item = new QuestionItem();
      item.setData(q);
      questions.add(item);
    }
  }

  @UiHandler("ok")
  protected void ok(ClickEvent e) {
    data.getQuestions().clear();
    Iterator it = questions.iterator();
    while (it.hasNext()) {
      QuestionItem item = (QuestionItem) it.next();
      data.getQuestions().add(item.updateWithResponse());
    }
    data.setComments(comments.getText());
    data.setAnonymComment(anonymComment.getValue().booleanValue());
    data.setNbParticipation(data.getNbParticipation()+1);
    EventBus.getInstance().fireEvent(new SurveySaveEvent(data));
  }

  @UiHandler("cancel")
  protected void cancel(ClickEvent e) {
    back();
  }

  @UiHandler("newParticipation")
  protected void newParticipation(ClickEvent e) {
    participationPart.setVisible(false);
    secondPart.setVisible(true);
    insertQuestions();
  }
}