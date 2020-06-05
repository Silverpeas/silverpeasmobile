/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

import org.silverpeas.core.annotation.RequestScoped;
import org.silverpeas.core.annotation.Service;
import org.silverpeas.core.security.authentication.AuthenticationCredential;
import org.silverpeas.core.security.authentication.exception.AuthenticationUserMustAcceptTermsOfService;
import org.silverpeas.core.security.authentication.verifier.AuthenticationUserVerifierFactory;
import org.silverpeas.core.template.SilverpeasTemplate;
import org.silverpeas.core.template.SilverpeasTemplateFactory;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.webapi.base.RESTWebService;
import org.silverpeas.core.webapi.base.UserPrivilegeValidation;
import org.silverpeas.core.webapi.base.annotation.Authorized;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/*
 * Copyright (C) 2000 - 2020 Silverpeas
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
@Service
@RequestScoped
@Authorized
@Path(ServiceTermsOfService.PATH)
public class ServiceTermsOfService extends RESTWebService {

  @Context
  HttpServletRequest request;

  static final String PATH = "termsOfService";

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("show")
  public Boolean isShow() {
    SettingBundle resource = ResourceLocator.getSettingBundle("org.silverpeas.authentication.settings.authenticationSettings");
    String frequency = resource.getString("termsOfServiceAcceptanceFrequency");

    try {
      frequency = resource.getString("termsOfServiceAcceptanceFrequency.domain" + getUser().getDomainId());
    } catch(Exception e) {
      SilverLogger.getLogger(this).debug("termsOfServiceAcceptanceFrequency.domain" + getUser().getDomainId() + " not found");
    }

    if (frequency.equalsIgnoreCase("NEVER")) {
      return false;
    } else if (frequency.equalsIgnoreCase("ALWAYS")) {
      return true;
    } else {
      try {
        AuthenticationCredential credential = AuthenticationCredential.newWithAsLogin(getUser().getLogin());
        credential.setDomainId(getUser().getDomainId());
        AuthenticationUserVerifierFactory.getUserMustAcceptTermsOfServiceVerifier(credential).verify();
      } catch (AuthenticationUserMustAcceptTermsOfService authenticationUserMustAcceptTermsOfService) {
        return true;
      }
    }
    return false;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("content")
  public String getContent() {
    SettingBundle resource = ResourceLocator.getSettingBundle("org.silverpeas.authentication.settings.authenticationSettings");
    Boolean specificTemplate = false;
    try {
      specificTemplate = resource.getBoolean("termsOfServiceAcceptanceSpecificTemplateContent.domain" + getUser().getDomainId());
    } catch(Exception e) {
      SilverLogger.getLogger(this).debug("termsOfServiceAcceptanceSpecificTemplateContent.domain" + getUser().getDomainId() + " not found");
    }
    String content = "";
    SilverpeasTemplate template = SilverpeasTemplateFactory.createSilverpeasTemplateOnCore("termsOfService");
    if (specificTemplate) {
      content = template.applyFileTemplate(
          "termsOfService_domain" + getUser().getDomainId() + "_" + getUser().getUserPreferences().getLanguage());
    } else {
      content = template.applyFileTemplate(
          "termsOfService_" + getUser().getUserPreferences().getLanguage());
    }

    return content;
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return null;
  }

  @Override
  public void validateUserAuthorization(final UserPrivilegeValidation validation) {
  }

}
