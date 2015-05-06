package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.persist.User;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

/**
 * @author: svu
 */
public class AuthentificationManager {

  private static AuthentificationManager instance = null;
  public final static ConfigurationProvider configuration = GWT.create(ConfigurationProvider.class);

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

    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      User u = new User(login, encryptedPassword, domainId, user);
      storage.setItem("userConnected", u.toJson());
    }
  }

  /**
   * Clean data in local storage.
   */
  public void clearLocalStorage() {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      storage.clear();
    }
  }

  public User loadUser() {
    User user = null;
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      String dataItem = storage.getItem("userConnected");
      if (dataItem != null) {
        user = User.getInstance(dataItem);
      }
    }
    return user;
  }

  /**
   * Decrypt password in local storage.
   * @param passwordEncrysted
   * @return
   */
  public String decryptPassword(String passwordEncrysted) {
    TripleDesCipher cipher = new TripleDesCipher();
    cipher.setKey(configuration.getDESKey().getBytes());
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
    cipher.setKey(SpMobil.configuration.getDESKey().getBytes());
    String encryptedPassword = cipher.encrypt(password);
    return encryptedPassword;
  }
}
