/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.client.common;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.FullUserDTO;

/**
 * @author: svu
 */
public class AuthentificationManager {

  private static AuthentificationManager instance = null;
  private static final String USER_CONNECTED_KEY = "userConnected";
  private static final String DES_KEY = "LagTegshyeecnoc^";

  public static AuthentificationManager getInstance() {
    if (instance == null) {
      instance = new AuthentificationManager();
    }
    return instance;
  }

  public void storeUser(final DetailUserDTO user, String login, String password, String domainId) {
    String encryptedPassword = null;
    try {
      encryptedPassword = encryptPassword(password);
    } catch (InvalidCipherTextException e) {
      EventBus.getInstance().fireEvent(new ErrorEvent(e));
    }
    SpMobil.setUser(user);

    FullUserDTO u = new FullUserDTO(login, encryptedPassword, domainId, user);
    LocalStorageHelper.store(USER_CONNECTED_KEY, FullUserDTO.class, u);
  }

  /**
   * Clean data in local storage.
   */
  public void clearLocalStorage() {
    LocalStorageHelper.clear();
  }

  public FullUserDTO loadUser() {
    FullUserDTO user = LocalStorageHelper.load(USER_CONNECTED_KEY, FullUserDTO.class);
    SpMobil.setUser(user);
    return user;
  }

  /**
   * Decrypt password in local storage.
   * @param passwordEncrysted
   * @return
   */
  public String decryptPassword(String passwordEncrysted) {
    TripleDesCipher cipher = new TripleDesCipher();
    cipher.setKey(DES_KEY.getBytes());
    String plainPassword = null;
    try {
      plainPassword = cipher.decrypt(passwordEncrysted);
    } catch (Exception e) {
      EventBus.getInstance().fireEvent(new ErrorEvent(e));
    }
    return plainPassword;
  }

  private String encryptPassword(String password) throws InvalidCipherTextException {
    TripleDesCipher cipher = new TripleDesCipher();
    cipher.setKey(DES_KEY.getBytes());
    String encryptedPassword = cipher.encrypt(password);
    return encryptedPassword;
  }

  public void clearUserStorage() {
    LocalStorageHelper.remove(USER_CONNECTED_KEY);
  }

  public void authenticateOnSilverpeas(String login, String password, String domainId, Command commandOnSuccess) {
    StringBuilder data = new StringBuilder();



    data.append("Login=" + URL.encode(login) + "&");
    data.append("Password=" + URL.encode(password) + "&");
    data.append("DomainId=" + domainId);
    RequestBuilder
        rb = new RequestBuilder(RequestBuilder.POST, "/silverpeas/AuthenticationServlet");
    rb.setHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
    try {
      rb.sendRequest(data.toString(), new RequestCallback() {
        @Override
        public void onResponseReceived(final Request request, final Response response) {
          ServicesLocator.getServiceNavigation().initSession(SpMobil.getUser(), new AsyncCallback<DetailUserDTO>() {
            @Override
            public void onFailure(final Throwable throwable) {
              Window.alert("error1");
              Notification.activityStop();
            }

            @Override
            public void onSuccess(final DetailUserDTO user) {
              Notification.activityStop();
              commandOnSuccess.execute();
              SpMobil.setUser(user);
            }
          });
        }

        @Override
        public void onError(final Request request, final Throwable t) {
          EventBus.getInstance().fireEvent(new ErrorEvent(t));
        }
      });
    } catch (RequestException e) {
      EventBus.getInstance().fireEvent(new ErrorEvent(e));
    }
  }
}
