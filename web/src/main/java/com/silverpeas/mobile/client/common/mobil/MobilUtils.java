package com.silverpeas.mobile.client.common.mobil;

import com.google.gwt.user.client.Window;
import com.gwtmobile.phonegap.client.Device;
import com.gwtmobile.ui.client.utils.Utils;

public class MobilUtils {
	public static Orientation getOrientation() {
		if (Window.getClientHeight() > Window.getClientWidth()) {
			return Orientation.Portrait;
		} else {
			return Orientation.Landscape;
		}
	}
	
	public static boolean isPhoneGap() {
    	try {
    		Device.getPhoneGap();
        	return true;
    	} catch (Exception e) {
			return false;
		}
    }
    
    public static boolean isMobil() {
    	return Utils.isAndroid() || Utils.isIOS();
    }
}
