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

package org.silverpeas.mobile.shared.dto.almanach;

import jsinterop.base.JsPropertyMap;

import java.io.Serializable;

/**
 * @author svu
 */
public class CalendarEventRecurrenceDTO {
    private CalendarEventRecurrenceDTO.FrequencyDTO frequency;
    private int count = 0;
    private String endDate = null;

    //private List<CalendarEventRecurrenceEntity.DayOfWeekOccurrenceEntity> daysOfWeek = new ArrayList(7);

    public FrequencyDTO getFrequency() {
        return frequency;
    }

    public void setFrequency(final FrequencyDTO frequency) {
        this.frequency = frequency;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public static CalendarEventRecurrenceDTO fromJSON(JsPropertyMap<Object> json) {

        if (json == null) {
            return null;
        }
        CalendarEventRecurrenceDTO dto = new CalendarEventRecurrenceDTO();

        // frequency (safe cast + null check)
        dto.setFrequency(json.get("frequency") != null ? FrequencyDTO.fromJSON((JsPropertyMap<Object>) json.get("frequency")) : null);

        // count (safe number)
        dto.setCount(json.get("count") != null ? ((Number) json.get("count")).intValue() : 0);

        // endDate
        dto.setEndDate(json.get("endDate") != null ? json.get("endDate").toString() : null);

        return dto;
    }

    public Object toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();

        // count
        json.set("count", getCount());

        // endDate
        json.set("endDate", getEndDate());

        // frequency (nullable)
        if (getFrequency() != null) {
            JsPropertyMap<Object> freqJson = JsPropertyMap.of();

            freqJson.set("interval", getFrequency().getInterval());
            freqJson.set("timeUnit",
                    getFrequency().getTimeUnit() != null
                            ? getFrequency().getTimeUnit().name()
                            : null);

            json.set("frequency", freqJson);
        } else {
            json.set("frequency", null);
        }

        return json;
    }

    public static class FrequencyDTO implements Serializable {
        private int interval;
        private TimeUnitDTO timeUnit;

        public int getInterval() {
            return this.interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public TimeUnitDTO getTimeUnit() {
            return this.timeUnit;
        }

        public void setTimeUnit(TimeUnitDTO timeUnit) {
            this.timeUnit = timeUnit;
        }

        public static FrequencyDTO fromJSON(JsPropertyMap<Object> json) {
            FrequencyDTO dto = new FrequencyDTO();

            if (json == null) {
                return dto;
            }

            // interval
            dto.setInterval(json.get("interval") != null ? ((Number) json.get("interval")).intValue() : 0);

            // timeUnit (safe enum)
            dto.setTimeUnit(json.get("timeUnit") != null ? TimeUnitDTO.valueOf(json.get("timeUnit").toString()) : null);

            return dto;
        }


    }
}
