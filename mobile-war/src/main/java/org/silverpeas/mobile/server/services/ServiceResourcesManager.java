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

import org.silverpeas.components.classifieds.model.ClassifiedDetail;
import org.silverpeas.components.classifieds.notification.ClassifiedOwnerNotification;
import org.silverpeas.components.resourcesmanager.ResourcesManagerProvider;
import org.silverpeas.components.resourcesmanager.model.Reservation;
import org.silverpeas.components.resourcesmanager.model.Resource;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.notification.user.builder.helper.UserNotificationHelper;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.web.rs.RESTWebService;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.reservations.ReservationDTO;
import org.silverpeas.mobile.shared.dto.reservations.ResourceDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

  static final String PATH = "resourcesManager";

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("resources/available/{startDate}/{endDate}")
  public List<ResourceDTO> getAvailableResources(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {
    List<ResourceDTO> resources = new ArrayList<>();
    try {
      Date start = sdf.parse(startDate.replace("T", " "));
      Date end = sdf.parse(endDate.replace("T", " "));
      List<Resource> availablesResources = ResourcesManagerProvider.getResourcesManager().getResourcesReservable(componentId, start, end);
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

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("saveReservation/{reservation}")
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

      ResourcesManagerProvider.getResourcesManager().saveReservation(reservation, resources);

      //TODO : envoi d'une notification pour validation aux responsables des ressources selectionnées.
      // voir sendNotificationForValidation de ResourcesManagerSessionController

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
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
