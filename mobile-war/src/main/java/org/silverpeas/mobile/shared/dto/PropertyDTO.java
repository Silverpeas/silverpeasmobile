package org.silverpeas.mobile.shared.dto;

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


}
