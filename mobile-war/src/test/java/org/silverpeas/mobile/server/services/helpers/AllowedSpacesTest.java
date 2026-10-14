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

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class AllowedSpacesTest {
  private final Map<String, String> hierarchy = Map.of(
      "1", "0", "2", "0", "3", "0", "11", "WA1", "12", "11", "21", "2");
  private final AllowedSpaces.ParentResolver parents = hierarchy::get;

  @Test
  void emptyConfigurationKeepsExistingNavigation() throws Exception {
    for (String configuration : new String[] {null, "", "  "}) {
      AllowedSpaces spaces = new AllowedSpaces(configuration);
      assertTrue(spaces.isUnrestricted());
      assertTrue(spaces.allows("WA21", id -> { fail("No hierarchy lookup needed"); return null; }));
    }
  }

  @Test
  void selectedBranchesIncludeTheirDescendantsButNotOtherBranches() throws Exception {
    AllowedSpaces spaces = new AllowedSpaces("WA1,WA3");
    for (String id : List.of("WA1", "WA11", "WA12", "WA3")) assertTrue(spaces.allows(id, parents), id);
    for (String id : List.of("WA2", "WA21")) assertFalse(spaces.allows(id, parents), id);
  }

  @Test
  void whitespaceCaseAndNumericIdsAreAccepted() throws Exception {
    AllowedSpaces spaces = new AllowedSpaces(" WA1 , wa3 , 1 \t WA3 ");
    assertEquals(List.of("1", "WA3"), spaces.navigationRoots(List.of("1", "2", "WA3"), parents));
    assertTrue(spaces.allows("wa12", parents));
  }

  @Test
  void selectedSubspaceBecomesAMobileNavigationRoot() throws Exception {
    AllowedSpaces spaces = new AllowedSpaces("WA11");
    assertEquals(List.of("WA11"), spaces.navigationRoots(List.of("WA1", "WA11", "WA12"), parents));
    assertFalse(spaces.allows("WA1", parents));
    assertTrue(spaces.allows("WA12", parents));
  }

  @Test
  void selectedDescendantIsNotDuplicatedWhenItsAncestorIsSelected() throws Exception {
    AllowedSpaces spaces = new AllowedSpaces("WA1,WA11,WA3");
    assertEquals(List.of("WA1", "WA3"), spaces.navigationRoots(List.of("WA1", "WA11", "WA12", "WA3"), parents));
  }

  @Test
  void configuredSpaceCannotExtendTheUsersAccessibleRoots() throws Exception {
    AllowedSpaces spaces = new AllowedSpaces("WA1,WA3");
    assertEquals(List.of("WA1"), spaces.navigationRoots(List.of("WA1", "WA11"), parents));
  }

  @Test
  void selectedAccessibleChildIsNotHiddenByAnInaccessibleSelectedAncestor() throws Exception {
    AllowedSpaces spaces = new AllowedSpaces("WA1,WA11");
    assertEquals(List.of("WA11"), spaces.navigationRoots(List.of("WA11", "WA12"), parents));
  }

  @Test
  void unknownOrMalformedIdsDoNotDisableTheFilter() throws Exception {
    for (String configuration : List.of("WA999", "WA", ",")) {
      AllowedSpaces spaces = new AllowedSpaces(configuration);
      assertFalse(spaces.isUnrestricted());
      assertEquals(List.of(), spaces.navigationRoots(List.of("WA1", "WA3"), parents));
    }
  }

  @Test
  void duplicateAccessibleIdAliasesDoNotDuplicateNavigationEntries() throws Exception {
    AllowedSpaces spaces = new AllowedSpaces("WA1");
    assertEquals(List.of("WA1"), spaces.navigationRoots(List.of("WA1", "1"), parents));
  }

  @Test
  void malformedHierarchyCannotLoopIndefinitely() throws Exception {
    AtomicInteger lookups = new AtomicInteger();
    AllowedSpaces spaces = new AllowedSpaces("WA1");
    assertFalse(spaces.allows("WA88", id -> {
      lookups.incrementAndGet(); return "88".equals(id) ? "WA89" : "WA88";
    }));
    assertEquals(2, lookups.get());
  }
}
