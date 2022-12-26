package org.silverpeas.mobile.shared.dto.formsonline;

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
}
