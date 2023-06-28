/*
 * Copyright (C) 2000 - 2023 Silverpeas
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

package org.silverpeas.bridge;

import javax.servlet.http.HttpServletRequest;

import static java.util.Optional.ofNullable;

/**
 * This class allows handling a session safely against the different request calls.
 * <p>
 *   Some synchronized access are performed around session creation and session attribute accesses.
 * </p>
 * @author silveryocha
 */
public class SafeSessionManager {
  private static final Object MUTEX = new Object();

  private final HttpServletRequest request;

  public SafeSessionManager(final HttpServletRequest request) {
    this.request = request;
  }

  /**
   * Gets the specified attribute value from the session.
   * <p>
   *   If the session does not exists, it is not created.
   * </p>
   * <p>
   *   If no value exists, null is returned.
   * </p>
   * @param attributeName the name of the attribute to fetch.
   * @param aClass the class of the value fetched.
   * @return the value if any, null otherwise.
   * @param <T> the type of the value.
   */
  public <T> T getAttribute(final String attributeName, final Class<T> aClass) {
    return getAttribute(attributeName, aClass, null);
  }

  /**
   * Gets the specified attribute value from the session.
   * <p>
   *   If the session does not exists, it is not created.
   * </p>
   * <p>
   *   If no value exists, default one is returned.
   * </p>
   * @param attributeName the name of the attribute to fetch.
   * @param aClass the class of the value fetched.
   * @param defaultValue default value of no value fetched from session.
   * @return the value if any, default value otherwise.
   * @param <T> the type of the value.
   */
  public <T> T getAttribute(final String attributeName, final Class<T> aClass,
      final T defaultValue) {
    synchronized (MUTEX) {
      return ofNullable(request.getSession(false))
          .map(s -> s.getAttribute(attributeName))
          .map(aClass::cast)
          .orElse(defaultValue);
    }
  }

  /**
   * Sets the specified attribute value into the session.
   * <p>
   *   If the session does not exist, it is created.
   * </p>
   * @param attributeName the name of the attribute to fetch.
   * @param value the value to put.
   * @param <T> the type of the value.
   */
  public <T> void setAttribute(final String attributeName, final T value) {
    synchronized (MUTEX) {
      request.getSession().setAttribute(attributeName, value);
    }
  }

  /**
   * Removes from the session the specified attribute.
   * <p>
   *   If the session does not exist, nothing is performed.
   * </p>
   * @param attributeName the name of the attribute to fetch.
   * @param aClass the class of the value fetched.
   * @return the removed value if any, null otherwise.
   * @param <T> the type of the value.
   */
  public <T> T removeAttribute(final String attributeName, final Class<T> aClass) {
    synchronized (MUTEX) {
      final T value = getAttribute(attributeName, aClass);
      ofNullable(request.getSession(false))
          .ifPresent(s -> s.removeAttribute(attributeName));
      return value;
    }
  }
}
