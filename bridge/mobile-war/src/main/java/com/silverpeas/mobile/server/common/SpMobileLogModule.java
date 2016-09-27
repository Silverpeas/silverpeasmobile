package com.silverpeas.mobile.server.common;

import com.silverpeas.mobile.server.config.Configurator;

public class SpMobileLogModule {
	
	public static String getName() {
		return Configurator.getConfigValue("log.module.name");
	}
}
