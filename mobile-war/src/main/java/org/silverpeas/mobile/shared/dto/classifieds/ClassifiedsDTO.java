/*
 * Copyright (C) 2000 - 2026 Silverpeas
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

package org.silverpeas.mobile.shared.dto.classifieds;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author svu
 */
public class ClassifiedsDTO extends BaseDTO {

    private Map<String, String> types;
    private Map<String, String> categories;
    private List<ClassifiedDTO> classifieds = new ArrayList<>();
    private boolean hasComments;

    public Map<String, String> getTypes() {
        return types;
    }

    public List<ClassifiedDTO> getClassifieds() {
        return classifieds;
    }

    public Map<String, String> getCategories() {
        return categories;
    }

    public void setClassifieds(final List<ClassifiedDTO> classifieds) {
        this.classifieds = classifieds;
    }

    public void setTypes(final Map<String, String> types) {
        this.types = types;
    }

    public void setCategories(final Map<String, String> categories) {
        this.categories = categories;
    }

    public boolean getHasComments() {
        return hasComments;
    }

    public void setHasComments(final boolean hasComments) {
        this.hasComments = hasComments;
    }

    public static ClassifiedsDTO fromJSON(JsPropertyMap<Object> json) {
        if (json == null) {
            return null;
        }

        ClassifiedsDTO dto = new ClassifiedsDTO();

        Object hasCommentsObj = json.get("hasComments");
        if (hasCommentsObj != null) {
            dto.setHasComments(Boolean.parseBoolean(hasCommentsObj.toString()));
        }

        Object typesObj = json.get("types");
        if (typesObj != null) {
            JsPropertyMap<String> typesMap = (JsPropertyMap<String>) typesObj;
            Map<String, String> types = new java.util.HashMap<>();
            String[] keys = getKeys(typesMap);
            for (String key : keys) {
                types.put(key, typesMap.get(key));
            }
            dto.setTypes(types);
        }

        Object catsObj = json.get("categories");
        if (catsObj != null) {
            JsPropertyMap<String> catsMap = (JsPropertyMap<String>) catsObj;
            Map<String, String> cats = new java.util.HashMap<>();
            String[] keys = getKeys(catsMap);
            for (String key : keys) {
                cats.put(key, catsMap.get(key));
            }
            dto.setCategories(cats);
        }

        if (json.get("classifieds") != null) {
            List<ClassifiedDTO> result = new ArrayList<>();
            JsArray<Object> list = (JsArray<Object>) json.get("classifieds");
            for (int i = 0; i < list.length; i++) {
                JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                result.add(ClassifiedDTO.fromJSON(map));
            }
            dto.setClassifieds(result);
        }
        
        return dto;
    }

    private static native String[] getKeys(JsPropertyMap<?> map) /*-{
        return Object.keys(map);
    }-*/;

}
