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

package org.silverpeas.mobile.shared.dto.media;

import jsinterop.base.JsPropertyMap;

import java.io.Serializable;

public class VideoStreamingDTO extends MediaDTO {

    private String duration;
    private String url;
    private String urlPoster;

    public VideoStreamingDTO() {
        setClassName(this.getClass().getSimpleName());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(final String duration) {
        this.duration = duration;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(final String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public static VideoStreamingDTO fromJSON(JsPropertyMap<Object> json) {

        VideoStreamingDTO dto = new VideoStreamingDTO();
        if (json == null) {
            return dto;
        }

        dto.setClassName(dto.getClass().getSimpleName());
        MediaDTO.fromJSON(json, dto);

        dto.setDuration(json.get("duration") != null ? json.get("duration").toString() : null);
        dto.setUrl(json.get("url") != null ? json.get("url").toString() : null);
        dto.setUrlPoster(json.get("urlPoster") != null ? json.get("urlPoster").toString() : null);

        return dto;
    }
}
