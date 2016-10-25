package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.exceptions.AlmanachException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.services.ServiceAlmanach;
import org.silverpeas.components.almanach.model.EventDetail;
import org.silverpeas.components.almanach.model.EventPK;
import org.silverpeas.components.almanach.service.AlmanachService;
import org.silverpeas.core.util.logging.SilverLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServiceAlmanachImpl extends AbstractAuthenticateService implements ServiceAlmanach {

  private static final long serialVersionUID = 1L;

  public List<EventDetailDTO> getAlmanach(String instanceId) throws AlmanachException, AuthenticationException {
    checkUserInSession();
    List<EventDetailDTO> listEventDetailDTO = new ArrayList<>();

    try {
      EventPK eventPK = new EventPK("");
      eventPK.setComponentName(instanceId);
      Collection<EventDetail> listEventDetail = AlmanachService.get().getAllEvents(eventPK);

      if (!listEventDetail.isEmpty()) {
        for (EventDetail eventDetail : listEventDetail) {
          listEventDetailDTO.add(populate(eventDetail));
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceAlmanachImpl.getAlmanach", e);
      throw new AlmanachException(e.getMessage());
    }
    return listEventDetailDTO;
  }

  private EventDetailDTO populate(EventDetail event) {
    EventDetailDTO dto = new EventDetailDTO();
    dto.setTitle(event.getTitle());
    dto.set_name(event.getName());
    dto.setEndDate(event.getEndDate());
    dto.setEndHour(event.getEndHour());
    dto.setEventUrl(event.getEventUrl());
    dto.setStartDate(event.getStartDate());
    dto.setStartHour(event.getEndHour());
    dto.setPlace(event.getPlace());
    dto.setPriority(event.getPriority());

    return dto;
  }
}