/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.client.apps.faq;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.faq.events.app.*;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqAttachmentsLoadedEvent;
import org.silverpeas.mobile.client.apps.faq.events.pages.FaqCategoriesLoadedEvent;
import org.silverpeas.mobile.client.apps.faq.pages.FaqPage;
import org.silverpeas.mobile.client.apps.faq.resources.FaqMessages;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.Snackbar;
import org.silverpeas.mobile.shared.dto.documents.DocumentType;
import org.silverpeas.mobile.shared.dto.documents.SimpleDocumentDTO;
import org.silverpeas.mobile.shared.dto.faq.CategoryDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDetailDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.List;

public class FaqApp extends App implements NavigationEventHandler, FaqAppEventHandler {

  private FaqMessages msg;
  private String keysQuestions = "FaqQuestions_";
  private String keysCategories = "FaqCategories_";

  public FaqApp(){
    super();
    msg = GWT.create(FaqMessages.class);
    EventBus.getInstance().addHandler(AbstractFaqAppEvent.TYPE, this);
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
    if (event.getInstance().getType().equals(Apps.questionReply.name())) {
      this.setApplicationInstance(event.getInstance());

      MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<QuestionDTO>>() {
        @Override
        public void attempt() {
          super.attempt();
          ServicesLocator.getServiceFaq().getAllQuestions(getApplicationInstance().getId(), this);
        }

        @Override
        public void onSuccess(final Method method, final List<QuestionDTO> questions) {
          super.onSuccess(method, questions);
          FaqPage page = new FaqPage();
          page.setApp(FaqApp.this);
          page.setData(questions);
          page.show();
          setMainPage(page);
        }
      };
      action.attempt();
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals("Component") && event.getContent().getInstanceId().startsWith(Apps.questionReply.name())) {
      super.showContent(event);
    }
  }

  @Override
  public void loadCategories(final FaqCategoriesLoadEvent event) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<CategoryDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceFaq().getAllCategories(getApplicationInstance().getId(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<CategoryDTO> categories) {
        super.onSuccess(method, categories);
        EventBus.getInstance().fireEvent(new FaqCategoriesLoadedEvent(categories));
      }
    };
    action.attempt();
  }

  @Override
  public void loadAttachments(final FaqAttachmentsLoadEvent event) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<SimpleDocumentDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getRestServiceDocuments().getDocumentsByType(getApplicationInstance().getId(), event.getContentId(),
            DocumentType.attachment.name(), SpMobil.getUser().getLanguage(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<SimpleDocumentDTO> attachments) {
        super.onSuccess(method, attachments);
        EventBus.getInstance().fireEvent(new FaqAttachmentsLoadedEvent(attachments, event.getContentId()));
      }
    };

    action.attempt();
  }

  @Override
  public void onCreateQuestion(QuestionCreateEvent event) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<QuestionDetailDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceFaq().createQuestion(getApplicationInstance().getId(), event.getQuestion(), this);
      }

      @Override
      public void onSuccess(Method method, QuestionDetailDTO questionDetailDTO) {
        super.onSuccess(method, questionDetailDTO);
        Snackbar.show(msg.createConfirmation(), Snackbar.DELAY, Snackbar.INFO);
        reloadFaq();
      }
    };
    action.attempt();
  }


  private void reloadFaq() {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<QuestionDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceFaq().getAllQuestions(getApplicationInstance().getId(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<QuestionDTO> questions) {
        super.onSuccess(method, questions);
        FaqPage page = (FaqPage) getMainPage();
        page.setData(questions);
      }
    };
    action.attempt();
  }
}
