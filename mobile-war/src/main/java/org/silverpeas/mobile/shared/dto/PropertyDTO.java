package org.silverpeas.mobile.shared.dto;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;

import java.io.Serializable;

public class PropertyDTO implements Serializable {
    private String key;
    private String value;

    public static PropertyDTO fromJSON(JsPropertyMap<Object> json) {
        PropertyDTO dto = new PropertyDTO();
        dto.setKey((String) json.get("key"));
        dto.setValue((String) json.get("value"));
        return dto;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public JsPropertyMap<Object> toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();
        json.set("key", key);
        json.set("value", value);
        return json;
    }
}
