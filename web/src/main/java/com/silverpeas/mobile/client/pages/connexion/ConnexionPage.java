/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.client.pages.connexion;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.DropDownItem;
import com.gwtmobile.ui.client.widgets.DropDownList;
import com.gwtmobile.ui.client.widgets.PasswordTextBox;
import com.gwtmobile.ui.client.widgets.TextBox;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.pages.main.MainPage;
import com.silverpeas.mobile.client.persist.UserIds;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.client.resources.ApplicationResources;
import com.silverpeas.mobile.shared.dto.DomainDTO;

public class ConnexionPage extends Page {

  private static ConnexionPageUiBinder uiBinder = GWT.create(ConnexionPageUiBinder.class);
  private MainPage mainPage;
  @UiField(provided = true)
  protected ApplicationMessages msg = null;
  @UiField(provided = true)
  protected ApplicationResources res = null;
  @UiField
  Button go;
  @UiField
  TextBox loginField;
  @UiField
  PasswordTextBox passwordField;
  @UiField
  DropDownList domains;

  interface ConnexionPageUiBinder extends UiBinder<Widget, ConnexionPage> {
  }

  public ConnexionPage() {
    res = GWT.create(ApplicationResources.class);
    res.css().ensureInjected();
    msg = GWT.create(ApplicationMessages.class);
    loadDomains();
    initWidget(uiBinder.createAndBindUi(this));
  }

  /**
   * Gestion du clique sur le bouton go.
   * @param e
   */
  @UiHandler("go")
  void connexion(ClickEvent e) {
    String login = loginField.getText();
    String password = passwordField.getText();
    login(login, password, domains.getSelectedValue());
  }

  /**
   * Transition de la demande de connexion au serveur.
   * @param login
   * @param password
   * @param domainId
   */
  private void login(String login, final String password, String domainId) {
    ServicesLocator.serviceConnection.login(login, password, domainId, new AsyncCallback<Void>() {
        public void onFailure(Throwable caught) {
        EventBus.getInstance().fireEvent(new ErrorEvent(caught));
        }

        public void onSuccess(Void result) {
        String encryptedPassword = null;
        try {
          encryptedPassword = encryptPassword(password);
        } catch (InvalidCipherTextException e) {
          EventBus.getInstance().fireEvent(new ErrorEvent(e));
        }
        storeIds(encryptedPassword);
        mainPage = new MainPage();
        goTo(mainPage);
        }
            });
  }

  private String encryptPassword(String password) throws InvalidCipherTextException {
    TripleDesCipher cipher = new TripleDesCipher();
    cipher.setKey(SpMobil.configuration.getDESKey().getBytes());
    String encryptedPassword = cipher.encrypt(passwordField.getText());
    return encryptedPassword;
  }

  /**
   * Mémorisation des identifiants de l'utilisateur.
   */
  private void storeIds(final String encryptedPassword) {
    Database.open();
    final Entity<UserIds> userIdsEntity = GWT.create(UserIds.class);
    Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {
      public void onSuccess() {
        userIdsEntity.all().destroyAll(new com.gwtmobile.persistence.client.Callback() {
            public void onSuccess() {
            final UserIds userIds = userIdsEntity.newInstance();
            userIds.setLogin(loginField.getText());
            userIds.setPassword(encryptedPassword);
            userIds.setDomainId(domains.getSelectedValue());
            Persistence.flush();
            }
                    });
      }
    });
  }

  /**
   * Récupération de la liste des domaines.
   */
  private void loadDomains() {
    ServicesLocator.serviceConnection.getDomains(new AsyncCallback<List<DomainDTO>>() {
      public void onFailure(Throwable caught) {
        EventBus.getInstance().fireEvent(new ErrorEvent(caught));
      }

      public void onSuccess(List<DomainDTO> result) {
        Iterator<DomainDTO> iDomains = result.iterator();
        while (iDomains.hasNext()) {
          DomainDTO domain = iDomains.next();
          DropDownItem item = new DropDownItem();
          item.setValue(domain.getId());
          item.setText(domain.getName());
          domains.add(item);
        }
      }
    });
  }
}
