package org.silverpeas.mobile.shared.dto.navigation;

import java.io.Serializable;

public class AuroraSpaceHomePageConfig implements Serializable {

    private String introduction, picture;

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
}
