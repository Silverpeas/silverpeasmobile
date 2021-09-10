/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.server.services.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.silverpeas.core.notification.user.server.channel.silvermail.SILVERMAILMessage;
import org.silverpeas.core.notification.user.server.channel.silvermail.SILVERMAILPersistence;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.mobile.server.dao.token.TokenDAO;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: svu
 */
@Singleton
@Named("notificationsPushHelper")
public class NotificationsPushHelper {

  @Inject
  private TokenDAO tokenDAO;

  private static FileInputStream serviceAccount;
  private static boolean configPresent;

  private static ObjectMapper mapper = new ObjectMapper();

  public static NotificationsPushHelper getInstance() {
    return ServiceProvider.getSingleton(NotificationsPushHelper.class);
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

  public void sendNotification(String userId, Map<String, Object> dataNotifcation) {
    try {
      init();
      if (configPresent) {
        List<String> tokens = getTokenDAO().getTokens(userId);
        for (String token : tokens) {
          if (token != null) {
            try {
              sendToToken(token, dataNotifcation);
            } catch(FirebaseMessagingException fe) {
              removeToken(userId, token);
              SilverLogger.getLogger(this).error(fe);
            }
          }
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
  }

  private void init() throws IOException {
    if (serviceAccount == null) {
      String configFile = getSettings().getString("push.notification.serviceAccount", "");
      if (StringUtil.isDefined(configFile)) {
        serviceAccount = new FileInputStream(configFile);
        Map<String, Object> data = mapper.readValue(serviceAccount, Map.class);
        String projectId = (String) data.get("project_id");
        serviceAccount = new FileInputStream(configFile);
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://" + projectId + ".firebaseio.com").build();
        FirebaseApp.initializeApp(options);
        configPresent = true;
      } else {
        configPresent = false;
      }
    }
  }

  public String sendToToken(String registrationToken, Map<String, Object> dataNotifcation) throws FirebaseMessagingException {
    // [START send_to_token]
    // This registration token comes from the client FCM SDKs.

    // See documentation on defining a message payload.
    Message message = Message.builder()
        .putData("subject", String.valueOf(dataNotifcation.get("subject")))
        .putData("sender", String.valueOf(dataNotifcation.get("sender")))
        .putData("permalink", getNotificationPermalink(String.valueOf(dataNotifcation.get("id"))))
        .setToken(registrationToken)
        .build();

    // Send a message to the device corresponding to the provided
    // registration token.
    String response = FirebaseMessaging.getInstance().send(message);

    return response;
  }

  private String getNotificationPermalink(String id) {
    SILVERMAILMessage notif = SILVERMAILPersistence.getMessage(Long.decode(id));
    return notif.getUrl();
  }
}
