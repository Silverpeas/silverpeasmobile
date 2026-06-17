package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;

public class DEBUG {

    private static boolean isDebugMode() {
        String mode = LocalStorageHelper.getInstance().load("debug");
        if (mode != null && !mode.isEmpty()) {
            return Boolean.parseBoolean(mode);
        }
        return false;
    }

    public static void log(Object caller, String message) {
        if (isDebugMode()) GWT.log(caller.getClass().getSimpleName() + " " + message);
    }
}
