/*
 * Copyright (C) 2000 - 2026 Silverpeas
 * Licensed under the GNU Affero General Public License, version 3 or later.
 */
package org.silverpeas.mobile.client.pages.connexion;

import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.client.common.network.rest.RestMethod;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.services.rest.ServiceAuthentication;

/**
 * Native mobile enrollment UI. The secret and codes are never stored locally.
 */
public class TwoFactorEnrollmentPage extends PageContent {

  private final String login;
  private final String password;
  private final String domainId;
  private final VerticalPanel panel = new VerticalPanel();
  private final Label status = new Label();
  private final PasswordTextBox code = new PasswordTextBox();
  private final CheckBox trustDevice = new CheckBox("Faire confiance à cet appareil");

  public TwoFactorEnrollmentPage(String login, String password, String domainId) {
    this.login = login;
    this.password = password;
    this.domainId = domainId;
    panel.setSpacing(12);
    panel.setWidth("100%");
    panel.add(new HTML("<h2>Configurer la double authentification</h2>"));
    panel.add(new Label("Scannez le QR code avec votre application d'authentification."));
    initWidget(panel);
    loadEnrollment();
  }

  private void loadEnrollment() {
    ServicesLocator.getRestServiceAuthentication(login, password, domainId)
        .startEnrollment(new RestCallback<String>() {
          @Override
          public void onFailure(RestMethod method, Throwable error) {
            status.setText("Impossible de démarrer l'enrôlement. Reconnectez-vous.");
            panel.add(status);
          }

          @Override
          public void onSuccess(RestMethod method, String secret) {
            Image qr = new Image(ServiceAuthentication.PATH + "/enrollment/qr");
            qr.setPixelSize(256, 256);
            panel.add(qr);
            panel.add(new Label("Clé de configuration manuelle : " + secret));
            code.getElement().setAttribute("inputmode", "numeric");
            code.getElement().setAttribute("autocomplete", "one-time-code");
            code.getElement().setAttribute("placeholder", "Code à 6 chiffres");
            panel.add(code);
            panel.add(trustDevice);
            Button confirm = new Button("Activer la double authentification");
            confirm.addClickHandler(event -> confirmEnrollment());
            panel.add(confirm);
            panel.add(status);
          }
        });
  }

  private void confirmEnrollment() {
    String value = code.getText().trim();
    if (!value.matches("[0-9]{6}")) {
      status.setText("Saisissez le code à 6 chiffres.");
      return;
    }
    status.setText("Vérification du code...");
    ServicesLocator.getRestServiceAuthentication(login, password, domainId)
        .confirmEnrollment(value, trustDevice.getValue(),
            new RestCallback<ServiceAuthentication.EnrollmentResult>() {
              @Override
              public void onFailure(RestMethod method, Throwable error) {
                status.setText("Code incorrect ou session expirée. Réessayez ou reconnectez-vous.");
              }

              @Override
              public void onSuccess(RestMethod method,
                  ServiceAuthentication.EnrollmentResult result) {
                panel.clear();
                panel.add(new HTML("<h2>Codes de récupération</h2>"));
                panel.add(new Label("Conservez ces codes en lieu sûr. Ils ne seront plus affichés."));
                TextArea recovery = new TextArea();
                recovery.setVisibleLines(12);
                recovery.setCharacterWidth(28);
                recovery.setReadOnly(true);
                recovery.setText(String.join("\n", result.recoveryCodes));
                panel.add(recovery);
                Button continueButton = new Button("J'ai enregistré mes codes — Continuer");
                continueButton.addClickHandler(event ->
                    AuthentificationManager.getInstance().completeEnrollment(
                        login, password, domainId, method, result.profile));
                panel.add(continueButton);
              }
            });
  }
}
