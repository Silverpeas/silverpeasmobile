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

package org.silverpeas.mobile.shared.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.silverpeas.mobile.shared.dto.blog.PostDTO;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedsDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.documents.TopicDTO;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.io.Serializable;

/**
 * @author: svu
 */
@JsonSubTypes({@JsonSubTypes.Type(value = UserDTO.class, name = "UserDTO"),
    @JsonSubTypes.Type(value = GroupDTO.class, name = "GroupeDTO"),
    @JsonSubTypes.Type(value = PostDTO.class, name = "PostDTO"),
    @JsonSubTypes.Type(value = StatusDTO.class, name = "StatusDTO"),
    @JsonSubTypes.Type(value = ClassifiedsDTO.class, name = "ClassifiedsDTO"),
    @JsonSubTypes.Type(value = ClassifiedDTO.class, name = "ClassifiedDTO"),
    @JsonSubTypes.Type(value = TaskDTO.class, name = "TaskDTO"),
    @JsonSubTypes.Type(value = NewsDTO.class, name = "NewsDTO"),
    @JsonSubTypes.Type(value = TopicDTO.class, name = "TopicDTO"),
    @JsonSubTypes.Type(value = PublicationDTO.class, name = "PublicationDTO"),
    @JsonSubTypes.Type(value = HomePageDTO.class, name = "HomePageDTO")})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class BaseDTO implements Serializable {

  private static final long serialVersionUID = 8186851689918190659L;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  private String id;

}
