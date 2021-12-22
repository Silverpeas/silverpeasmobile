package org.silverpeas.mobile.shared.dto.reservations;

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
}
