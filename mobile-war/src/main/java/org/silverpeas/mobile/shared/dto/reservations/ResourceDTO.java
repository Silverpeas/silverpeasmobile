package org.silverpeas.mobile.shared.dto.reservations;

import jsinterop.base.JsPropertyMap;

import java.io.Serializable;

/**
 * @author svu
 */
public class ResourceDTO implements Serializable {
  private String id;
  private String name;
  private String categoryId;
  private String categoryName;
  private String description;
  private String reservationStatus;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(final String categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(final String categoryName) {
    this.categoryName = categoryName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getReservationStatus() {
    return reservationStatus;
  }

  public void setReservationStatus(final String reservationStatus) {
    this.reservationStatus = reservationStatus;
  }

  public static ResourceDTO fromJSON(JsPropertyMap<Object> json) {
    ResourceDTO dto = new ResourceDTO();

    dto.setId(json.get("id") != null ? json.get("id").toString() : null);
    dto.setName(json.get("name") != null ? json.get("name").toString() : null);
    dto.setCategoryId(json.get("categoryId") != null ? json.get("categoryId").toString() : null);
    dto.setCategoryName(json.get("categoryName") != null ? json.get("categoryName").toString() : null);
    dto.setDescription(json.get("description") != null ? json.get("description").toString() : null);
    dto.setReservationStatus(json.get("reservationStatus") != null ? json.get("reservationStatus").toString() : null);

    return dto;
  }

  public JsPropertyMap<Object> toJSON() {
    JsPropertyMap<Object> json = JsPropertyMap.of();
    json.set("id", getId());
    json.set("name", getName());
    json.set("categoryId", getCategoryId());
    json.set("categoryName", getCategoryName());
    json.set("description", getDescription());
    json.set("reservationStatus", getReservationStatus());
    return json;
  }
}
