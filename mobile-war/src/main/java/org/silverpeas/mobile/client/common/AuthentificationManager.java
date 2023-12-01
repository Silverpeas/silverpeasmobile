/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.event.authentication.AuthenticationErrorEvent;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.FullUserDTO;
import org.silverpeas.mobile.shared.dto.IFullUser;
import org.silverpeas.mobile.shared.dto.authentication.IUserProfile;
import org.silverpeas.mobile.shared.dto.authentication.UserProfileDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: svu
 */
public class AuthentificationManager {

  private static AuthentificationManager instance = null;
  private static final String USER_CONNECTED_KEY = "userConnected";
  public static final String USER_PROFIL = "userProfil";
  private static final String DES_KEY = "LagTegshyeecnoc^";

  public static final String XSTKN = "X-STKN";
  public static final String XSilverpeasSession = "X-Silverpeas-Session";

  public static AuthentificationManager getInstance() {
    if (instance == null) {
      instance = new AuthentificationManager();
    }
    return instance;
  }

  public void addHeader(String key, String value) {
    LocalStorageHelper.store(key, value);
  }

  public String getHeader(String key) {
    return LocalStorageHelper.load(key);
  }

  public void storeUser(final DetailUserDTO user, final UserProfileDTO profil, String login,
      String password, String domainId) {
    String encryptedPassword = null;
    try {
      encryptedPassword = encryptPassword(password);
    } catch (InvalidCipherTextException e) {
      EventBus.getInstance().fireEvent(new ErrorEvent(e));
    }
    SpMobil.setUser(user);

    FullUserDTO u = new FullUserDTO(login, encryptedPassword, domainId, user);

    String maintainSession = ResourcesManager.getParam("maintain.session");
    if (maintainSession.equalsIgnoreCase("true")) {
      LocalStorageHelper.store(USER_CONNECTED_KEY, u.getAutoBean());
      LocalStorageHelper.store(USER_PROFIL, profil.getAutoBean());
    }
  }

  public void updateAvatarInCache(final String avatarData) {
    SpMobil.getUser().setAvatar(avatarData);
    SpMobil.getUserProfile().setAvatar(avatarData);
    FullUserDTO user = FullUserDTO.getBean(LocalStorageHelper.load(USER_CONNECTED_KEY, IFullUser.class));
    user.setAvatar(avatarData);
    String maintainSession = ResourcesManager.getParam("maintain.session");
    if (maintainSession.equalsIgnoreCase("true")) {
      LocalStorageHelper.store(USER_CONNECTED_KEY, user.getAutoBean());
      LocalStorageHelper.store(USER_PROFIL, SpMobil.getUserProfile().getAutoBean());
    }
  }

  /**
   * Clean data in local storage.
   */
  public void clearLocalStorage() {
    LocalStorageHelper.clear();
  }

  public FullUserDTO loadUser() {
    FullUserDTO user = FullUserDTO.getBean(LocalStorageHelper.load(USER_CONNECTED_KEY, IFullUser.class));
    SpMobil.setUser(user);
    UserProfileDTO profil =
        UserProfileDTO.getBean(LocalStorageHelper.load(USER_PROFIL, IUserProfile.class));
    SpMobil.setUserProfile(profil);
    return user;
  }

