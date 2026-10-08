/*
 * Copyright (C) 2000 - 2026 Silverpeas
 * Licensed under the GNU Affero General Public License, version 3 or later.
 */
package org.silverpeas.mobile.client.pages.connexion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.client.common.network.rest.RestMethod;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.services.rest.ServiceAuthentication;

/**
 * TOTP enrollment with the same layout as the mobile login and verification pages.
 */
public class TwoFactorEnrollmentPage extends PageContent {

  interface TwoFactorEnrollmentPageUiBinder extends UiBinder<Widget, TwoFactorEnrollmentPage> {}

  private static final TwoFactorEnrollmentPageUiBinder uiBinder =
      GWT.create(TwoFactorEnrollmentPageUiBinder.class);

  @UiField(provided = true)
  protected ApplicationMessages msg;

  @UiField
  DivElement setupSection;
  @UiField
  DivElement recoverySection;
  @UiField
  DivElement version;
  @UiField
  Image qrCode;
  @UiField
  Label secret;
  @UiField
  PasswordTextBox codeField;
  @UiField
  CheckBox trustDevice;
  @UiField
  TextArea recoveryCodes;
  @UiField
  Label status;
  @UiField
  Anchor confirm;
  @UiField
  Anchor continueButton;

  private final String login;
  private final String password;
  private final String domainId;
  private RestMethod enrollmentAuthenticationMethod;
  private ServiceAuthentication.EnrollmentResult enrollmentResult;

  public TwoFactorEnrollmentPage(String login, String password, String domainId) {
    this.login = login;
    this.password = password;
    this.domainId = domainId;
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));

    trustDevice.setVisible(Boolean.parseBoolean(
        ResourcesManager.getParam("twoFactorTrustedDeviceEnabled")));
    codeField.getElement().setAttribute("inputmode", "numeric");
    codeField.getElement().setAttribute("pattern", "[0-9]*");
    codeField.getElement().setAttribute("autocomplete", "one-time-code");
    codeField.getElement().setAttribute("autocapitalize", "none");
    codeField.getElement().setAttribute("autocorrect", "off");
    codeField.getElement().setAttribute("spellcheck", "off");
    codeField.getElement().setAttribute("placeholder", msg.codeLabel().asString());

    version.setId("version");
    version.setInnerText(msg.version() + " " + ResourcesManager.getVersion());
    Scheduler.get().scheduleDeferred(() ->
        DOM.getElementById("page-login-title")
            .setInnerHTML(ResourcesManager.getLabel("login.title")));
    loadEnrollment();
  }

  private void loadEnrollment() {
    status.setText("Chargement du QR code...");
    ServicesLocator.getRestServiceAuthentication(login, password, domainId)
        .startEnrollment(new RestCallback<ServiceAuthentication.EnrollmentSetup>() {
          @Override
          public void onFailure(RestMethod method, Throwable error) {
            status.setText("Impossible de démarrer l'enrôlement. Reconnectez-vous.");
          }

          @Override
          public void onSuccess(RestMethod method, ServiceAuthentication.EnrollmentSetup setup) {
            qrCode.setUrl("data:image/png;base64," + setup.qrCode);
            qrCode.setPixelSize(256, 256);
            secret.setText(setup.secret);
            status.setText("");
          }
        });
  }

  @UiHandler("confirm")
  void confirmEnrollment(ClickEvent event) {
    String value = codeField.getText().trim();
    if (!value.matches("[0-9]{6}")) {
      codeField.getElement().getStyle().setBackgroundColor("#ec9c01");
      status.setText("Saisissez le code à 6 chiffres.");
      return;
    }
    codeField.getElement().getStyle().clearBackgroundColor();
    confirm.setVisible(false);
    status.setText("Vérification du code...");
    ServicesLocator.getRestServiceAuthentication(login, password, domainId)
        .confirmEnrollment(value, trustDevice.getValue(),
            new RestCallback<ServiceAuthentication.EnrollmentResult>() {
              @Override
              public void onFailure(RestMethod method, Throwable error) {
                confirm.setVisible(true);
                status.setText("Code incorrect ou session expirée. Réessayez ou reconnectez-vous.");
              }

              @Override
              public void onSuccess(RestMethod method,
                  ServiceAuthentication.EnrollmentResult result) {
                enrollmentAuthenticationMethod = method;
                enrollmentResult = result;
                recoveryCodes.setText(String.join("\n", result.recoveryCodes));
                setupSection.getStyle().setProperty("display", "none");
                recoverySection.getStyle().clearDisplay();
                status.setText("");
              }
            });
  }

  @UiHandler("continueButton")
  void continueAfterRecoveryCodes(ClickEvent event) {
    if (enrollmentResult != null) {
      AuthentificationManager.getInstance().completeEnrollment(
          login, password, domainId, enrollmentAuthenticationMethod, enrollmentResult.profile);
    }
  }
}
