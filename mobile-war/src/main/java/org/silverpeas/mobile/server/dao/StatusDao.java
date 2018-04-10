/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.server.dao;

import org.silverpeas.core.persistence.jdbc.DBUtil;
import org.silverpeas.core.util.UtilException;
import org.silverpeas.mobile.shared.dto.StatusDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StatusDao {

  private static final int mystatus_pagesize = 10;

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

  public StatusDTO getStatus(int userId) throws SQLException {
    Connection connection = getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      String query =
          "SELECT id,userid, creationdate, description FROM sb_sn_status WHERE userid = ? ORDER BY creationdate DESC";
      pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, userId);
      rs = pstmt.executeQuery();

      return getSocialInformations(rs);
    } finally {
      DBUtil.close(rs, pstmt);
      DBUtil.close(connection);
    }
  }

  private StatusDTO getSocialInformations(ResultSet rs) throws SQLException {
    if (rs.next()) {
      StatusDTO status = new StatusDTO();
      status.setId(rs.getInt(1));
      status.setUserId(rs.getInt(2));
      status.setCreationDate(new Date(rs.getTimestamp(3).getTime()));
      status.setDescription(rs.getString(4));
      return status;
    }
    return null;
  }

  private List<StatusDTO> getSocialInformationsList(ResultSet rs, int step) throws SQLException {
    ArrayList<StatusDTO> statusList = new ArrayList<StatusDTO>();
    int index = 0;
    while (rs.next() && index < step * mystatus_pagesize) {
      StatusDTO status = new StatusDTO();
      status.setId(rs.getInt(1));
      status.setUserId(rs.getInt(2));
      status.setCreationDate(new Date(rs.getTimestamp(3).getTime()));
      status.setDescription(rs.getString(4));
      if (index >= (step-1) * mystatus_pagesize) {
        statusList.add(status);
      }
      index++;
    }
    Collections.sort(statusList);
    return statusList;
  }

  private Connection getConnection() throws UtilException, SQLException {
    return DBUtil.openConnection();
  }
}
