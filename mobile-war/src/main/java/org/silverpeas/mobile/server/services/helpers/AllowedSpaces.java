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

package org.silverpeas.mobile.server.services.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Configured mobile navigation roots, including all their descendants. */
public final class AllowedSpaces {
  private final Set<String> allowedIds = new LinkedHashSet<>();
  private final Set<String> excludedIds = new LinkedHashSet<>();
  private final boolean allRootsAllowed;

  @FunctionalInterface
  public interface ParentResolver {
    String getParentId(String normalizedSpaceId) throws Exception;
  }

  public AllowedSpaces(String configuration) {
    this(configuration, "");
  }

  public AllowedSpaces(String allowed, String excluded) {
    allRootsAllowed = allowed == null || allowed.trim().isEmpty();
    readIds(allowed, allowedIds);
    readIds(excluded, excludedIds);
  }

  private static void readIds(String configuration, Set<String> ids) {
    if (configuration != null) {
      for (String id : configuration.split("[,\\s]+")) {
        String normalized = normalizeId(id);
        if (!normalized.isEmpty()) ids.add(normalized);
      }
    }
  }

  public boolean hasAllowedRoots() {
    return !allRootsAllowed;
  }

  public boolean isUnrestricted() {
    return allRootsAllowed && excludedIds.isEmpty();
  }

  public boolean allows(String spaceId, ParentResolver parents) throws Exception {
    boolean included = allRootsAllowed || hasAllowedAncestor(spaceId, allowedIds, parents);
    return included && (excludedIds.isEmpty() || !hasAllowedAncestor(spaceId, excludedIds, parents));
  }

  /** Select only roots accessible to the user, without duplicating selected descendant branches. */
  public List<String> navigationRoots(Collection<String> accessibleIds, ParentResolver parents)
      throws Exception {
    Set<String> accessibleSelections = new HashSet<>();
    for (String id : accessibleIds) {
      if (allowedIds.contains(normalizeId(id))) accessibleSelections.add(normalizeId(id));
    }
    List<String> roots = new ArrayList<>();
    Set<String> added = new HashSet<>();
    for (String id : accessibleIds) {
      String normalized = normalizeId(id);
      if (accessibleSelections.contains(normalized) && added.add(normalized)
          && allows(normalized, parents)
          && !hasAllowedAncestor(parents.getParentId(normalized), accessibleSelections, parents)) {
        roots.add(id);
      }
    }
    return roots;
  }

  private static boolean hasAllowedAncestor(String spaceId, Set<String> selected,
      ParentResolver parents) throws Exception {
    Set<String> visited = new HashSet<>();
    String current = normalizeId(spaceId);
    while (!current.isEmpty() && !"0".equals(current) && visited.add(current)) {
      if (selected.contains(current)) return true;
      current = normalizeId(parents.getParentId(current));
    }
    return false;
  }

  public static String normalizeId(String spaceId) {
    if (spaceId == null) return "";
    String id = spaceId.trim().toUpperCase(Locale.ROOT);
    return id.startsWith("WA") ? id.substring(2) : id;
  }
}
