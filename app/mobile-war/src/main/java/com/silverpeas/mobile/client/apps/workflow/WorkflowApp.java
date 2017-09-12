/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.client.apps.workflow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.documents.events.app.AbstractDocumentsAppEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsAppEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadGedItemsEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.pages.GedNavigationPage;
import com.silverpeas.mobile.client.apps.documents.pages.PublicationPage;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import com.silverpeas.mobile.client.apps.workflow.events.app.AbstractWorkflowAppEvent;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowAppEventHandler;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadInstancesEvent;
import com.silverpeas.mobile.client.apps.workflow.pages.WorkflowPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.client.components.IframePage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.ContentDTO;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.Apps;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowInstanceDTO;

import java.util.ArrayList;
import java.util.List;

public class WorkflowApp extends App implements NavigationEventHandler, WorkflowAppEventHandler {

    private ApplicationMessages globalMsg;


    public WorkflowApp() {
        super();
        globalMsg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
        EventBus.getInstance().addHandler(AbstractWorkflowAppEvent.TYPE, this);
    }

    @Override
    public void start() {
      // no "super.start(lauchingPage);" this apps is used in another apps
    }

    @Override
    public void stop() {
        // never stop
    }

    @Override
    public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
      if (event.getInstance().isWorkflow()) {
        ServicesLocator.getServiceWorkflow().getInstances(event.getInstance().getId(), new AsyncCallback
            <List<WorkflowInstanceDTO>>() {
          @Override
          public void onFailure(final Throwable throwable) {
            EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
          }

          @Override
          public void onSuccess(final List<WorkflowInstanceDTO> workflowInstanceDTOS) {
            WorkflowPage page = new WorkflowPage();
            page.setData(workflowInstanceDTOS);
            page.show();
          }
        });
      }
    }

  @Override
  public void showContent(final NavigationShowContentEvent event) {

  }

  @Override
  public void loadInstances(final WorkflowLoadInstancesEvent event) {






  }
}
