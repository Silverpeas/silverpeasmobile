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

package org.silverpeas.mobile.client.apps.documents.resources;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface DocumentsMessages extends Messages {
  String title();

  // Publication page
  String lastUpdate(String updateDate, String updater);
  String publicationTitle();
  String sizeK(String size);
  String sizeM(String size);

  String content();
  String file();
  String files(int number);

  String creation(String creationDate, String creator);

  String views(int number);
  String view();
  String noview();

  String share();

  String validityPermanent();
  String validityTemporary();

  String validityMandatory();

  SafeHtml validityLabel();
  SafeHtml endValidityLabel();
  SafeHtml maxAccessLabel();
  SafeHtml usersLabel();
  SafeHtml emailsLabel();
  SafeHtml commentsLabel();

  String importFile();
  String maxUploadError();

  String fileNotSupportedError();

  String draftOut();

  String edit();

  String fileNotVerifiedError();

  String fileInfectedError();
}
