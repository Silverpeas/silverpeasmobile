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


import org.silverpeas.core.persistence.jdbc.DBUtil;

import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author svu
 */
@Singleton
public class JDBCStatisticsRequester {

  protected JDBCStatisticsRequester() {
  }

    public void saveLoginEvent(Connection con, long userId, short platform,
                               String appVersion, String osVersion,
                               String ipAddress, String countryCode, String device) throws SQLException {

            String insertQuery = "INSERT INTO sc_spmobil_loginevents "
                + "(user_id, occurred_at, platform, app_version, os_version, ip_address, country_code, device) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement prepStmt = null;
        try {
            prepStmt = con.prepareStatement(insertQuery);

            // user_id
            prepStmt.setLong(1, userId);

            // occurred_at = maintenant
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
            prepStmt.setTimestamp(2, currentTimestamp);

            // platform
            prepStmt.setShort(3, platform);

            // app_version
            prepStmt.setString(4, appVersion);

            // device_id
            prepStmt.setString(5, osVersion);

            // ip_address
            prepStmt.setString(6, ipAddress);

            // country_code
            prepStmt.setString(7, countryCode);

            prepStmt.setString(8, device);

            prepStmt.executeUpdate();
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
        }
    }


}
