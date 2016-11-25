package com.silverpeas.mobile.client.common.gwt;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.client.common.gwt.impl.SuperDevModeHelper;

public class SuperDevModeUtil {

	private static final SuperDevModeHelper HELPER = GWT.create(SuperDevModeHelper.class);

	public static void showDevMode() {
		HELPER.showDevMode();
	}
}
