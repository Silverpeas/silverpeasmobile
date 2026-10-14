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

package org.silverpeas.mobile.client.components;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/** Displays creation and last modification metadata independently of the contribution type. */
public class AuditTrail extends Composite {
  private static final String DEFAULT_AVATAR_URL = "/silverpeas/directory/jsp/icons/avatar.png";
  private final Entry creation = new Entry();
  private final Entry lastUpdate = new Entry();

  public AuditTrail() {
    FlowPanel container = new FlowPanel();
    container.setStyleName("auditTrail");
    container.add(creation);
    container.add(lastUpdate);
    initWidget(container);
  }

  /** A null description hides and clears the entry. */
  public void setCreation(SafeHtml description, String avatarUrl) {
    creation.setData(description, avatarUrl);
  }

  /** A null description hides and clears the entry. */
  public void setLastUpdate(SafeHtml description, String avatarUrl) {
    lastUpdate.setData(description, avatarUrl);
  }

  private static String normalizeAvatarUrl(String avatarUrl) {
    if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
      return DEFAULT_AVATAR_URL;
    }
    String url = avatarUrl.trim();
    if (url.startsWith("/silverpeas/") || url.startsWith("http://")
        || url.startsWith("https://") || url.startsWith("data:") || url.startsWith("//")) {
      return url;
    }
    return "/silverpeas" + (url.startsWith("/") ? url : "/" + url);
  }

  private static class Entry extends FlowPanel {
    private final Image avatar = new Image();
    private final HTML description = new HTML();
    private boolean usingDefaultAvatar;

    private Entry() {
      setStyleName("auditTrailEntry");
      avatar.setStyleName("auditTrailAvatar");
      avatar.setAltText("");
      avatar.addLoadHandler(event -> avatar.setVisible(true));
      avatar.addErrorHandler(event -> {
        if (!usingDefaultAvatar) {
          usingDefaultAvatar = true;
          avatar.setUrl(DEFAULT_AVATAR_URL);
        } else {
          // Keep the avatar space without retrying a missing default image indefinitely.
          avatar.getElement().removeAttribute("src");
        }
      });
      description.setStyleName("auditTrailText");
      add(avatar);
      add(description);
      setVisible(false);
    }

    private void setData(SafeHtml text, String avatarUrl) {
      description.setHTML(text != null ? text : SafeHtmlUtils.fromString(""));
      avatar.getElement().removeAttribute("src");
      avatar.setVisible(text != null);
      if (text != null) {
        String url = normalizeAvatarUrl(avatarUrl);
        usingDefaultAvatar = DEFAULT_AVATAR_URL.equals(url);
        avatar.setUrl(url);
      }
      setVisible(text != null);
    }
  }
}
