package com.silverpeas.mobile.server.dao;

import com.silverpeas.mobile.server.config.Configurator;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import org.silverpeas.core.persistence.jdbc.DBUtil;
import org.silverpeas.core.util.UtilException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    while (rs.next() && index < step * Configurator.getConfigIntValue("mystatus.pagesize")) {
      StatusDTO status = new StatusDTO();
      status.setId(rs.getInt(1));
      status.setUserId(rs.getInt(2));
      status.setCreationDate(new Date(rs.getTimestamp(3).getTime()));
      status.setDescription(rs.getString(4));
      if (index >= (step-1) * Configurator.getConfigIntValue("mystatus.pagesize") || step == 1) {
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