  /**
   * Decrypt password in local storage.
   *
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

  public void authenticateOnSilverpeas(String login, String password, String domainId,
      Command attempt) {
    Notification.activityStart();
    if (NetworkHelper.isOnline()) {
      ServicesLocator.getRestServiceAuthentication(login, password, domainId).authentication(

          new MethodCallback<UserProfileDTO>() {
            @Override
            public void onFailure(final Method method, final Throwable throwable) {
              int statusError = method.getResponse().getStatusCode();
              if (statusError == 403) {
                Notification.activityStop();
                SpMobil.displayMainPage();
              } else if (statusError == 401 || statusError == 500) {

                MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Boolean>() {
                  @Override
                  public void attempt() {
                    super.attempt();
                    ServicesLocator.getServiceConnection().userExist(login, domainId, this);
                  }

                  @Override
                  public void onFailure(final Method method, final Throwable t) {
                    super.onFailure(method, t);
                    EventBus.getInstance().fireEvent(new AuthenticationErrorEvent(t));
                  }

                  @Override
                  public void onSuccess(final Method method, final Boolean exist) {
                    super.onSuccess(method, exist);
                    if (exist) {
                      AuthenticationException ex = new AuthenticationException(
                          AuthenticationException.AuthenticationError.PwdNotAvailable);
                      ex.setLogin(login);
                      EventBus.getInstance().fireEvent(new AuthenticationErrorEvent(ex));
                    } else {
                      AuthenticationException ex = new AuthenticationException(
                          AuthenticationException.AuthenticationError.LoginNotAvailable);
                      ex.setLogin(login);
                      EventBus.getInstance().fireEvent(new AuthenticationErrorEvent(ex));
                    }
                  }
                };
                action.attempt();
              } else {
                EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
              }
            }

            @Override
            public void onSuccess(final Method method, final UserProfileDTO userProfile) {

              AuthentificationManager.getInstance().addHeader("X-STKN", method.getResponse().getHeader("X-STKN"));
              AuthentificationManager.getInstance().addHeader("X-Silverpeas-Session",
                  method.getResponse().getHeader("X-Silverpeas-Session"));

              SpMobil.setUserProfile(userProfile);


              MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<DetailUserDTO>() {
                @Override
                public void attempt() {
                  super.attempt();
                  List<String> ids = new ArrayList<>();
                  ids.add(login);
                  ids.add(password);
                  ids.add(domainId);

                  ServicesLocator.getServiceConnection().login(ids, this);
                }

                @Override
                public void onSuccess(final Method method, final DetailUserDTO user) {
                  super.onSuccess(method, user);
                  SpMobil.setUser(user);

                  ServicesLocator.getServiceTermsOfService().show(new MethodCallback<Boolean>() {
                    @Override
                    public void onFailure(final Method method, final Throwable throwable) {
                      if (throwable instanceof AuthenticationException) {
                        EventBus.getInstance().fireEvent(new AuthenticationErrorEvent(throwable));
                      } else {
                        EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
                      }
                    }

                    @Override
                    public void onSuccess(final Method method, final Boolean showTermsOfServices) {

                      Notification.activityStop();
                      AuthentificationManager.getInstance().storeUser(user, userProfile, login,
                          password, domainId);
                      if (showTermsOfServices) {
                        SpMobil.displayTermsOfServicePage();
                      } else {
                        if (attempt == null) {
                          SpMobil.displayMainPage();
                        } else {
                          attempt.execute();
                        }
                      }
                    }
                  });
                }

                @Override
                public void onFailure(final Method method, final Throwable t) {
                  //super.onFailure(method, t);
                  GWT.log("Normaly never happen !!! " + t.getClass().getName() + " " + t.getMessage());
                  if (t instanceof AuthenticationException) {
                    EventBus.getInstance().fireEvent(new AuthenticationErrorEvent(t));
                  } else {
                    EventBus.getInstance().fireEvent(new ErrorEvent(t));
                  }
                }
              };
              action.attempt();
            }
          });
    } else {
      //TODO : terms of service in offline mode ?
      Notification.activityStop();
      loadUser();
      SpMobil.displayMainPage();
    }
  }

  public void injectAuthenticationHttpHeaders(RequestBuilder builder) {
    if (getHeader(AuthentificationManager.XSTKN) != null) {
      builder.setHeader(AuthentificationManager.XSTKN, getHeader(AuthentificationManager.XSTKN));
    }
    if (getHeader(AuthentificationManager.XSilverpeasSession) != null) {
      builder.setHeader(AuthentificationManager.XSilverpeasSession,
          getHeader(AuthentificationManager.XSilverpeasSession));
    }
  }

  public void logout() {
    // logout
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "/silverpeas/Logout");
    try {
      clearCache();
      builder.sendRequest("", new RequestCallback() {
        @Override
        public void onResponseReceived(final Request request, final Response response) {
          Notification.activityStop();
          AuthentificationManager.getInstance().clearLocalStorage();
          PageHistory.getInstance().clear();
          Notification.activityStop();
          ConnexionPage connexionPage = new ConnexionPage();
          RootPanel.get().clear();
          RootPanel.get().add(connexionPage);
          SpMobil.destroyMainPage();

        }

        @Override
        public void onError(final Request request, final Throwable throwable) {

        }
      });
    } catch (RequestException e) {
      Window.alert(e.getMessage());
    }
  }

  private void clearCache() {
    // clear app cache
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceNavigation().clearAppCache(this);
      }

      @Override
      public void onFailure(final Method method, final Throwable t) {
        super.onFailure(method, t);
      }

      @Override
      public void onSuccess(final Method method, final Void unused) {
        super.onSuccess(method, unused);
      }
    };
    action.attempt();


  }
}
