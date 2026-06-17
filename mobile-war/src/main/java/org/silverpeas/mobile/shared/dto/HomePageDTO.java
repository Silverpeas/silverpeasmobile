/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.shared.dto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;
import org.silverpeas.mobile.shared.dto.navigation.aurora.AuroraSpaceHomePageConfig;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomePageDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 5388415881024885835L;

    private String spaceName;

    private AuroraSpaceHomePageConfig auroraConfig;

    private List<SilverpeasObjectDTO> spacesAndApps;
    private List<NewsDTO> news = new ArrayList<NewsDTO>();
    private List<MyLinkDTO> favorites = new ArrayList<MyLinkDTO>();
    private List<PublicationDTO> lastPublications = new ArrayList<PublicationDTO>();
    private List<CalendarEventDTO> lastEvents = new ArrayList<CalendarEventDTO>();
    private List<ShortCutLinkDTO> shortCuts = new ArrayList<ShortCutLinkDTO>();
    private List<ShortCutLinkDTO> tools = new ArrayList<ShortCutLinkDTO>();
    private String htmlFreeZone;
    private String newsDisplayer;

    public final static String NEWS_DISPLAYER_CARROUSEL = "carrousel";
    public final static String NEWS_DISPLAYER_LIST = "list";

    public String getHtmlFreeZoneThin() {
        return htmlFreeZoneThin;
    }

    public void setHtmlFreeZoneThin(final String htmlFreeZoneThin) {
        this.htmlFreeZoneThin = htmlFreeZoneThin;
    }

    private String htmlFreeZoneThin;

    public List<PublicationDTO> getLastPublications() {
        return lastPublications;
    }

    public void setLastPublications(final List<PublicationDTO> lastPublications) {
        this.lastPublications = lastPublications;
    }

    public List<NewsDTO> getNews() {
        return news;
    }

    public void setNews(final List<NewsDTO> news) {
        this.news = news;
    }

    public List<MyLinkDTO> getFavorites() {
        return favorites;
    }

    public void setFavorites(final List<MyLinkDTO> favorites) {
        this.favorites = favorites;
    }

    public List<SilverpeasObjectDTO> getSpacesAndApps() {
        return spacesAndApps;
    }

    public void setSpacesAndApps(final List<SilverpeasObjectDTO> spacesAndApps) {
        this.spacesAndApps = spacesAndApps;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(final String spaceName) {
        this.spaceName = spaceName;
    }

    public void setLastEvents(final List<CalendarEventDTO> lastEvents) {
        this.lastEvents = lastEvents;
    }

    public List<CalendarEventDTO> getLastEvents() {
        return lastEvents;
    }

    public List<ShortCutLinkDTO> getShortCuts() {
        return shortCuts;
    }

    public void setShortCuts(final List<ShortCutLinkDTO> shortCuts) {
        this.shortCuts = shortCuts;
    }

    public String getHtmlFreeZone() {
        return htmlFreeZone;
    }

    public void setHtmlFreeZone(final String htmlFreeZone) {
        this.htmlFreeZone = htmlFreeZone;
    }

    public List<ShortCutLinkDTO> getTools() {
        return tools;
    }

    public void setTools(final List<ShortCutLinkDTO> tools) {
        this.tools = tools;
    }

    public String getNewsDisplayer() {
        return newsDisplayer;
    }

    public void setNewsDisplayer(final String newsDisplayer) {
        this.newsDisplayer = newsDisplayer;
    }

    public AuroraSpaceHomePageConfig getAuroraConfig() {
        return auroraConfig;
    }

    public void setAuroraConfig(AuroraSpaceHomePageConfig auroraConfig) {
        this.auroraConfig = auroraConfig;
    }

    public static HomePageDTO fromJSON(JsPropertyMap<Object> json) {
        HomePageDTO dto = new HomePageDTO();

        if (json == null) {
            return dto;
        }
        try {
            dto.setSpaceName(json.get("spaceName") != null ? json.get("spaceName").toString() : "");

            dto.setHtmlFreeZone(json.get("htmlFreeZone") != null ? json.get("htmlFreeZone").toString() : "");

            dto.setHtmlFreeZoneThin(json.get("htmlFreeZoneThin") != null ? json.get("htmlFreeZoneThin").toString() : "");

            dto.setNewsDisplayer(json.get("newsDisplayer") != null ? json.get("newsDisplayer").toString() : "");

            // ===== aurora config =====
            if (json.get("auroraConfig") != null) {
                dto.setAuroraConfig(AuroraSpaceHomePageConfig.fromJSON((JsPropertyMap<Object>) json.get("auroraConfig")));
            }

            // ===== spaces and apps =====
            if (json.get("spacesAndApps") != null) {
                JsArray<Object> list = (JsArray<Object>) json.get("spacesAndApps");
                List<SilverpeasObjectDTO> result = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                    Object type = map.get("className");
                    if (type != null && type.toString().contains(SpaceDTO.class.getSimpleName())) {
                        result.add(SpaceDTO.fromJSON(map));
                    } else {
                        result.add(ApplicationInstanceDTO.fromJSON(map));
                    }
                }
                dto.setSpacesAndApps(result);
            }

            // ===== news =====
            if (json.get("news") != null) {
                List<NewsDTO> result = new ArrayList<>();
                JsArray<Object> list = (JsArray<Object>) json.get("news");
                for (int i = 0; i < list.length; i++) {
                    JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                    result.add(NewsDTO.fromJSON(map));
                }
                dto.setNews(result);
            }


            // ===== favorites =====
            if (json.get("favorites") != null) {
                List<MyLinkDTO> result = new ArrayList<>();
                JsArray<Object> list = (JsArray<Object>) json.get("favorites");
                for (int i = 0; i < list.length; i++) {
                    JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                    result.add(MyLinkDTO.fromJSON(map));
                }
                dto.setFavorites(result);
            }

            // ===== last publications =====
            if (json.get("lastPublications") != null) {
                JsArray<Object> list = (JsArray<Object>) json.get("lastPublications");

                List<PublicationDTO> result = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                    result.add(PublicationDTO.fromJSON(map));
                }

                dto.setLastPublications(result);
            }

            // ===== last events =====
            if (json.get("lastEvents") != null) {
                JsArray<Object> list = (JsArray<Object>) json.get("lastEvents");
                List<CalendarEventDTO> result = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                    result.add(CalendarEventDTO.fromJSON(map));
                }

                dto.setLastEvents(result);
            }

            // ===== shortcuts =====
            if (json.get("shortCuts") != null) {
                JsArray<Object> list = (JsArray<Object>) json.get("shortCuts");
                List<ShortCutLinkDTO> result = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                    result.add(ShortCutLinkDTO.fromJSON(map));
                }
                dto.setShortCuts(result);
            } else {
                dto.setShortCuts(new ArrayList<>());
            }

            // ===== tools =====
            if (json.get("tools") != null) {
                JsArray<Object> list = (JsArray<Object>) json.get("tools");
                List<ShortCutLinkDTO> result = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                    result.add(ShortCutLinkDTO.fromJSON(map));
                }
                dto.setTools(result);
            } else {
                dto.setTools(new ArrayList<>());
            }

        } catch (Throwable t) {
            GWT.log("ERROR", t);
        }

        return dto;
    }
}
