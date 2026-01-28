/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.server.dao.statistics;

import org.silverpeas.kernel.SilverpeasRuntimeException;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.mobile.server.dao.token.JDBCTokenRequester;
import org.silverpeas.mobile.server.dao.token.TokenDAO;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Connection;
import java.util.List;

import static org.silverpeas.core.persistence.jdbc.DBUtil.openConnection;

/**
 * @author svu
 */
@Named("statisticsDAO")
public class JDBCStatisticsDAO implements StatisticsDAO {

  @Inject
  private JDBCStatisticsRequester theRequester;

  private JDBCStatisticsRequester getRequester() {
    return theRequester;
  }


  @Override
  public void saveLoginEvent(long userId, short platform, String appVersion, String osVersion, String ipAddress, String countryCode, String device) {
    try (Connection con = openConnection()) {
      JDBCStatisticsRequester requester = getRequester();
      requester.saveLoginEvent(con, userId, platform, appVersion, osVersion, ipAddress, countryCode, device);
    } catch (Exception re) {
      SilverLogger.getLogger(this).error(re);
    }
  }
}

