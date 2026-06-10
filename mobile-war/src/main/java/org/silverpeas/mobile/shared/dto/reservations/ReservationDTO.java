package org.silverpeas.mobile.shared.dto.reservations;

import jsinterop.base.JsPropertyMap;

import java.io.Serializable;
import java.util.List;

/**
 * @author svu
 */
public class ReservationDTO implements Serializable {
    private String id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public static ReservationDTO fromJSON(JsPropertyMap<Object> json) {
        ReservationDTO dto = new ReservationDTO();

        dto.setId(json.get("id") != null ? json.get("id").toString() : null);
        dto.setEvenement(json.get("evenement") != null ? json.get("evenement").toString() : null);
        dto.setStartDate(json.get("startDate") != null ? json.get("startDate").toString() : null);
        dto.setEndDate(json.get("endDate") != null ? json.get("endDate").toString() : null);
        dto.setReason(json.get("reason") != null ? json.get("reason").toString() : null);
        dto.setStatus(json.get("status") != null ? json.get("status").toString() : null);

        Object resourcesObj = json.get("resources");
        if (resourcesObj != null) {
            JsPropertyMap<Object>[] resourcesJson = (JsPropertyMap<Object>[]) resourcesObj;
            List<ResourceDTO> resources = new java.util.ArrayList<>();
            for (JsPropertyMap<Object> resourceJson : resourcesJson) {
                resources.add(ResourceDTO.fromJSON(resourceJson));
            }
            dto.setResources(resources);
        }

        return dto;
    }

    public JsPropertyMap<Object> toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();
        json.set("id", getId());
        json.set("evenement", getEvenement());
        json.set("startDate", getStartDate());
        json.set("endDate", getEndDate());
        json.set("reason", getReason());
        json.set("status", getStatus());

        // resources
        if (getResources() != null) {
            JsPropertyMap<Object>[] resourcesJson =
                    new JsPropertyMap[getResources().size()];

            for (int i = 0; i < getResources().size(); i++) {
                resourcesJson[i] = getResources().get(i).toJSON();
            }

            json.set("resources", resourcesJson);
        }

        return json;
    }
}
