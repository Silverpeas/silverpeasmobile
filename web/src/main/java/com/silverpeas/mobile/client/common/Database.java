package com.silverpeas.mobile.client.common;

import com.gwtmobile.persistence.client.Persistence;

public class Database {
	private static final String NAME = "SilverpeasMobile";
	private static final String DESC = "DB for Silverpeas Mobile";
	private static final int SIZE = 5 * 1024 * 1024;
	
	public static void open() {
		Persistence.connect(NAME, DESC, SIZE);
		Persistence.setAutoAdd(true);
	}
	
}
