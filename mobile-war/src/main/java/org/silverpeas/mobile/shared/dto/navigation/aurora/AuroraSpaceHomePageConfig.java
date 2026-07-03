package org.silverpeas.mobile.shared.dto.navigation.aurora;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.ShortCutLinkDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AuroraSpaceHomePageConfig implements Serializable {

    private String introduction, picture, latestPublications;

    private List<ShortCutLinkDTO> shortcuts;

    private List<PublicationDTO> lastPublications;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }

    public List<ShortCutLinkDTO> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(List<ShortCutLinkDTO> shortcuts) {
        this.shortcuts = shortcuts;
    }

    public String getLatestPublications() {
        return latestPublications;
    }

    public void setLatestPublications(String latestPublications) {
        this.latestPublications = latestPublications;
    }

    public List<PublicationDTO> getLastPublications() {
        return lastPublications;
    }

    public void setLastPublications(List<PublicationDTO> lastPublications) {
        this.lastPublications = lastPublications;
    }

    public static AuroraSpaceHomePageConfig fromJSON(JsPropertyMap<Object> json) {
        AuroraSpaceHomePageConfig dto = new AuroraSpaceHomePageConfig();

        if (json == null) {
            return dto;
        }

        dto.setIntroduction(json.get("introduction") != null ? json.get("introduction").toString() : "");
        dto.setPicture(json.get("picture") != null ? json.get("picture").toString() : "");
        dto.setLatestPublications(json.get("latestPublications") != null ? json.get("latestPublications").toString() : "");

        // ===== shortcuts =====
        if (json.get("shortcuts") != null) {
            List<ShortCutLinkDTO> result = new ArrayList<>();
            JsArray<Object> list = (JsArray<Object>) json.get("shortcuts");
            for (int i = 0; i < list.length; i++) {
                JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                result.add(ShortCutLinkDTO.fromJSON(map));
            }
            dto.setShortcuts(result);
        }

        // ===== last publications =====
        if (json.get("lastPublications") != null) {
            JsArray<Object> list = (JsArray<Object>) json.get("lastPublications");

            List<PublicationDTO> result = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                result.add(PublicationDTO.fromJSON(map));
            }

            dto.setLastPublications(result);
        }
        return dto;
    }
}

