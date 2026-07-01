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

package org.silverpeas.mobile.shared.dto.reminder;

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.almanach.TimeUnitDTO;

import java.io.Serializable;

/**
 * @author svu
 */
public class ReminderDTO implements Serializable {
    private String id;
    private String cId;
    private String cProperty;
    private String userId;
    private String dateTime;
    private Integer duration;
    private TimeUnitDTO timeUnit;
    private String text;
    private String processName;
    private boolean canBeModified;
    private boolean canBeDeleted;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(final String cId) {
        this.cId = cId;
    }

    public String getcProperty() {
        return cProperty;
    }

    public void setcProperty(final String cProperty) {
        this.cProperty = cProperty;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(final String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public TimeUnitDTO getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(final TimeUnitDTO timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public boolean isCanBeModified() {
        return canBeModified;
    }

    public void setCanBeModified(final boolean canBeModified) {
        this.canBeModified = canBeModified;
    }

    public boolean isCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(final boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(final String processName) {
        this.processName = processName;
    }

    public static ReminderDTO fromJSON(JsPropertyMap<Object> json) {
        ReminderDTO dto = new ReminderDTO();

        if (json == null) {
            return dto;
        }

        dto.setId(json.get("id") != null ? json.get("id").toString() : "");
        dto.setcId(json.get("cId") != null ? json.get("cId").toString() : "");
        dto.setcProperty(json.get("cProperty") != null ? json.get("cProperty").toString() : "");
        dto.setUserId(json.get("userId") != null ? json.get("userId").toString() : "");
        dto.setDateTime(json.get("dateTime") != null ? json.get("dateTime").toString() : "");
        dto.setText(json.get("text") != null ? json.get("text").toString() : "");
        dto.setProcessName(json.get("processName") != null ? json.get("processName").toString() : "");

        // numeric fields
        dto.setDuration(json.get("duration") != null ? ((Number) json.get("duration")).intValue() : 0);

        // enum (safe)
        dto.setTimeUnit(json.get("timeUnit") != null
                ? TimeUnitDTO.valueOf(json.get("timeUnit").toString())
                : null);

        // booleans
        dto.setCanBeModified(json.get("canBeModified") != null ? Boolean.parseBoolean(json.get("canBeModified").toString()) : false);
        dto.setCanBeDeleted(json.get("canBeDeleted") != null ? Boolean.parseBoolean(json.get("canBeDeleted").toString()) : false);

        return dto;
    }

    public Object toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();

        json.set("id", getId());
        json.set("cId", getcId());
        json.set("cProperty", getcProperty());
        json.set("userId", getUserId());
        json.set("dateTime", getDateTime());
        

        json.set("text", getText());
        json.set("processName", getProcessName());

        // numeric fields
        json.set("duration", getDuration().intValue());

        // enum
        json.set("timeUnit", getTimeUnit() != null ? getTimeUnit().name() : null);

        // booleans
        json.set("canBeModified", isCanBeModified());
        json.set("canBeDeleted", isCanBeDeleted());

        return json;
    }
}
