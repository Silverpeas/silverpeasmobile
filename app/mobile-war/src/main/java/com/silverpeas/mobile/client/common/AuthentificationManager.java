package com.silverpeas.mobile.client.common;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.shared.dto.FullUserDTO;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

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
    SpMobil.user = user;

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
    data.append("Login=" + login + "&");
    data.append("Password=" + password + "&");
    data.append("DomainId=" + domainId);
    RequestBuilder
        rb = new RequestBuilder(RequestBuilder.POST, "/silverpeas/AuthenticationServlet");
    rb.setHeader("Content-type", "application/x-www-form-urlencoded");
    try {
      rb.sendRequest(data.toString(), new RequestCallback() {
        @Override
        public void onResponseReceived(final Request request, final Response response) {
          ServicesLocator.getServiceNavigation().initSession(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(final Throwable throwable) {
              Window.alert("error1");
              Notification.activityStop();
            }

            @Override
            public void onSuccess(final Boolean init) {
              Notification.activityStop();
              commandOnSuccess.execute();
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
