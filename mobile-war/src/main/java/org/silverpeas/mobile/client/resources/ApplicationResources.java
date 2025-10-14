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

package org.silverpeas.mobile.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import org.silverpeas.mobile.shared.dto.ShortCutLinkDTO;

/**
 * @author: svu
 */
public interface ApplicationResources extends ClientBundle {

  @Source("application.css")
  ApplicationCSS css();

  @Source("icons/avatar.png")
  ImageResource avatar();

  @Source("icons/offline.png")
  ImageResource offline();

  @Source("icons/star.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource favorite();

  @Source("icons/expand_more.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource more();

  @Source("icons/expand_less.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource less();

  @Source("icons/call.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource call();

  @Source("icons/sms.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource sms();

  @Source("icons/peoples.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource peoples();

  @Source("icons/arrow_forward.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource arrowForward();


  /** Applications icons **/

  @Source("icons/apps/blog.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource blog();

  @Source("icons/apps/news.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource news();

  @Source("icons/apps/calendar.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource calendar();

  @Source("icons/apps/webpage.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource webpage();

  @Source("icons/apps/ged.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource ged();

  @Source("icons/apps/media_library.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource mediaLib();

  @Source("icons/apps/link.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource link();

  @Source("icons/apps/classifieds.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource classifieds();

  @Source("icons/apps/faq.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource faq();

  @Source("icons/apps/form.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource form();

  @Source("icons/apps/book_online.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource bookonline();

  @Source("icons/apps/poll.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource poll();

  @Source("icons/apps/quiz.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource quizz();

  @Source("icons/apps/workflow.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource workflow();

  /** Applications images ressources **/

  @Source("icons/apps/ged/folder.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource folder();

  @Source("icons/apps/ged/trash.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource trash();

  @Source("icons/apps/ged/publication.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource publication();

  @Source("icons/shortcuts/home_shortcut.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource homeShortcut();

  @Source("icons/shortcuts/favorites_shortcut.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource favoritesShortcut();

  @Source("icons/shortcuts/contacts_shortcut.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource contactsShortcut();

  @Source("icons/shortcuts/tasks_shortcut.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource tasksShortcut();

  @Source("icons/shortcuts/chat_shortcut.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource chatShortcut();
  @Source("icons/menu/help.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource help();

  @Source("icons/menu/settings.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource settings();

  @Source("icons/menu/home.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource home();

  @Source("icons/menu/logout.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource logout();

  @Source("icons/menu/usercalendar.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource usercalendar();

  @Source("icons/menu/sharebox.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource sharebox();

  @Source("icons/menu/inbox.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource inbox();

  @Source("icons/menu/search.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource search();

  @Source("icons/apps/orgchartgroup.svg")
  @DataResource.MimeType("image/svg+xml")
  TextResource orgchartGroup();
}

