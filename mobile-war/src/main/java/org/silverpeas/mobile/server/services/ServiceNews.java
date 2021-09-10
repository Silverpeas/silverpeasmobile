/*
 * Copyright (C) 2000 - 2021 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
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

import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.webapi.base.RESTWebService;
import org.silverpeas.core.webapi.base.annotation.Authorized;
import org.silverpeas.mobile.server.services.helpers.NewsHelper;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.List;

/*
 * Copyright (C) 2000 - 2021 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
@WebService
@Authorized
@Path(ServiceNews.PATH + "/{appId}")
public class ServiceNews extends RESTWebService {

  @Context
  HttpServletRequest request;

  @PathParam("appId")
  private String componentId;

  static final String PATH = "news";

  private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
  private OrganizationController organizationController = OrganizationController.get();


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("all")
  public List<NewsDTO> getNews() {

    String[] profiles =
        OrganizationController.get().getUserProfiles(getUser().getId(), componentId);
    boolean managerAccess = isManagerOrPublisher(profiles);
    List<News> news = NewsHelper.getInstance().getNewsByAppId(componentId, managerAccess);
    List<NewsDTO> newsDTO = NewsHelper.getInstance().populate(news, managerAccess);
    return newsDTO;
  }

  private boolean isManagerOrPublisher(final String[] profiles) {
    for (String profile : profiles) {
      if (profile.equals("admin")) {
        return true;
      }
      if (profile.equals("publisher")) {
        return true;
      }
    }
    return false;
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
