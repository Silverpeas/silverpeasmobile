/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.admin.service.OrganizationControllerProvider;
import org.silverpeas.core.questioncontainer.answer.model.Answer;
import org.silverpeas.core.questioncontainer.container.model.QuestionContainerDetail;
import org.silverpeas.core.questioncontainer.container.model.QuestionContainerHeader;
import org.silverpeas.core.questioncontainer.container.model.QuestionContainerPK;
import org.silverpeas.core.questioncontainer.container.service.QuestionContainerService;
import org.silverpeas.core.questioncontainer.question.model.Question;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.shared.dto.survey.AnswerDTO;
import org.silverpeas.mobile.shared.dto.survey.QuestionDTO;
import org.silverpeas.mobile.shared.dto.survey.ResponseDTO;
import org.silverpeas.mobile.shared.dto.survey.SurveyDTO;
import org.silverpeas.mobile.shared.dto.survey.SurveyDetailDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.SurveyException;
import org.silverpeas.mobile.shared.services.ServiceSurvey;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service de gestion des news.
 *
 * @author svu
 */
public class ServiceSurveyImpl extends AbstractAuthenticateService implements ServiceSurvey {

  private static final long serialVersionUID = 1L;


  @Override
  public List<SurveyDTO> getSurveys(final String instanceId)
      throws SurveyException, AuthenticationException {
    checkUserInSession();
    QuestionContainerPK pk = new QuestionContainerPK(null, null, instanceId);
    Collection<QuestionContainerHeader> questions =
        QuestionContainerService.get().getOpenedQuestionContainers(pk);
    return populate(questions);
  }

  @Override
  public void saveSurvey(final SurveyDetailDTO data)
      throws SurveyException, AuthenticationException {
    checkUserInSession();
    try {
      QuestionContainerPK pk = new QuestionContainerPK(data.getId(), null, null);

      Map<String, List<String>> reply = new HashMap<>();

      for (QuestionDTO question : data.getQuestions()) {
        List<String> values = new ArrayList<>();
        for (ResponseDTO response : question.getResponses()) {
          values.add(response.getId());
          if (!response.getContent().isEmpty()) values.add("OA" + response.getContent());
        }
        reply.put(question.getId(), values);
      }
      QuestionContainerService.get()
          .recordReplyToQuestionContainerByUser(pk, getUserInSession().getId(), reply, data.getComments(), data.isAnonymComment());
    } catch (Throwable t) {
      SilverLogger.getLogger(this).error(t);
    }
  }

  @Override
  public SurveyDetailDTO getSurvey(final String id, final String instanceId)
      throws SurveyException, AuthenticationException {
    checkUserInSession();

    QuestionContainerPK pk = new QuestionContainerPK(id, null, null);
    int nbParticipations = QuestionContainerService.get()
        .getUserNbParticipationsByFatherId(pk, getUserInSession().getId());

    QuestionContainerDetail questions =
        QuestionContainerService.get().getQuestionContainer(pk, getUserInSession().getId());
    return populate(questions, instanceId, nbParticipations);
  }

  private boolean isMultipleParticipant(String componentId) {
    List<String> userMultipleRole = new ArrayList<>();
    userMultipleRole.add("userMultiple");
    String[] ids = OrganizationControllerProvider.getOrganisationController()
        .getUsersIdsByRoleNames(componentId, userMultipleRole);

    return Arrays.asList(ids).contains(getUserInSession().getId());
  }

  private SurveyDetailDTO populate(final QuestionContainerDetail questions, final String instanceId,
      final int nbParticipations) {
    SurveyDetailDTO dto = new SurveyDetailDTO();
    dto.setNbParticipation(nbParticipations);
    if (isMultipleParticipant(instanceId)) {
      dto.setCanParticipate(true);
    } else if (nbParticipations > 1) {
      dto.setCanParticipate(false);
    } else if (nbParticipations < 1) {
      dto.setCanParticipate(true);
    }

    dto.setId(questions.getId());
    String imagePath =
        System.getenv("SILVERPEAS_HOME") + File.separator + "data" + File.separator + "workspaces" +
            File.separator + instanceId + File.separator + "images";
    for (Question question : questions.getQuestions()) {
      QuestionDTO q = new QuestionDTO();
      q.setId(question.getPK().getId());
      q.setType(question.getStyle());
      q.setLabel(question.getLabel());
      for (Answer answer : question.getAnswers()) {
        AnswerDTO a = new AnswerDTO();
        a.setId(answer.getPK().getId());
        if (answer.getImage() != null) a.setImage(DataURLHelper.convertPictureToUrlData(imagePath, answer.getImage(), null));
        a.setLabel(answer.getLabel());
        a.setComments(answer.getComment());
        q.getAnswers().add(a);
      }
      dto.getQuestions().add(q);
    }

    return dto;
  }

  private List<SurveyDTO> populate(final Collection<QuestionContainerHeader> questions) {
    List<SurveyDTO> dtos = new ArrayList<>();
    for (QuestionContainerHeader question : questions) {
      SurveyDTO dto = new SurveyDTO();
      dto.setId(question.getId());
      dto.setDescription(question.getDescription());
      dto.setName(question.getName());
      dto.setNbVotes(String.valueOf(question.getNbVoters()));
      //dto.setCreationDate(question.getCreationDate());
      dto.setBeginDate(question.getBeginDate());
      dto.setEndDate(question.getEndDate());
      //dto.setCreator();
      //dto.setNbMaxParticipations();
      //dto.setResultMode();
      //dto.setResultView();
      dtos.add(dto);
    }

    return dtos;
  }
}