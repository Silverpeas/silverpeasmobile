package org.silverpeas.mobile.shared.helpers;

import com.google.gwt.user.client.Window;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class ApplicationsHelper {

    public static boolean isSupportedApp(ApplicationInstanceDTO app) {
        String appSupported = ResourcesManager.getParam("apps.supported");
        String apps[] = appSupported.split(",");
        for (String ap : apps) {
            if (app.getType().equalsIgnoreCase(ap.trim())) return true;
        }
        return false;
    }

}
