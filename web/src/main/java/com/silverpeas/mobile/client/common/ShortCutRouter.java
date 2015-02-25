package com.silverpeas.mobile.client.common;

import com.google.gwt.user.client.Window;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.media.MediaApp;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

/**
 * @author: svu
 */
public class ShortCutRouter {
  public static void route(final DetailUserDTO user, String shortcutAppId, String shortcutContentType, String shortcutContentId) {
    if (!shortcutContentType.isEmpty() && !shortcutContentId.isEmpty() && !shortcutAppId.isEmpty()) {
      if (shortcutContentType.equals(ContentsTypes.Publication.toString())) {
        DocumentsApp app = new DocumentsApp();
        app.startWithContent(shortcutAppId, ContentsTypes.Publication.toString(), shortcutContentId);
      } else if (shortcutContentType.equals(ContentsTypes.Media.toString())) {
        MediaApp app = new MediaApp();
        app.startWithContent(shortcutAppId, ContentsTypes.Media.toString(), shortcutContentId);
      }
    }
  }
}
