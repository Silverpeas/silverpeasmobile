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

package org.silverpeas.mobile.client.apps.formsonline;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.TextCallback;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.formsonline.events.app.*;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormRequestStatusChangedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormSavedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlineLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlineRequestValidatedEvent;
import org.silverpeas.mobile.client.apps.formsonline.pages.FormOnlineEditPage;
import org.silverpeas.mobile.client.apps.formsonline.pages.FormOnlineRequestsPage;
import org.silverpeas.mobile.client.apps.formsonline.pages.FormOnlineViewPage;
import org.silverpeas.mobile.client.apps.formsonline.pages.FormsOnlineAsReceiverPage;
import org.silverpeas.mobile.client.apps.formsonline.pages.FormsOnlinePage;
import org.silverpeas.mobile.client.apps.formsonline.resources.FormsOnlineMessages;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.FormsHelper;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormLayerDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormRequestDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.List;

public class FormsOnlineApp extends App implements FormsOnlineAppEventHandler, NavigationEventHandler {

    private FormsOnlineMessages msg;
    private FormDTO currentForm;

    private static ApplicationMessages msgApp = GWT.create(ApplicationMessages.class);

    public FormsOnlineApp() {
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

    /**
     * Loads all forms published.
     *
     * @param event
     */
    @Override
    public void loadFormsOnline(final FormsOnlineLoadEvent event) {

        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<FormDTO>>() {
            @Override
            public void attempt() {
                super.attempt();
                ServicesLocator.getServiceFormsOnline().getSendablesForms(getApplicationInstance().getId(), this);
            }

            @Override
            public void onSuccess(final Method method, final List<FormDTO> forms) {
                super.onSuccess(method, forms);
                FormsOnlineLoadedEvent event = new FormsOnlineLoadedEvent();
                event.setForms(forms);
                EventBus.getInstance().fireEvent(event);
            }
        };
        action.attempt();

    }

    /**
     * Loads one form for create a new request.
     *
     * @param event
     */
    @Override
    public void loadFormOnline(final FormOnlineLoadEvent event) {

        currentForm = event.getForm();

        FormOnlineEditPage page = new FormOnlineEditPage();
        page.setPageTitle(event.getForm().getTitle());
        page.show();
        String xmlFormName = event.getForm().getXmlFormName();

        ServicesLocator.getServiceFormsOnline().getFormLayer(getApplicationInstance().getId(),
                xmlFormName, "update", new MethodCallback<FormLayerDTO>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {
                        EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
                    }

                    @Override
                    public void onSuccess(Method method, final FormLayerDTO layer) {

                        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<FormFieldDTO>>() {
                            @Override
                            public void attempt() {
                                super.attempt();
                                ServicesLocator.getServiceFormsOnline().getForm(getApplicationInstance().getId(),
                                        event.getForm().getXmlFormName(), this);
                            }

                            @Override
                            public void onSuccess(final Method method, final List<FormFieldDTO> formFieldsDTO) {
                                super.onSuccess(method, formFieldsDTO);
                                FormLoadedEvent event = new FormLoadedEvent();
                                event.setFormFields(formFieldsDTO);
                                event.setLayer(layer);
                                event.setXmlFormName(xmlFormName);
                                EventBus.getInstance().fireEvent(event);
                            }
                        };
                        action.attempt();
                    }
                });
    }

    /**
     * Save new request.
     *
     * @param formSaveEvent
     */
    @Override
    public void saveForm(final FormSaveEvent formSaveEvent) {
        JavaScriptObject formData = FormsHelper.createFormData();
        for (FormFieldDTO f : formSaveEvent.getData()) {
            if (f.getType().equalsIgnoreCase("file")) {
                formData = FormsHelper.populateFormData(formData, f.getName(), f.getObjectValue());
            } else if (FormsHelper.isStoreValueId(f)) {
                formData = FormsHelper.populateFormData(formData, f.getName(), f.getValueId());
            } else {
                formData = FormsHelper.populateFormData(formData, f.getName(), f.getValue());
            }
        }
        if (!NetworkHelper.isOnline()) {
            Notification.activityStop();
            Notification.alert(msgApp.needToBeOnline());
            return;
        }
        saveForm(this, formData, SpMobil.getUserToken(),
                AuthentificationManager.getInstance().getHeader(AuthentificationManager.XSTKN),
                getApplicationInstance().getId(), currentForm.getId());
    }

    private static native void saveForm(FormsOnlineApp app, JavaScriptObject fd, String token, String stkn, String instanceId, String formId) /*-{
        var url = "/silverpeas/services/mobile/formsOnline/" + instanceId + "/saveForm/" + formId;
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url, false);
        xhr.setRequestHeader("X-Silverpeas-Session", token);
        xhr.setRequestHeader("Authorization", "Bearer " + token);
        xhr.setRequestHeader("X-STKN", stkn);

        xhr.onreadystatechange = function () {
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
                super.attempt();
                ServicesLocator.getServiceFormsOnline().getUserField(getApplicationInstance().getId(), currentForm.getXmlFormName(), event.getFieldName(), this);
            }

            @Override
            public void onSuccess(final Method method, final List<BaseDTO> users) {
                super.onSuccess(method, users);
                AllowedUsersAndGroupsLoadedEvent ev = new AllowedUsersAndGroupsLoadedEvent(users, true);
                EventBus.getInstance().fireEvent(ev);
            }
        };
        action.attempt();
    }

    /**
     * Loads all forms where the current user is receiver.
     *
     * @param formsOnlineAsReceiverLoadEvent
     */
    @Override
    public void loadFormsOnlineAsReceiver(
            final FormsOnlineAsReceiverLoadEvent formsOnlineAsReceiverLoadEvent) {

        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<FormDTO>>() {
            @Override
            public void attempt() {
                super.attempt();
                ServicesLocator.getServiceFormsOnline().getReceivablesForms(getApplicationInstance().getId(), this);
            }

            @Override
            public void onSuccess(final Method method, final List<FormDTO> forms) {
                super.onSuccess(method, forms);
                FormsOnlineAsReceiverPage page = new FormsOnlineAsReceiverPage();
                page.setPageTitle(getApplicationInstance().getLabel());
                page.setData(forms);
                page.show();
            }
        };
        action.attempt();
    }

