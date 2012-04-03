package com.silverpeas.mobile.client.common.mobil;

import com.google.gwt.user.client.Window;

public class MobilUtils {
	public static Orientation getOrientation() {
		if (Window.getClientHeight() > Window.getClientWidth()) {
			return Orientation.Portrait;
		} else {
			return Orientation.Landscape;
		}
	}
}
