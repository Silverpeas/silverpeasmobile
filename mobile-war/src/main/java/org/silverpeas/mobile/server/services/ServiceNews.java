/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

import org.fusesource.restygwt.client.MethodCallback;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.components.quickinfo.model.QuickInfoService;
import org.silverpeas.components.quickinfo.model.QuickInfoServiceProvider;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.mobile.server.services.helpers.NewsHelper;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.List;

@WebService
@Authorized
@Path(ServiceNews.PATH + "/{appId}")
public class ServiceNews extends AbstractRestWebService {

  @Context
  HttpServletRequest request;

  @PathParam("appId")
  private String componentId;

  static final String PATH = "mobile/news";

  private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
  private OrganizationController organizationController = OrganizationController.get();

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("create")
  public void createNews(NewsDTO news) {

    QuickInfoService service = QuickInfoServiceProvider.getQuickInfoService();
    News n = News.builder().build();
    n.setTitle(news.getTitle());
    n.setImportant(news.getImportant());
    n.setDescription(news.getDescription());
    n.setContentToStore(news.getContent());
    n.setComponentInstanceId(componentId);
    n.setCreatorId(getUser().getId());
    service.create(n);

    //TODO : test
  }

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
