package org.silverpeas.mobile.server.dao;

import java.util.Date;

public class SecurityCode {
    private String code;
    private Date creationDate;

    public SecurityCode(String code, Date creationDate) {
        this.code = code;
        this.creationDate = creationDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
