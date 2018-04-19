package org.silverpeas.mobile.shared.dto.almanach;

import java.io.Serializable;

/**
 * @author svu
 */
public class CalendarDTO implements Serializable {
  private String id;
  private String title;
  private String zoneId;

  private boolean main;
  private boolean userMainPersonal;
  private boolean userPersonal;
  private String ownerName;


  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getZoneId() {
    return zoneId;
  }

  public void setZoneId(final String zoneId) {
    this.zoneId = zoneId;
  }

  public boolean isMain() {
    return main;
  }

  public void setMain(final boolean main) {
    this.main = main;
  }

  public boolean isUserMainPersonal() {
    return userMainPersonal;
  }

  public void setUserMainPersonal(final boolean userMainPersonal) {
    this.userMainPersonal = userMainPersonal;
  }

  public boolean isUserPersonal() {
    return userPersonal;
  }

  public void setUserPersonal(final boolean userPersonal) {
    this.userPersonal = userPersonal;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(final String ownerName) {
    this.ownerName = ownerName;
  }
}
