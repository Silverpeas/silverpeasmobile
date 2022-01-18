package org.silverpeas.mobile.shared.dto.reservations;

import java.io.Serializable;
import java.util.List;

/**
 * @author svu
 */
public class ReservationDTO implements Serializable {
  private String evenement;
  private String startDate;
  private String endDate;
  private String reason;

  private String status;

  private List<ResourceDTO> resources;

  public String getEvenement() {
    return evenement;
  }

  public void setEvenement(final String evenement) {
    this.evenement = evenement;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(final String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(final String endDate) {
    this.endDate = endDate;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(final String reason) {
    this.reason = reason;
  }

  public List<ResourceDTO> getResources() {
    return resources;
  }

  public void setResources(final List<ResourceDTO> resources) {
    this.resources = resources;
  }

  public String getStatus() { return status; }

  public void setStatus(final String status) { this.status = status; }
}
