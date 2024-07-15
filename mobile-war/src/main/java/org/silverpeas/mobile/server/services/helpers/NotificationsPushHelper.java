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

package org.silverpeas.mobile.server.services.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.silverpeas.kernel.SilverpeasRuntimeException;
import org.silverpeas.core.notification.user.server.channel.silvermail.SILVERMAILMessage;
import org.silverpeas.core.notification.user.server.channel.silvermail.SILVERMAILPersistence;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.mobile.server.dao.token.TokenDAO;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.silverpeas.kernel.util.StringUtil.EMPTY;

/**
 * @author svu
 */
@Singleton
@Named("notificationsPushHelper")
public class NotificationsPushHelper {

  private final ObjectMapper mapper = new ObjectMapper();

  @Inject
  private TokenDAO tokenDAO;

  private FileInputStream serviceAccount;
  private boolean configPresent;

  public static NotificationsPushHelper getInstance() {
    return ServiceProvider.getService(NotificationsPushHelper.class);
  }

  protected TokenDAO getTokenDAO() {
    return tokenDAO;
  }

  private SettingBundle getSettings() {
    return ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
  }

  private void removeToken(String userId, String token) {
    if (token != null) getTokenDAO().removeToken(userId, token);
  }

  public void storeToken(String userId, String token) {
    if (token != null) getTokenDAO().saveToken(userId, token);
  }

  public void sendNotification(String userId, Map<String, Object> notifData) {
    try {
      init();
      if (configPresent) {
        getTokenDAO().getTokens(userId)
            .stream()
            .filter(Objects::nonNull)
            .forEach(t -> sendToToken(userId, t, notifData));
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
  }

  @SuppressWarnings("unchecked")
  private void init() throws IOException {
    if (serviceAccount == null) {
      String configFile = getSettings().getString("push.notification.serviceAccount", "");
      if (StringUtil.isDefined(configFile)) {
        serviceAccount = new FileInputStream(configFile);
        Map<String, Object> data = mapper.readValue(serviceAccount, Map.class);
        String projectId = (String) data.get("project_id");
        serviceAccount = new FileInputStream(configFile);
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://" + projectId + ".firebaseio.com")
            .build();
        FirebaseApp.initializeApp(options);
        configPresent = true;
      } else {
        configPresent = false;
      }
    }
  }

  public String sendToToken(final String userId, String token, Map<String, Object> notifData) {
    String response = EMPTY;
    try {
      // [START send_to_token]
      // This registration token comes from the client FCM SDKs.

      // See documentation on defining a message payload.
      response = getNotificationPermalink(userId, String.valueOf(notifData.get("id")))
          // No push if no permalink
          .map(l -> Message.builder()
              .putData("subject", String.valueOf(notifData.get("subject")))
              .putData("sender", String.valueOf(notifData.get("sender")))
              .putData("body", String.valueOf(notifData.get("body")))
              .putData("notificationId", String.valueOf(notifData.get("notificationId")))
              .putData("permalink", l))
          .map(m -> m.setToken(token))
          .map(Message.Builder::build)
          .map(m -> {
            try {
              // Send a message to the device corresponding to the provided
              // registration token.
              return FirebaseMessaging.getInstance().send(m);
            } catch (FirebaseMessagingException e) {
              throw new SilverpeasRuntimeException(e);
            }
          })
          // empty string when no message sent
          .orElse(EMPTY);
    } catch (Exception fe) {
      removeToken(userId, token);
      SilverLogger.getLogger(this).error(fe);
    }
    return response;
  }

  private Optional<String> getNotificationPermalink(String userId, String id) {
    SILVERMAILMessage m = SILVERMAILPersistence.getMessage(userId, Long.valueOf(id));
    return ofNullable(m.getUrl());
  }
}
