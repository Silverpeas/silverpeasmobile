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

package org.silverpeas.mobile.client.pages.connexion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;


public class TwoFactorPage extends PageContent {

  private static TwoFactorPageUiBinder uiBinder = GWT.create(TwoFactorPageUiBinder.class);

  private String login, password, domainId;

  @UiField(provided = true)
  protected ApplicationMessages msg = null;
  @UiField
  Anchor sendCode, go;

  @UiField
  PasswordTextBox codeField;

  @UiField
  FormPanel form;

  @UiField
  DivElement version;

  public void setIds(String login, String password, String domainId) {
    this.login = login;
    this.password = password;
    this.domainId = domainId;
    requestCode(null);
  }

  interface TwoFactorPageUiBinder extends UiBinder<Widget, TwoFactorPage> {}

  public TwoFactorPage() {
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));

    codeField.getElement().setId("Password");
    codeField.getElement().setAttribute("autocapitalize", "none");
    codeField.getElement().setAttribute("autocorrect", "off");
    codeField.getElement().setAttribute("spellcheck", "off");
    codeField.getElement().setAttribute("autocomplete", "off");
    codeField.getElement().setAttribute("placeholder", msg.codeLabel().asString());
    form.getElement().setId("formLogin");
    form.getElement().setAttribute("autocomplete","off");

    version.setId("version");
    version.setInnerText(msg.version() + " " + ResourcesManager.getVersion());

    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        DOM.getElementById("page-login-title")
            .setInnerHTML(ResourcesManager.getLabel("login.title"));
      }
    });
  }
  @UiHandler("codeField")
  void codeChange(ChangeEvent event) {
    codeField.getElement().getStyle().clearBackgroundColor();
  }

  @UiHandler("sendCode")
  void requestCode(ClickEvent event) {
    codeField.setText("");
    codeField.getElement().getStyle().clearBackgroundColor();
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceConnection().generateSecurityCode(login, domainId, this);
      }
    };
    action.attempt();
  }

  /**
   * Gestion du clique sur le bouton go.
   */
  @UiHandler("go")
  void connexion(ClickEvent e) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Boolean>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceConnection().checkSecurityCode(login, domainId, codeField.getText(), this);
      }

      @Override
      public void onSuccess(Method method, Boolean valid) {
        super.onSuccess(method, valid);
        if (valid) {
          AuthentificationManager.getInstance()
                  .authenticateOnSilverpeas(login, password, domainId, null);
        } else {
          codeField.getElement().getStyle().setBackgroundColor("#ec9c01");
        }
      }
    };
    action.attempt();
  }
}
