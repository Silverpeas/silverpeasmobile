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

package com.silverpeas.mobile.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.silverpeas.mobile.server.config.Configurator;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.stratelia.webactiv.util.DBUtil;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.UtilException;

import edu.emory.mathcs.backport.java.util.Collections;

public class StatusDao {

  public List<StatusDTO> getAllStatus(int userId, int step) throws SQLException {
    Connection connection = getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      String query =
          "SELECT id,userid, creationdate, description FROM sb_sn_status WHERE userid = ? ORDER BY creationdate DESC";
      pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, userId);
      rs = pstmt.executeQuery();

      return getSocialInformationsList(rs, step);
    } finally {
      DBUtil.close(rs, pstmt);
      DBUtil.close(connection);
    }
  }

  private List<StatusDTO> getSocialInformationsList(ResultSet rs, int step) throws SQLException {
    ArrayList<StatusDTO> statusList = new ArrayList<StatusDTO>();
    int index = 0;
    while (rs.next() && index < step * Configurator.getConfigIntValue("mystatus.pagesize")) {
      StatusDTO status = new StatusDTO();
      status.setId(rs.getInt(1));
      status.setUserId(rs.getInt(2));
      status.setCreationDate(new Date(rs.getTimestamp(3).getTime()));
      status.setDescription(rs.getString(4));
      if (index >= (step - 1) * Configurator.getConfigIntValue("mystatus.pagesize") || step == 1) {
        statusList.add(status);
      }
      index++;
    }
    Collections.sort(statusList);
    return statusList;
  }

  private Connection getConnection() throws UtilException, SQLException {
    return DBUtil.makeConnection(JNDINames.DATABASE_DATASOURCE);
  }
}
