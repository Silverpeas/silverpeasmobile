/**
 * Copyright (C) 2000 - 2009 Silverpeas
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
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.oosphere.silverpeasmobile.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.silverpeas.socialNetwork.model.SocialInformation;
import com.silverpeas.socialNetwork.model.SocialInformationType;
import com.silverpeas.socialNetwork.myProfil.control.SocialNetworkService;
import com.silverpeas.util.EncodeHelper;
import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.silverpeas.peasCore.URLManager;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.util.ResourceLocator;

public class JsonDashboardServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doPost(req, res);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse res)
      throws ServletException, IOException {
    String userId = request.getParameter("userId");
    request.setAttribute("userId", userId);

    Calendar begin = Calendar.getInstance();
    begin.add(Calendar.MONTH, -1);
    Calendar end = Calendar.getInstance();
    Map<Date, List<SocialInformation>> map = new LinkedHashMap<Date, List<SocialInformation>>();
    map =
        getInformations("MyFeed", userId, SocialInformationType.ALL, "0", end.getTime(),
            begin.getTime());

    HttpSession session = request.getSession(false);
    MainSessionController m_MainSessionCtrl = (MainSessionController) session.getAttribute(
        MainSessionController.MAIN_SESSION_CONTROLLER_ATT);
    ResourceLocator multilang =
        new ResourceLocator("com.silverpeas.socialNetwork.multilang.socialNetworkBundle",
            m_MainSessionCtrl.getFavoriteLanguage());
    JSONArray newsArray = toJsonS(map, m_MainSessionCtrl.getOrganizationController(), multilang);
    
    res.setContentType("application/json");
    res.getWriter().write( newsArray.toString());
  }

  private Map<Date, List<SocialInformation>> getInformations(String view, String userId,
      SocialInformationType type, String anotherUserId, Date begin, Date end) {
    Map<Date, List<SocialInformation>> map = new LinkedHashMap<Date, List<SocialInformation>>();

    SocialNetworkService socialNetworkService = new SocialNetworkService(userId);

    if ("MyFeed".equals(view)) {
      // get all data from me and my contacts
      map = socialNetworkService.getSocialInformationOfMyContacts(type, begin, end);
    } else if ("MyContactWall".equals(view)) {
      // get all data from my contact
      map = socialNetworkService.getSocialInformationOfMyContact(anotherUserId, type, begin, end);
    } else {
      // get all data from me
      map = socialNetworkService.getSocialInformation(type, begin, end);
    }
    return map;
  }

  private JSONArray toJsonS(Map<Date, List<SocialInformation>> map,
      OrganizationController oc,
      ResourceLocator multilang) {
    SimpleDateFormat formatDate =
        new SimpleDateFormat("EEEE dd MMMM yyyy", new Locale(multilang.getLanguage()));
    JSONArray result = new JSONArray();
    for (Map.Entry<Date, List<SocialInformation>> entry : map.entrySet()) {
      JSONArray jsonArrayDateWithValues = new JSONArray();
      Object key = entry.getKey();

      JSONArray jsonArray = new JSONArray();
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("day", formatDate.format(key));
      List<SocialInformation> informations = entry.getValue();
      for (SocialInformation si : informations) {
        jsonArray.put(toJson(si, oc, multilang));
      }
      jsonArrayDateWithValues.put(jsonObject);
      jsonArrayDateWithValues.put(jsonArray);
      result.put(jsonArrayDateWithValues);
    }
    return result;
  }

  private JSONObject toJson(SocialInformation information, OrganizationController oc,
      ResourceLocator multilang) {
    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    JSONObject valueObj = new JSONObject();
    UserDetail contactUser1 = oc.getUserDetail(information.getAuthor());
    if (information.getType().equals(SocialInformationType.RELATIONSHIP.toString())) {
      UserDetail contactUser2 = oc.getUserDetail(information.getTitle());
      valueObj.put("type", information.getType());
      valueObj.put("author", userDetailToJSON(contactUser1));
      valueObj.put("title", userDetailToJSON(contactUser2));
      valueObj.put("hour", formatTime.format(information.getDate()));
      valueObj.put("url", URLManager.getApplicationURL() + information.getUrl());
      valueObj.put("icon", URLManager.getApplicationURL() + contactUser2.getAvatar());
      valueObj.put(
          "label",
          multilang.getStringWithParam("newsFeed.relationShip.label",
              contactUser2.getDisplayedName()));
      return valueObj;
    } else if (information.getType().endsWith(SocialInformationType.EVENT.toString())) {
      return eventSocialToJSON(information, contactUser1, multilang);
    }
    valueObj.put("type", information.getType());
    valueObj.put("author", userDetailToJSON(contactUser1));

    valueObj.put("description", EncodeHelper.javaStringToHtmlParagraphe(information
        .getDescription()));

    if (information.getType().equals(SocialInformationType.STATUS.toString())) {
      valueObj.put("title", multilang.getString("newsFeed.status.suffix"));
    } else {
      valueObj.put("title", information.getTitle());
    }
    // if time not identified display string empty
    if ("00:00".equalsIgnoreCase(formatTime.format(information.getDate()))) {
      valueObj.put("hour", "");
    } else {
      valueObj.put("hour", formatTime.format(information.getDate()));
    }
    valueObj.put("url", URLManager.getApplicationURL() + information.getUrl());
    valueObj.put("icon",
        getIconUrl(SocialInformationType.valueOf(information.getType())) + information.getIcon());
    valueObj.put("label", multilang.getString("newsFeed." + information.getType().toLowerCase() +
        ".updated." + information.isUpdeted()));

    return valueObj;
  }

  private JSONObject eventSocialToJSON(SocialInformation event, UserDetail contactUser,
      ResourceLocator multilang) {
    SimpleDateFormat formatTime =
        new SimpleDateFormat("HH:mm", new Locale(multilang.getLanguage()));
    JSONObject valueObj = new JSONObject();
    valueObj.put("type", event.getType());
    valueObj.put("author", userDetailToJSON(contactUser));
    valueObj.put("hour", formatTime.format(event.getDate()));
    valueObj.put("url", URLManager.getApplicationURL() + event.getUrl());
    valueObj.put("icon",
        getIconUrl(SocialInformationType.valueOf(event.getType())) + event.getIcon());
    if (!event.isUpdeted() && event.getIcon().startsWith(event.getType() + "_private")) {
      valueObj.put("title", multilang.getString("profil.icon.private.event"));
      valueObj.put("description", "");
    } else {
      valueObj.put("title", event.getTitle());
      valueObj.put("description", event.getDescription());
    }
    valueObj.put("label", multilang.getString("newsFeed." + event.getType().toLowerCase() +
        ".label"));

    return valueObj;
  }

  private JSONObject userDetailToJSON(UserDetail user) {
    JSONObject userJSON = new JSONObject();
    userJSON.put("id", user.getId());
    userJSON.put("displayedName", user.getDisplayedName());
    userJSON.put("profilPhoto", URLManager.getApplicationURL() + user.getAvatar());
    return userJSON;
  }

  private String getIconUrl(SocialInformationType type) {
    String url = URLManager.getApplicationURL() + "/socialNetwork/jsp/icons/";
    if (type.equals(SocialInformationType.PHOTO)) {
      url = URLManager.getApplicationURL();
    }
    return url;
  }

}
