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

package org.silverpeas.mobile.client.apps.formsonline;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.agenda.AgendaApp;
import org.silverpeas.mobile.client.apps.formsonline.events.app.AbstractFormsOnlineAppEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormsOnlineAppEventHandler;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormsOnlineLoadEvent;
import org.silverpeas.mobile.client.apps.formsonline.pages.FormsOnlinePage;
import org.silverpeas.mobile.client.apps.formsonline.resources.FormsOnlineMessages;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.ArrayList;
import java.util.List;

public class FormsOnlineApp extends App implements FormsOnlineAppEventHandler, NavigationEventHandler {

  private FormsOnlineMessages msg;
  private ApplicationInstanceDTO instance;
  private String keyForms;

  public FormsOnlineApp(){
    super();
    msg = GWT.create(FormsOnlineMessages.class);
    EventBus.getInstance().addHandler(AbstractFormsOnlineAppEvent.TYPE, this);
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
  public void loadFormsOnline(final FormsOnlineLoadEvent event) {

    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<FormDTO> forms = LocalStorageHelper.load(keyForms, List.class);
        if (forms == null) {
          forms = new ArrayList<FormDTO>();
        }
        //TODO
        Window.alert("Forms number = " + forms.size());
      }
    };

    MethodCallbackOnlineOrOffline action = new MethodCallbackOnlineOrOffline<List<FormDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceFormsOnline().getSendablesForms(instance.getId(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<FormDTO> forms) {
        super.onSuccess(method, forms);
        //TODO
        Window.alert("Forms number = " + forms.size());
      }
    };
    action.attempt();

  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.formsOnline.name())) {
      this.instance = event.getInstance();

      keyForms = "forms_" + instance.getId();

      FormsOnlinePage page = new FormsOnlinePage();
      page.setPageTitle(event.getInstance().getLabel());
      page.show();
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {

  }

}