package org.silverpeas.mobile.shared.dto.documents;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class DocumentDTO extends BaseDTO {
    public static List<BaseDTO> fromJSON(JsArray<Object> json) {
        List<BaseDTO> result = new ArrayList<>();
        if (json == null) {
            return result;
        }

        if (json != null) {
            for (int i = 0; i < json.length; i++) {
                JsPropertyMap<Object> map = (JsPropertyMap<Object>) json.getAt(i);
                if (map.get("className").equals(TopicDTO.class.getSimpleName())) {
                    result.add(TopicDTO.fromJSON(map));
                } else if (map.get("className").equals(PublicationDTO.class.getSimpleName())) {
                    result.add(PublicationDTO.fromJSON(map));
                }
            }
        }

        return result;
    }
}
