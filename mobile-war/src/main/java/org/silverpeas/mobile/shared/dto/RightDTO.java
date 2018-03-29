/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package org.silverpeas.mobile.shared.dto;

import java.io.Serializable;

/**
 * @author: svu
 */
public class RightDTO implements Serializable {

  private static final long serialVersionUID = 1186851689918190659L;

  private boolean reader = false;
  private boolean writer = false;
  private boolean publisher = false;
  private boolean manager = false;

  public boolean isWriter() {
    return writer;
  }

  public void setWriter(final boolean writer) {
    this.writer = writer;
  }

  public boolean isPublisher() {
    return publisher;
  }

  public void setPublisher(final boolean publisher) {
    this.publisher = publisher;
  }

  public boolean isManager() {
    return manager;
  }

  public void setManager(final boolean manager) {
    this.manager = manager;
  }

  public boolean isReader() {
    return reader;
  }

  public void setReader(final boolean reader) {
    this.reader = reader;
  }
}
