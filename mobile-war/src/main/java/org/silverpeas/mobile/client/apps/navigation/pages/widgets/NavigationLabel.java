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

package org.silverpeas.mobile.client.apps.navigation.pages.widgets;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/** Keeps a leading emoji separate for optional card layouts, preserving the original label. */
public final class NavigationLabel {
  private NavigationLabel() {}

  public static SafeHtml format(String label) {
    String text = label == null ? "" : label;
    int end = leadingEmojiEnd(text);
    SafeHtmlBuilder html = new SafeHtmlBuilder();
    if (end > 0) {
      html.appendHtmlConstant("<span class='homepage-label-icon'>")
          .appendEscaped(text.substring(0, end)).appendHtmlConstant("</span>");
    }
    return html.appendHtmlConstant("<span class='homepage-label-text'>")
        .appendEscaped(text.substring(end)).appendHtmlConstant("</span>").toSafeHtml();
  }

  static int leadingEmojiEnd(String text) {
    if (text.isEmpty()) return 0;
    int first = text.codePointAt(0);
    int end = Character.charCount(first);
    // Keycap emojis must include the enclosing keycap; ordinary numbers remain text.
    if (first == '#' || first == '*' || (first >= '0' && first <= '9')) {
      if (end < text.length() && text.codePointAt(end) == 0xFE0F) end++;
      return end < text.length() && text.codePointAt(end) == 0x20E3 ? end + 1 : 0;
    }
    if (!isEmoji(first)) return 0;
    if (isRegionalIndicator(first) && end < text.length()
        && isRegionalIndicator(text.codePointAt(end))) {
      end += Character.charCount(text.codePointAt(end));
    }
    while (end < text.length()) {
      int next = text.codePointAt(end);
      if (next == 0xFE0F || (next >= 0x1F3FB && next <= 0x1F3FF)
          || (next >= 0xE0020 && next <= 0xE007F)) {
        end += Character.charCount(next);
      } else if (next == 0x200D && end + 1 < text.length()
          && isEmoji(text.codePointAt(end + 1))) {
        end += 1 + Character.charCount(text.codePointAt(end + 1));
      } else {
        break;
      }
    }
    return end;
  }

  private static boolean isEmoji(int codePoint) {
    return (codePoint >= 0x1F000 && codePoint <= 0x1FAFF)
        || (codePoint >= 0x2600 && codePoint <= 0x27BF)
        || (codePoint >= 0x231A && codePoint <= 0x231B)
        || (codePoint >= 0x23E9 && codePoint <= 0x23F3)
        || (codePoint >= 0x23F8 && codePoint <= 0x23FA)
        || (codePoint >= 0x2B05 && codePoint <= 0x2B07)
        || codePoint == 0x2B50 || codePoint == 0x2B55;
  }

  private static boolean isRegionalIndicator(int codePoint) {
    return codePoint >= 0x1F1E6 && codePoint <= 0x1F1FF;
  }
}
