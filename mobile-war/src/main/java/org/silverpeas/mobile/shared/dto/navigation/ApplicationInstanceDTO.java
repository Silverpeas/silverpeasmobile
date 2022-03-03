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

package org.silverpeas.mobile.shared.dto.navigation;

import org.silverpeas.mobile.shared.dto.RightDTO;

import java.io.Serializable;


public class ApplicationInstanceDTO extends SilverpeasObjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String type;
  private RightDTO rights;
  private boolean commentable;
  private boolean notifiable;
  private boolean ableToStoreContent;
  private boolean workflow;
  private String extraId;

  public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}

  public RightDTO getRights() {
    return rights;
  }

  public void setRights(final RightDTO rights) {
    this.rights = rights;
  }

  public boolean isCommentable() {
    return commentable;
  }

  public void setCommentable(final boolean commentable) {
    this.commentable = commentable;
  }

  public void setAbleToStoreContent(final boolean ableToStoreContent) {
    this.ableToStoreContent = ableToStoreContent;
  }

  public boolean isAbleToStoreContent() {
    return ableToStoreContent;
  }

  public boolean isNotifiable() {
    return notifiable;
  }

  public void setNotifiable(boolean notifiable) {
    this.notifiable = notifiable;
  }

  public void setWorkflow(boolean workflow) {
    this.workflow = workflow;
  }

  public boolean isWorkflow() {
    return workflow;
  }

  public void setExtraId(final String extraId) { this.extraId = extraId; }

  public String getExtraId() { return extraId; }
}
