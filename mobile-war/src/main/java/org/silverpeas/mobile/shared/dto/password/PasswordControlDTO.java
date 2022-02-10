/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.shared.dto.password;

import java.io.Serializable;

/**
 * @author svu
 */
public class PasswordControlDTO implements Serializable {

  String[] requiredRuleIdsInError;
  String[] combinedRuleIdsInError;
  boolean isRuleCombinationRespected;
  boolean isCorrect;

  public String[] getRequiredRuleIdsInError() {
    return requiredRuleIdsInError;
  }

  public void setRequiredRuleIdsInError(final String[] requiredRuleIdsInError) {
    this.requiredRuleIdsInError = requiredRuleIdsInError;
  }

  public String[] getCombinedRuleIdsInError() {
    return combinedRuleIdsInError;
  }

  public void setCombinedRuleIdsInError(final String[] combinedRuleIdsInError) {
    this.combinedRuleIdsInError = combinedRuleIdsInError;
  }

  public boolean isRuleCombinationRespected() {
    return isRuleCombinationRespected;
  }

  public void setIsRuleCombinationRespected(final boolean isRuleCombinationRespected) {
    this.isRuleCombinationRespected = isRuleCombinationRespected;
  }

  public boolean isCorrect() {
    return isCorrect;
  }

  public void setIsCorrect(final boolean isCorrect) {
    this.isCorrect = isCorrect;
  }
}
