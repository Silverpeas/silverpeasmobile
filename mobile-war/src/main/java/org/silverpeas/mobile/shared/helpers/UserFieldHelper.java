package org.silverpeas.mobile.shared.helpers;

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;

public class UserFieldHelper {
    public static BaseDTO userFieldFromJSON(JsPropertyMap<Object> json) {
        BaseDTO dto = null;
        Object type = json.get("className");
        if (type != null && type.toString().contains(UserDTO.class.getSimpleName())) {
            dto = UserDTO.fromJSON(json);
        } else if (type != null && type.toString().contains(GroupDTO.class.getSimpleName())) {
            dto = GroupDTO.fromJSON(json);
        }
        return dto;
    }
}
