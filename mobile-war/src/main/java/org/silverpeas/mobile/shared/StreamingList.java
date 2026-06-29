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

package org.silverpeas.mobile.shared;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.media.*;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstanceDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: svu
 */
public class StreamingList<B> implements Serializable {
  private boolean moreElement;
  private boolean firstCall = false;
  private List<B> list = new ArrayList<>();

  public StreamingList(final Collection collection, final boolean moreElement) {
    super();
    list.addAll(collection);
    this.moreElement = moreElement;
  }

  public StreamingList() {
    super();
  }

  public boolean getMoreElement() {
    return moreElement;

  }

  public void setMoreElement(final boolean moreElement) {
    this.moreElement = moreElement;
  }

  public List<B> getList() {
    return list;
  }

  public void setList(final List<B> list) {
    this.list = list;
  }

  public boolean isFirstCall() {
    return firstCall;
  }

  public void setFirstCall(final boolean firstCall) {
    this.firstCall = firstCall;
  }

  public static <T> StreamingList<T> fromJSON(JsPropertyMap<Object> json) {
    StreamingList dto = new StreamingList();
    if (json == null) {
      return dto;
    }

    dto.setFirstCall(json.get("firstCall") != null ? Boolean.parseBoolean(json.get("firstCall").toString()) : false);
    dto.setMoreElement(json.get("moreElement") != null ? Boolean.parseBoolean(json.get("moreElement").toString()) : false);

    if (json.get("list") != null) {
      List<BaseDTO> result = new ArrayList<>();
      JsArray<Object> list = (JsArray<Object>) json.get("list");
      for (int i = 0; i < list.length; i++) {
        JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
        if (map.get("className").equals(AlbumDTO.class.getSimpleName())) {
          result.add(AlbumDTO.fromJSON(map));
        } else if (map.get("className").equals(PhotoDTO.class.getSimpleName())) {
          result.add(PhotoDTO.fromJSON(map));
        } else if (map.get("className").equals(VideoDTO.class.getSimpleName())) {
          result.add(VideoDTO.fromJSON(map));
        } else if (map.get("className").equals(VideoStreamingDTO.class.getSimpleName())) {
          result.add(VideoStreamingDTO.fromJSON(map));
        } else if (map.get("className").equals(SoundDTO.class.getSimpleName())) {
          result.add(SoundDTO.fromJSON(map));
        } else if (map.get("className").equals(WorkflowInstanceDTO.class.getSimpleName())) {
          result.add(WorkflowInstanceDTO.fromJSON(map));
        }
      }
      dto.setList(result);
    }

    return dto;
  }
}