    /**
     * Loads all requests of selected form where the current user is receiver.
     *
     * @param event
     */
    @Override
    public void loadFormOnlineAsReceiver(final FormOnlineAsReceiverLoadEvent event) {
        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<FormRequestDTO>>() {
            @Override
            public void attempt() {
                super.attempt();
                ServicesLocator.getServiceFormsOnline().getRequests(getApplicationInstance().getId(), event.getForm().getId(), this);
            }

            @Override
            public void onSuccess(final Method method, final List<FormRequestDTO> requests) {
                super.onSuccess(method, requests);
                FormOnlineRequestsPage page = new FormOnlineRequestsPage();
                page.setTitle(getApplicationInstance().getLabel());
                page.setData(requests, false);
                page.show();
            }
        };
        action.attempt();
    }

    @Override
    public void validationRequest(final FormsOnlineValidationRequestEvent event) {

        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
            @Override
            public void attempt() {
                super.attempt();
                ServicesLocator.getServiceFormsOnline().processRequest(getApplicationInstance().getId(), event.getData().getId(), event.getValidation(), this);
            }

            @Override
            public void onSuccess(final Method method, final Void aVoid) {
                super.onSuccess(method, aVoid);
                EventBus.getInstance().fireEvent(new FormsOnlineRequestValidatedEvent(event.getData()));
            }
        };
        action.attempt();
    }

    /**
     * Load all requests created by the current user.
     *
     * @param formOnlineMyRequestLoadEvent
     */
    @Override
    public void loadMyRequests(final FormOnlineMyRequestLoadEvent formOnlineMyRequestLoadEvent) {

        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<FormRequestDTO>>() {
            @Override
            public void attempt() {
                super.attempt();
                ServicesLocator.getServiceFormsOnline().getMyRequests(getApplicationInstance().getId(), this);
            }

            @Override
            public void onSuccess(final Method method, final List<FormRequestDTO> requests) {
                super.onSuccess(method, requests);
                FormOnlineRequestsPage page = new FormOnlineRequestsPage();
                page.setData(requests, true);
                page.show();
            }
        };
        action.attempt();

    }

    /**
     * Loads a form request.
     *
     * @param loadEvent
     */
    @Override
    public void loadFormRequest(
            final FormsOnlineLoadRequestEvent loadEvent) {

        ServicesLocator.getServiceFormsOnline().getFormLayer(getApplicationInstance().getId(), loadEvent.getData().getFormName(), "view", new MethodCallback<FormLayerDTO>() {

            @Override
            public void onFailure(Method method, Throwable throwable) {
                EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
            }


            @Override
            public void onSuccess(Method method, final FormLayerDTO layer) {

                MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<FormRequestDTO>() {
                    @Override
                    public void attempt() {
                        super.attempt();
                        ServicesLocator.getServiceFormsOnline().loadRequest(getApplicationInstance().getId(), loadEvent.getData().getId(), this);
                    }

                    @Override
                    public void onSuccess(final Method method, final FormRequestDTO data) {
                        super.onSuccess(method, data);
                        data.setHtmlLayer(layer);
                        EventBus.getInstance().fireEvent(new FormRequestStatusChangedEvent(data));
                        FormOnlineViewPage page = new FormOnlineViewPage();
                        page.setApp(FormsOnlineApp.this);
                        page.setData(data);
                        page.setPageTitle(data.getTitle() + " " + data.getCreationDate() + " " + data.getCreator());
                        page.show();
                    }
                };
                action.attempt();
            }
        });
    }

    @Override
    public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
        if (event.getInstance().getType().equals(Apps.formsOnline.name())) {
            this.setApplicationInstance(event.getInstance());

            FormsOnlinePage page = new FormsOnlinePage();
            page.setApp(this);
            page.setPageTitle(event.getInstance().getLabel());
            page.show();
        }
    }

    @Override
    public void showContent(final NavigationShowContentEvent event) {
        if (event.getContent().getType().equals("Form")) {
            ApplicationInstanceDTO appInst = new ApplicationInstanceDTO();
            appInst.setId(event.getContent().getInstanceId());
            this.setApplicationInstance(appInst);

            String id = event.getContent().getId();
            FormOnlineLoadEvent ev = new FormOnlineLoadEvent();
            FormDTO form = new FormDTO();
            form.setXmlFormName(id);
            ev.setForm(form);
            ev.getForm().setId(id);
            loadFormOnline(ev);
        }

        if (event.getContent().getType().equals("Component") && event.getContent().getInstanceId().startsWith(Apps.formsOnline.name())) {
            ApplicationInstanceDTO appInst = new ApplicationInstanceDTO();
            appInst.setId(event.getContent().getInstanceId());
            this.setApplicationInstance(appInst);
            FormRequestDTO data = new FormRequestDTO();
            data.setId(event.getContent().getId());

            FormsOnlineLoadRequestEvent ev = new FormsOnlineLoadRequestEvent(data, false);
            EventBus.getInstance().fireEvent(ev);
        }
    }
}
