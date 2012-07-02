package com.silverpeas.mobile.client.common;

import com.silverpeas.mobile.client.common.event.ErrorEventHandler;
import com.silverpeas.mobile.client.common.event.ExceptionEvent;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

public class ErrorManager implements ErrorEventHandler {

	public void onError(ExceptionEvent event) {
		String message = "Erreur système";
		if (event.getException() instanceof AuthenticationException) {
			//TODO : afficher le bon message d'erreur
			message = "Mot de passe incorrect";			
		} else {
			message = event.getException().getMessage();
		}	
		
		Notification.activityStop();
		Notification.progressStop();
		
		// Affichage du message
		Notification.alert(message, null, "Erreur", "OK");
	}

}
