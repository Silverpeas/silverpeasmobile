package org.silverpeas.mobile.server.helpers;

import org.silverpeas.core.contribution.template.publication.PublicationTemplate;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateManager;
import org.silverpeas.core.web.mvc.controller.MainSessionController;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.mobile.server.services.AbstractAuthenticateService;

import javax.servlet.http.HttpServletRequest;

public class FormXMLHelper {

    public static final String MAINSESSIONCONTROLLER_ATTRIBUT_NAME = AbstractAuthenticateService.MAINSESSIONCONTROLLER_ATTRIBUT_NAME;

    public static PublicationTemplate getXMLTemplate(HttpServletRequest request, String instanceId)
            throws Exception {
        registerXMLForm(request, instanceId);
        return PublicationTemplateManager.getInstance().getPublicationTemplate(
                instanceId + ":" + getUsedXMLTemplateShortname(request, instanceId));
    }

    public static void registerXMLForm(HttpServletRequest request, String instanceId) throws Exception {
        if (isXMLTemplateUsed(request, instanceId)) {
            // register xmlForm to component

            PublicationTemplateManager.getInstance().addDynamicPublicationTemplate(
                    instanceId + ":" + getUsedXMLTemplateShortname(request, instanceId),
                    getUsedXMLTemplate(request, instanceId));
            getUsedXMLTemplate(request, instanceId);
        }
    }

    public static boolean isXMLTemplateUsed(HttpServletRequest request, String instanceId) throws Exception {
        return StringUtil.isDefined(getUsedXMLTemplate(request, instanceId));
    }

    public static String getUsedXMLTemplate(HttpServletRequest request, String instanceId)
            throws Exception {
        return getMainSessionController(request).getComponentParameterValue(instanceId, "xmlTemplate");
    }

    public static String getUsedXMLTemplateShortname(HttpServletRequest request, String instanceId)
            throws Exception {
        String xmlFormName = getUsedXMLTemplate(request, instanceId);
        return xmlFormName.substring(xmlFormName.indexOf('/') + 1, xmlFormName.indexOf('.'));
    }

    protected static MainSessionController getMainSessionController(HttpServletRequest request) throws Exception {
        MainSessionController mainSessionController = (MainSessionController) request.getSession().getAttribute(MAINSESSIONCONTROLLER_ATTRIBUT_NAME);
        return mainSessionController;
    }

}
