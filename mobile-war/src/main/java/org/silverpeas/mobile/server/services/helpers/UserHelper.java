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

import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.user.model.Group;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.notification.NotificationException;
import org.silverpeas.core.notification.user.client.NotificationManager;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;
import org.silverpeas.kernel.SilverpeasRuntimeException;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author svu
 */
public class UserHelper {

  private static UserHelper instance;

  public static UserHelper getInstance() {
    if (instance == null) {
      instance = new UserHelper();
    }
    return instance;
  }

  private SettingBundle getSettings() {
    return ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
  }

  public DetailUserDTO populate(UserDetail user) {
    DetailUserDTO dto = new DetailUserDTO();
    initUserDTO(user, dto);
    dto.setToken(user.getToken());
    dto.setZone(user.getUserPreferences().getZoneId().getId());
    dto.setLdap(!user.getDomain().getDriverClassName()
            .equals("org.silverpeas.core.admin.domain.driver.SilverpeasDomainDriver"));

    boolean notificationBox = false;
    List<Properties> channels = null;
    try {
      channels = NotificationManager.get().getNotifAddressProperties(user.getId());
    } catch (NotificationException e) {
      SilverLogger.getLogger(this).error(e);
    }
    for (Properties channel : Objects.requireNonNull(channels)) {
      String isDefault = channel.getProperty("isDefault");
      String canal = channel.getProperty("channel");
      if (canal.equalsIgnoreCase("SILVERMAIL") && isDefault.equalsIgnoreCase("true")) {
        notificationBox = true;
      }
    }
    dto.setNotificationBox(notificationBox);


    return dto;
  }

  public static void initUserDTO(UserDetail user, DetailUserDTO dto) {
    dto.setId(user.getId());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.seteMail(user.getEmailAddress());
    dto.setStatus(user.getStatus());
    dto.setAvatar(user.getAvatar());
    dto.setLanguage(user.getUserPreferences().getLanguage());
  }

  public UserDTO populateUserDTO(UserDetail user) {
    UserDTO dto = new UserDTO();
    dto.setId(user.getId());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.seteMail(user.getEmailAddress());
    String avatar = DataURLHelper.convertAvatarToUrlData(user.getAvatarFileName(),
        getSettings().getString("avatar.size", "24x"));
    dto.setAvatar(avatar);
    return dto;
  }

  public GroupDTO populateGroupDTO(Group group) {
    GroupDTO dto = new GroupDTO();
    dto.setId(group.getId());
    dto.setName(group.getName());
    dto.setNbMembers(group.getDirectUsersCount());
    return dto;
  }

  public String getUserLook(String userId) {
    UserDetail user;
    try {
      user = Administration.get().getUserDetail(userId);
    } catch(Exception e) {
      throw new SilverpeasRuntimeException("Unable to found user '" + userId + "'", e);
    }
    return getUserLook(user);
  }

  public String getUserLook(UserDetail user) {
    String look = user.getUserPreferences().getLook();
    if(!StringUtil.isDefined(look)) look = GraphicElementFactory.DEFAULT_LOOK_NAME;
    return look;
  }
}
