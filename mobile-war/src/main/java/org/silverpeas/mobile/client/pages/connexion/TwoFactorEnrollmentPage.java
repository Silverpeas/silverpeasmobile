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
  Anchor copySecret;
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
    status.setText("Chargement de la clé de configuration...");
    ServicesLocator.getRestServiceAuthentication(login, password, domainId)
        .startEnrollment(new RestCallback<ServiceAuthentication.EnrollmentSetup>() {
          @Override
          public void onFailure(RestMethod method, Throwable error) {
            status.setText("Impossible de démarrer l'enrôlement. Reconnectez-vous.");
          }

          @Override
          public void onSuccess(RestMethod method, ServiceAuthentication.EnrollmentSetup setup) {
            secret.setText(setup.secret);
            status.setText("");
          }
        });
  }

  @UiHandler("copySecret")
  void copyEnrollmentSecret(ClickEvent event) {
    if (secret.getText().isEmpty()) {
      return;
    }
    copyToClipboard(secret.getText());
  }

  private void clipboardResult(boolean copied) {
    status.setText(copied ? "Clé copiée dans le presse-papiers." :
        "Copie impossible. Sélectionnez et copiez la clé manuellement.");
  }

  /**
   * Uses the asynchronous Clipboard API when available (secure contexts),
   * with a selection-based fallback for older mobile WebViews.
   */
  private native void copyToClipboard(String value) /*-{
    var page = this;
    function result(ok) {
      page.@org.silverpeas.mobile.client.pages.connexion.TwoFactorEnrollmentPage::clipboardResult(Z)(ok);
    }
    function fallback() {
      var field = $doc.createElement("textarea");
      field.value = value;
      field.setAttribute("readonly", "");
      field.style.position = "fixed";
      field.style.opacity = "0";
      $doc.body.appendChild(field);
      field.focus();
      field.select();
      var ok = false;
      try {
        ok = $doc.execCommand("copy");
      } catch (ignored) {
      }
      $doc.body.removeChild(field);
      result(ok);
    }
    if ($wnd.navigator.clipboard && $wnd.navigator.clipboard.writeText) {
      $wnd.navigator.clipboard.writeText(value).then(function() {
        result(true);
      }, fallback);
    } else {
      fallback();
    }
  }-*/;

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
