package org.silverpeas.mobile.shared.dto.formsonline;

import jsinterop.base.JsPropertyMap;

import java.io.Serializable;
import java.util.List;

public class FormLayerDTO implements Serializable {

    private String html;
    private List<String> scripts;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public List<String> getScripts() {
        return scripts;
    }

    public void setScripts(List<String> scripts) {
        this.scripts = scripts;
    }

    public static FormLayerDTO fromJSON(JsPropertyMap<Object> json) {
        FormLayerDTO dto = new FormLayerDTO();

        dto.setHtml(json.get("html") != null ? json.get("html").toString() : null);

        Object scriptsObj = json.get("scripts");
        if (scriptsObj != null) {
            if (scriptsObj instanceof List) {
                dto.setScripts((List<String>) scriptsObj);
            } else {
                try {
                    List<String> scripts = new java.util.ArrayList<>();
                    jsinterop.base.JsArrayLike<?> arrayLike =
                            jsinterop.base.Js.asArrayLike(scriptsObj);

                    for (int i = 0; i < arrayLike.getLength(); i++) {
                        Object item = arrayLike.getAt(i);
                        if (item != null) {
                            scripts.add(item.toString());
                        }
                    }

                    dto.setScripts(scripts);

                } catch (Exception e) {
                    dto.setScripts(null);
                }
            }
        }

        return dto;
    }
}
