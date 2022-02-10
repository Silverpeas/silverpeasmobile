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

package org.silverpeas.mobile.client.apps.media.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaItemClickEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.media.MediaDTO;
import org.silverpeas.mobile.shared.dto.media.PhotoDTO;
import org.silverpeas.mobile.shared.dto.media.SoundDTO;
import org.silverpeas.mobile.shared.dto.media.VideoDTO;
import org.silverpeas.mobile.shared.dto.media.VideoStreamingDTO;

public class MediaItem extends Composite {

  private MediaDTO data;
  private static MediaItemUiBinder uiBinder = GWT.create(MediaItemUiBinder.class);
  @UiField Anchor link;
  @UiField ImageElement thumb;
  @UiField SpanElement detail;
  @UiField HTMLPanel container;
  private ApplicationResources resources = GWT.create(ApplicationResources.class);
  protected ApplicationMessages msg = null;

  interface MediaItemUiBinder extends UiBinder<Widget, MediaItem> {
  }

  public MediaItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(MediaDTO data) {
    this.data = data;
    link.setTitle(data.getTitle());
    thumb.setAlt(data.getTitle());
    detail.setInnerText(data.getTitle());

    if (data instanceof PhotoDTO) {
      thumb.setSrc( ((PhotoDTO)data).getDataPhoto());
    } else if (data instanceof SoundDTO) {
      thumb.setSrc(resources.sound().getSafeUri().asString());
    } else if (data instanceof VideoDTO) {
      String url = UrlUtils.getSilverpeasServicesLocation();
      url += "gallery/" + ((VideoDTO) data).getInstance() + "/videos/" + ((VideoDTO) data).getId() + "/thumbnail/0";
      thumb.setSrc(url);
    } else if (data instanceof VideoStreamingDTO) {
      String url = ((VideoStreamingDTO) data).getUrlPoster();
      if (url.isEmpty()) {
        thumb.setSrc(resources.streaming().getSafeUri().asString());
      } else {
        thumb.setSrc(url);
      }
    }
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    EventBus.getInstance().fireEvent(new MediaItemClickEvent(data));
  }
}
