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
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Command;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.formsonline.events.app.AbstractFormsOnlineAppEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormOnlineLoadEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormSaveEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormsOnlineAppEventHandler;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormsOnlineLoadEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormOnlineLoadUserFieldEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormSavedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlineLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.pages.FormOnlineEditPage;
import org.silverpeas.mobile.client.apps.formsonline.pages.FormsOnlinePage;
import org.silverpeas.mobile.client.apps.formsonline.resources.FormsOnlineMessages;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.FormsHelper;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.ArrayList;
import java.util.List;

public class FormsOnlineApp extends App implements FormsOnlineAppEventHandler, NavigationEventHandler {

  private FormsOnlineMessages msg;
  private ApplicationInstanceDTO instance;
  private String keyForms;
  private FormDTO currentForm;

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
        FormsOnlineLoadedEvent event = new FormsOnlineLoadedEvent();
        event.setForms(forms);
        EventBus.getInstance().fireEvent(event);
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
        LocalStorageHelper.store(keyForms, List.class, forms);
        FormsOnlineLoadedEvent event = new FormsOnlineLoadedEvent();
        event.setForms(forms);
        EventBus.getInstance().fireEvent(event);
      }
    };
    action.attempt();

  }

  @Override
  public void loadFormOnline(final FormOnlineLoadEvent event) {

    currentForm = event.getForm();

    FormOnlineEditPage page = new FormOnlineEditPage();
    page.setPageTitle(event.getForm().getTitle());
    page.show();

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<FormFieldDTO>>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceFormsOnline().getForm(instance.getId(),
            event.getForm().getXmlFormName(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<FormFieldDTO> formFieldsDTO) {
        super.onSuccess(method, formFieldsDTO);
        FormLoadedEvent event = new FormLoadedEvent();
        event.setFormFields(formFieldsDTO);
        EventBus.getInstance().fireEvent(event);
      }
    };
    action.attempt();


  }

  @Override
  public void saveForm(final FormSaveEvent formSaveEvent) {
    JavaScriptObject formData = FormsHelper.createFormData();
    for (FormFieldDTO f : formSaveEvent.getData()) {
      if (f.getType().equalsIgnoreCase("file")) {
        formData = FormsHelper.populateFormData(formData, f.getName(), f.getObjectValue());
      } else if(f.getType().equalsIgnoreCase("user") || f.getType().equalsIgnoreCase("multipleUser") || f.getType().equalsIgnoreCase("group")) {
        formData = FormsHelper.populateFormData(formData, f.getName(), f.getValueId());
      } else {
        formData = FormsHelper.populateFormData(formData, f.getName(), f.getValue());
      }
    }
    saveForm(this, formData, SpMobil.getUserToken(), AuthentificationManager.getInstance().getHeader(AuthentificationManager.XSTKN), instance.getId(), currentForm.getId());
  }

  private static native void saveForm(FormsOnlineApp app, JavaScriptObject fd, String token, String stkn, String instanceId, String formId) /*-{
    var url = "/silverpeas/services/formsOnline/"+instanceId+"/saveForm/" + formId;
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("X-Silverpeas-Session", token);
    xhr.setRequestHeader("Authorization", "Bearer " + token);
    xhr.setRequestHeader("X-STKN", stkn);

    xhr.onreadystatechange = function() {
      if (xhr.readyState == 4 && xhr.status == 200) {
        // Every thing ok, file uploaded
        app.@org.silverpeas.mobile.client.apps.formsonline.FormsOnlineApp::formSaved()();
      } else {
        app.@org.silverpeas.mobile.client.apps.formsonline.FormsOnlineApp::formNotSaved(I)(xhr.status);
      }
    };

    xhr.send(fd);
  }-*/;

  public void formSaved() {
    EventBus.getInstance().fireEvent(new FormSavedEvent());
  }

  public void formNotSaved(int error) {
    EventBus.getInstance().fireEvent(new ErrorEvent(new RequestException("Error " + error)));
  }

  @Override
  public void loadUserField(final FormOnlineLoadUserFieldEvent event) {


    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<BaseDTO>>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceFormsOnline().getUserField(instance.getId(), currentForm.getXmlFormName(), event.getFieldName(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<BaseDTO> users) {
        super.onSuccess(method, users);


        AllowedUsersAndGroupsLoadedEvent ev = new AllowedUsersAndGroupsLoadedEvent(users);
        EventBus.getInstance().fireEvent(ev);
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
    if (event.getContent().getType().equals("Component") && event.getContent().getInstanceId().startsWith(Apps.formsOnline.name())) {
      super.showContent(event);
    }
  }

}
