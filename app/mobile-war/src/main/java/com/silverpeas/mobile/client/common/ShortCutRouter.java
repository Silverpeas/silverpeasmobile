package com.silverpeas.mobile.client.common;

import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import com.silverpeas.mobile.shared.dto.ContentDTO;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

/**
 * @author: svu
 */
public class ShortCutRouter {
  public static void route(final DetailUserDTO user, String shortcutAppId, String shortcutContentType, String shortcutContentId) {
    if (!shortcutContentType.isEmpty() && !shortcutContentId.isEmpty() && !shortcutAppId.isEmpty()) {
      ContentDTO content = new ContentDTO();
      content.setId(shortcutContentId);
      content.setType(shortcutContentType);
      content.setInstanceId(shortcutAppId);
      EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));
    }
  }
}
