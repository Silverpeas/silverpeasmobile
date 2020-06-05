/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.event.authentication.AuthenticationErrorEvent;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.FullUserDTO;
import org.silverpeas.mobile.shared.dto.authentication.UserProfileDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;

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
    LocalStorageHelper.store(key, String.class, value);
  }

  public String getHeader(String key) {
    return LocalStorageHelper.load(key, String.class);
  }

  public void storeUser(final DetailUserDTO user, final UserProfileDTO profil, String login, String password, String domainId) {
    String encryptedPassword = null;
    try {
      encryptedPassword = encryptPassword(password);
    } catch (InvalidCipherTextException e) {
      EventBus.getInstance().fireEvent(new ErrorEvent(e));
    }
    SpMobil.setUser(user);

    FullUserDTO u = new FullUserDTO(login, encryptedPassword, domainId, user);
    LocalStorageHelper.store(USER_CONNECTED_KEY, FullUserDTO.class, u);
    LocalStorageHelper.store(USER_PROFIL, UserProfileDTO.class, profil);
  }

  public void updateAvatarInCache(final String avatarData) {
    SpMobil.getUser().setAvatar(avatarData);
    SpMobil.getUserProfile().setAvatar(avatarData);
    FullUserDTO user = LocalStorageHelper.load(USER_CONNECTED_KEY, FullUserDTO.class);
    user.setAvatar(avatarData);
    LocalStorageHelper.store(USER_CONNECTED_KEY, FullUserDTO.class, user);
    LocalStorageHelper.store(USER_PROFIL, UserProfileDTO.class, SpMobil.getUserProfile());
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
    UserProfileDTO profil = LocalStorageHelper.load(USER_PROFIL, UserProfileDTO.class);
    SpMobil.setUserProfile(profil);
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

  public void authenticateOnSilverpeas(String login, String password, String domainId, Command attempt) {
    Notification.activityStart();
    ServicesLocator.getRestServiceAuthentication(login, password, domainId).authentication(

        new MethodCallback<UserProfileDTO>() {
          @Override
          public void onFailure(final Method method, final Throwable throwable) {
            int statusError = method.getResponse().getStatusCode();
            if (statusError == 403) {
              Notification.activityStop();
              SpMobil.displayMainPage();
            } else if (statusError == 401 || statusError == 500) {

              ServicesLocator.getServiceConnection().userExist(login, domainId, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(final Throwable throwable) {
                  EventBus.getInstance().fireEvent(new AuthenticationErrorEvent(throwable));
                }

                @Override
                public void onSuccess(final Boolean exist) {
                  if (exist) {
                    AuthenticationException ex = new AuthenticationException(AuthenticationException.AuthenticationError.PwdNotAvailable);
                    ex.setLogin(login);
                    EventBus.getInstance().fireEvent(new AuthenticationErrorEvent(ex));
                  } else {
                    AuthenticationException ex = new AuthenticationException(AuthenticationException.AuthenticationError.LoginNotAvailable);
                    ex.setLogin(login);
                    EventBus.getInstance().fireEvent(new AuthenticationErrorEvent(ex));
                  }
                }
              });
            } else if (statusError == 0) {
              loadUser();
              SpMobil.displayMainPage();
              //TODO : display termsofservice
            } else {
              EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
            }
          }

          @Override
          public void onSuccess(final Method method, final UserProfileDTO userProfile) {
            LocalStorageHelper.clear(); // clear offline data
            AuthentificationManager.getInstance().clearLocalStorage();

            AuthentificationManager.getInstance().addHeader("X-STKN", method.getResponse().getHeader("X-STKN"));
            AuthentificationManager.getInstance().addHeader("X-Silverpeas-Session", method.getResponse().getHeader("X-Silverpeas-Session"));

            SpMobil.setUserProfile(userProfile);
            ServicesLocator.getServiceConnection().login(login, password, domainId, new AsyncCallback<DetailUserDTO>() {

              @Override
              public void onFailure(final Throwable throwable) {
                if (throwable instanceof AuthenticationException) {
                  EventBus.getInstance().fireEvent(new AuthenticationErrorEvent(throwable));
                } else {
                  EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
                }
              }

              @Override
              public void onSuccess(final DetailUserDTO user) {
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
                    AuthentificationManager.getInstance().storeUser(user, userProfile, login, password,
                        domainId);
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
            });
          }
        });
  }

  public void injectAuthenticationHttpHeaders(RequestBuilder builder) {
    if (getHeader(AuthentificationManager.XSTKN) != null) {
      builder.setHeader(AuthentificationManager.XSTKN, getHeader(AuthentificationManager.XSTKN));
    }
    if (getHeader(AuthentificationManager.XSilverpeasSession) != null) {
      builder.setHeader(AuthentificationManager.XSilverpeasSession, getHeader(AuthentificationManager.XSilverpeasSession));
    }
  }

  public void logout() {
    ServicesLocator.getServiceNavigation().logout(new AsyncCallback<Void>() {
      @Override
      public void onFailure(final Throwable throwable) {
        Notification.activityStop();
      }
      @Override
      public void onSuccess(final Void aVoid) {
        AuthentificationManager.getInstance().clearLocalStorage();
        PageHistory.getInstance().clear();
        Notification.activityStop();
        ConnexionPage connexionPage = new ConnexionPage();
        RootPanel.get().clear();
        RootPanel.get().add(connexionPage);
        SpMobil.destroyMainPage();
      }
    });
  }

}
