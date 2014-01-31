package com.silverpeas.mobile.client.resources;

import com.google.gwt.i18n.client.Messages;


public interface ApplicationMessages extends Messages {
	@DefaultMessage("Confirmer")
	String confirmBtnLabel();
	@DefaultMessage("Annuler")
	String cancelBtnLabel();
	@DefaultMessage("Corriger")
	String correctBtnLabel();
	
	@DefaultMessage("Silverpeas&copy; 1999-2014")
	String copyright();
	
	@DefaultMessage("Information")
	String infoTitle();
	@DefaultMessage("OK")
	String ok();
}
