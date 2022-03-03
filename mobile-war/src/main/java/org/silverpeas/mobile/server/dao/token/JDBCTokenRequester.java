/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.server.dao.token;


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
public class JDBCTokenRequester {

  protected JDBCTokenRequester() {
  }

  public void saveToken(Connection con, String userId, String token) throws SQLException {
    if (!isExist(con,userId, token)) {
      String insertQuery = "INSERT INTO st_token_pushnotification (id , userid, token, savedate) "
          + "VALUES ( ?, ?, ?, ? )";
      PreparedStatement prepStmt = null;
      long newId;
      newId = DBUtil.getNextId("st_token_pushnotification", "id");
      java.util.Date now = Calendar.getInstance().getTime();
      java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

      try {
        prepStmt = con.prepareStatement(insertQuery);
        prepStmt.setLong(1, newId);
        prepStmt.setString(2, userId);
        prepStmt.setString(3, token);
        prepStmt.setTimestamp(4, currentTimestamp);
        prepStmt.executeUpdate();
      } finally {
        DBUtil.close(prepStmt);
      }
    }
  }

  public List<String> getTokens(Connection con, String userId) throws SQLException {
    List<String> tokens = new ArrayList<>();
    String selectQuery = "SELECT token FROM st_token_pushnotification WHERE userId=?";
    PreparedStatement prepStmt = null;
    try {
      prepStmt = con.prepareStatement(selectQuery);
      prepStmt.setString(1, userId);
      ResultSet rs = prepStmt.executeQuery();
      while (rs.next()) {
        tokens.add(rs.getString(1));
      }
    } finally {
      DBUtil.close(prepStmt);
    }

    return tokens;
  }

  public void removeToken(Connection con, String userId, String token) throws SQLException {
    String deleteQuery = "DELETE FROM st_token_pushnotification WHERE userId=? AND token=?";
    PreparedStatement prepStmt = null;
    try {
      prepStmt = con.prepareStatement(deleteQuery);
      prepStmt.setString(1, userId);
      prepStmt.setString(2, token);
      prepStmt.executeUpdate();
    } finally {
      DBUtil.close(prepStmt);
    }
  }

  private boolean isExist(Connection con, String userId, String token) throws SQLException {
    String selectQuery = "SELECT count(*) FROM st_token_pushnotification WHERE userId=? AND token=?";
    PreparedStatement prepStmt = null;
    try {
      prepStmt = con.prepareStatement(selectQuery);
      prepStmt.setString(1, userId);
      prepStmt.setString(2, token);
      ResultSet rs = prepStmt.executeQuery();
      int n = 0;
      if (rs.next() ) {
        n = rs.getInt(1);
      }
      return (n > 0);
    } finally {
      DBUtil.close(prepStmt);
    }
  }
  
}
