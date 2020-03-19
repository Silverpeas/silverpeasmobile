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

package org.silverpeas.mobile.client.apps.faq;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.faq.events.app.AbstractFaqAppEvent;
import org.silverpeas.mobile.client.apps.faq.events.app.FaqAppEventHandler;
import org.silverpeas.mobile.client.apps.faq.events.app.FaqCategoriesLoadEvent;
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
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.shared.dto.faq.CategoryDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.ArrayList;
import java.util.List;

public class FaqApp extends App implements NavigationEventHandler, FaqAppEventHandler {

  private FaqMessages msg;
  private String keysQuestions = "FaqQuestions";
  private String keysCategories = "FaqCategories";

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

      Command offlineAction = new Command() {
        @Override
        public void execute() {
          List<QuestionDTO> questions = LocalStorageHelper.load(keysQuestions+getApplicationInstance().getId(), List.class);
          if (questions == null) {
            questions = new ArrayList<QuestionDTO>();
          }
          FaqPage page = new FaqPage();
          page.setApp(FaqApp.this);
          page.setData(questions);
          page.show();
        }
      };

      MethodCallbackOnlineOrOffline action = new MethodCallbackOnlineOrOffline<List<QuestionDTO>>(offlineAction) {
        @Override
        public void attempt() {
          ServicesLocator.getServiceFaq().getAllQuestions(getApplicationInstance().getId(), this);
        }

        @Override
        public void onSuccess(final Method method, final List<QuestionDTO> questions) {
          super.onSuccess(method, questions);
          LocalStorageHelper.store(keysQuestions+getApplicationInstance().getId(), List.class, questions);
          FaqPage page = new FaqPage();
          page.setApp(FaqApp.this);
          page.setData(questions);
          page.show();
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
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<CategoryDTO> categories = LocalStorageHelper.load(keysCategories+getApplicationInstance().getId(), List.class);
        if (categories == null) {
          categories = new ArrayList<CategoryDTO>();
        }
        EventBus.getInstance().fireEvent(new FaqCategoriesLoadedEvent(categories));
      }
    };

    MethodCallbackOnlineOrOffline action = new MethodCallbackOnlineOrOffline<List<CategoryDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceFaq().getAllCategories(getApplicationInstance().getId(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<CategoryDTO> categories) {
        super.onSuccess(method, categories);
        LocalStorageHelper.store(keysCategories+getApplicationInstance().getId(), List.class, categories);
        EventBus.getInstance().fireEvent(new FaqCategoriesLoadedEvent(categories));
      }
    };
    action.attempt();
  }
}
