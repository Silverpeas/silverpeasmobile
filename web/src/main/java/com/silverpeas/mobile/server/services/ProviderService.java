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

package com.silverpeas.mobile.server.services;

import java.util.ArrayList;
import java.util.List;

import com.silverpeas.calendar.Date;
import com.silverpeas.jcrutil.BasicDaoFactory;
import com.silverpeas.socialNetwork.model.SocialInformation;
import com.silverpeas.socialNetwork.model.SocialInformationType;
import com.silverpeas.socialNetwork.provider.ProviderSwitchInterface;
import com.stratelia.silverpeas.silvertrace.SilverTrace;

public class ProviderService {
  private ProviderSwitchInterface switchInterface;

  public ProviderService() {
    switchInterface = (ProviderSwitchInterface) BasicDaoFactory.getBean("providerSwitch");
  }

  public List<SocialInformation> getSocialInformationsListOfMyContact(
      SocialInformationType socialInformationType, String myId,
      List<String> myContactIds, Date begin, Date end) {
    try {
      return switchInterface.getSocialInformationsListOfMyContacts(socialInformationType, myId,
          myContactIds, begin, end);
    } catch (Exception ex) {
      SilverTrace.info("socialNetwork", "ProviderService.getSocialInformationsListOfMyContact",
          "root.MSG_GEN_ENTER_METHOD" + ex);
    }
    return new ArrayList<SocialInformation>();
  }
}
