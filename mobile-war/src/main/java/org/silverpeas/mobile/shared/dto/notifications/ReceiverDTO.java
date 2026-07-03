package org.silverpeas.mobile.shared.dto.notifications;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;

public class ReceiverDTO {

    public final static String TYPE_USER = "USER";
    public final static String TYPE_GROUP = "GROUP";

    private String id;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();
        json.set("id", getId());
        json.set("type", getType());
        return json;
    }
}
