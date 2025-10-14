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

package org.silverpeas.mobile.server.dao.token;

import org.silverpeas.kernel.SilverpeasRuntimeException;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Connection;
import java.util.List;

import static org.silverpeas.core.persistence.jdbc.DBUtil.openConnection;

/**
 * @author svu
 */
@Named("tokenDAO")
public class JDBCTokenDAO implements TokenDAO {

  @Inject
  private JDBCTokenRequester theRequester;

  private JDBCTokenRequester getRequester() {
    return theRequester;
  }

  @Override
  public void saveToken(final String userId, final String token) {
    try (Connection con = openConnection()) {
      JDBCTokenRequester requester = getRequester();
      requester.saveToken(con, userId, token);
    } catch (Exception re) {
      throw new SilverpeasRuntimeException(re.getMessage(), re);
    }
  }

  @Override
  public void removeToken(final String userId, final String token) {
    try (Connection con = openConnection()) {
      JDBCTokenRequester requester = getRequester();
      requester.removeToken(con, userId, token);
    } catch (Exception re) {
      throw new SilverpeasRuntimeException(re.getMessage(), re);
    }
  }

  @Override
  public List<String> getTokens(final String userId) {
    try (Connection con = openConnection()) {
      JDBCTokenRequester requester = getRequester();
      return requester.getTokens(con, userId);
    } catch (Exception re) {
      throw new SilverpeasRuntimeException(re.getMessage(), re);
    }
  }

}

