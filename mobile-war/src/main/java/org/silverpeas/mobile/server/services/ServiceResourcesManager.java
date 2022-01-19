/*
 * Copyright (C) 2000 - 2021 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.server.services;

import org.silverpeas.components.resourcesmanager.ResourcesManagerProvider;
import org.silverpeas.components.resourcesmanager.model.Reservation;
import org.silverpeas.components.resourcesmanager.model.Resource;
import org.silverpeas.components.resourcesmanager.model.ResourceStatus;
import org.silverpeas.components.resourcesmanager.model.ResourceValidator;
import org.silverpeas.components.resourcesmanager.service.ReservationService;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.notification.NotificationException;
import org.silverpeas.core.notification.user.client.NotificationMetaData;
import org.silverpeas.core.notification.user.client.NotificationParameters;
import org.silverpeas.core.notification.user.client.NotificationSender;
import org.silverpeas.core.notification.user.client.UserRecipient;
import org.silverpeas.core.ui.DisplayI18NHelper;
import org.silverpeas.core.util.DateUtil;
import org.silverpeas.core.util.Link;
import org.silverpeas.core.util.LocalizationBundle;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.util.URLUtil;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.web.rs.RESTWebService;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.reservations.ReservationDTO;
import org.silverpeas.mobile.shared.dto.reservations.ResourceDTO;
import org.silverpeas.mobile.shared.dto.reservations.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebService
@Authorized
@Path(ServiceResourcesManager.PATH + "/{appId}")
public class ServiceResourcesManager extends RESTWebService {

  @Context
  HttpServletRequest request;

  @PathParam("appId")
  private String componentId;

  private NotificationSender notifSender;

  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

  static final String PATH = "resourcesManager";

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("resources/checkdates/{startDate}/{endDate}")
  public String checkDates(@PathParam("startDate") String startDate,
      @PathParam("endDate") String endDate) {
    try {
      Date start = sdf.parse(startDate.replace("T", " "));
      Date end = sdf.parse(endDate.replace("T", " "));
      Date now = Calendar.getInstance().getTime();
      if (end.before(start)) {
        return Errors.dateOrder.toString();
      } else if (start.before(now)) {
        return Errors.earlierDate.toString();
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return "";
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("resources/available/{startDate}/{endDate}")
  public List<ResourceDTO> getAvailableResources(@PathParam("startDate") String startDate,
      @PathParam("endDate") String endDate) {
    List<ResourceDTO> resources = new ArrayList<>();
    try {
      Date start = sdf.parse(startDate.replace("T", " "));
      Date end = sdf.parse(endDate.replace("T", " "));
      List<Resource> availablesResources = ResourcesManagerProvider.getResourcesManager()
          .getResourcesReservable(componentId, start, end);
      for (Resource res : availablesResources) {
        ResourceDTO dto = new ResourceDTO();
        dto.setId(res.getId());
        dto.setName(res.getName());
        dto.setCategoryId(res.getCategory().getId());
        dto.setCategoryName(res.getCategory().getName());
        dto.setDescription(res.getDescription());
        resources.add(dto);
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return resources;
  }

  //TODO : delete reservation
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/reservation")
  public void deleteReservation(ReservationDTO reservation) {
    ResourcesManagerProvider.getResourcesManager().deleteReservation(Long.parseLong(reservation.getId()), componentId);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/reservations/my")
  public List<ReservationDTO> getMyReservations() {
    List<ReservationDTO> reservations = new ArrayList<>();
    try {
      ReservationService service = ServiceProvider.getService(ReservationService.class);
      String startPeriod = String.valueOf(Calendar.getInstance().getTime().getTime());
      String endPeriod = String.valueOf(DateUtil.MAXIMUM_DATE.getTime());

      List<Reservation> reservationList =
          service.findAllReservationsInRange(componentId, Integer.parseInt(getUser().getId()),
              startPeriod, endPeriod);
      for (Reservation reserv : reservationList) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reserv.getId());
        dto.setEvenement(reserv.getEvent());
        dto.setStartDate(sdf.format(reserv.getBeginDate()));
        dto.setEndDate(sdf.format(reserv.getEndDate()));
        dto.setReason(reserv.getReason());
        dto.setStatus(reserv.getStatus());
        List<Resource> resources = ResourcesManagerProvider.getResourcesManager().getResourcesOfReservation(componentId, reserv.getIdAsLong());
        List<ResourceDTO> resourcesBooked = new ArrayList();
        for (Resource res : resources) {
          ResourceDTO resourceDTO = new ResourceDTO();
          String status = ResourcesManagerProvider.getResourcesManager().getResourceOfReservationStatus(res.getIdAsLong(), reserv.getIdAsLong());
          resourceDTO.setReservationStatus(status);
          resourceDTO.setName(res.getName());
          resourceDTO.setId(res.getId());
          resourceDTO.setDescription(res.getDescription());
          resourceDTO.setCategoryId(String.valueOf(res.getCategoryId()));
          resourcesBooked.add(resourceDTO);
        }
        dto.setResources(resourcesBooked);
        reservations.add(dto);
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return reservations;
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("saveReservation")
  public void saveReservation(ReservationDTO dto) {
    try {
      Reservation reservation = new Reservation();
      reservation.setEvent(dto.getEvenement());
      Date start = sdf.parse(dto.getStartDate().replace("T", " "));
      Date end = sdf.parse(dto.getEndDate().replace("T", " "));
      reservation.setBeginDate(start);
      reservation.setEndDate(end);
      reservation.setReason(dto.getReason());
      List<Long> resources = new ArrayList<>();
      for (ResourceDTO res : dto.getResources()) {
        resources.add(Long.parseLong(res.getId()));
      }
      reservation.setInstanceId(componentId);
      reservation.setUserId(getUser().getId());
      reservation.setPlace("");

      ResourcesManagerProvider.getResourcesManager().saveReservation(reservation, resources);

      // envoi d'une notification pour validation aux responsables des ressources selectionnées.
      for (Long resourceId : resources) {
        sendNotificationForValidation(resourceId, reservation.getIdAsLong());
      }

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
  }

  private void sendNotificationForValidation(Long resourceId, Long reservationId)
      throws NotificationException {
    Resource resource = getResource(resourceId);
    String status = ResourcesManagerProvider.getResourcesManager()
        .getResourceOfReservationStatus(resourceId, reservationId);
    if (ResourceStatus.STATUS_FOR_VALIDATION.equals(status)) {
      // envoyer une notification aux responsables de la ressource
      String user = getUser().getDisplayedName();

      LocalizationBundle message = ResourceLocator.getLocalizationBundle(
          "org.silverpeas.resourcesmanager.multilang.resourcesManagerBundle",
          DisplayI18NHelper.getDefaultLanguage());

      StringBuilder messageBody = new StringBuilder();

      // liste des responsables (de la ressource) à notifier
      List<ResourceValidator> validators =
          ResourcesManagerProvider.getResourcesManager().getManagers(resource.getIdAsLong());
      List<UserRecipient> managers = new ArrayList<UserRecipient>(validators.size());
      if (!ResourcesManagerProvider.getResourcesManager()
          .isManager(Long.parseLong(getUser().getId()), resourceId)) {
        // envoie de la notification seulement si le user courant n'est pas aussi responsable
        for (ResourceValidator validator : validators) {
          managers.add(new UserRecipient(String.valueOf(validator.getManagerId())));
        }
        String url = URLUtil.getURL(null, getComponentId()) + "ViewReservation?reservationId=" +
            reservationId;

        String subject = message.getString("resourcesManager.notifSubject");
        messageBody = messageBody.append(user).append(" ")
            .append(message.getString("resourcesManager.notifBody")).append(" '")
            .append(resource.getName()).append("'");

        NotificationMetaData notifMetaData =
            new NotificationMetaData(NotificationParameters.PRIORITY_NORMAL, subject,
                messageBody.toString());

        for (String language : DisplayI18NHelper.getLanguages()) {
          message = ResourceLocator.getLocalizationBundle(
              "org.silverpeas.resourcesmanager.multilang.resourcesManagerBundle", language);
          subject = message.getString("resourcesManager.notifSubject");
          messageBody = new StringBuilder();
          messageBody = messageBody.append(user).append(" ")
              .append(message.getString("resourcesManager.notifBody")).append(" '")
              .append(resource.getName()).append("'.");
          notifMetaData.addLanguage(language, subject, messageBody.toString());

          Link link =
              new Link(url, message.getString("resourcesManager.notifReservationLinkLabel"));
          notifMetaData.setLink(link, language);
        }

        notifMetaData.addUserRecipients(managers);
        setMetaData(notifMetaData);
        // 2. envoie de la notification aux responsables
        getNotificationSender().notifyUser(notifMetaData);
      }
    }
  }

  public Resource getResource(Long id) {
    return ResourcesManagerProvider.getResourcesManager().getResource(id);
  }

  public NotificationSender getNotificationSender() {
    if (notifSender == null) {
      notifSender = new NotificationSender(getComponentId());
    }
    return notifSender;
  }

  private void setMetaData(NotificationMetaData notifMetaData) {
    notifMetaData.setComponentId(getComponentId());
    notifMetaData.setSender(getUser().getId());
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return this.componentId;
  }

}
