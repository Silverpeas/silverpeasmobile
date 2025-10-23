package org.silverpeas.mobile.shared.dto.navigation.aurora;

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
}
