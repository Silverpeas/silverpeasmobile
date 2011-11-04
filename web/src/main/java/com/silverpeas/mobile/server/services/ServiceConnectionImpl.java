package com.silverpeas.mobile.server.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;

public class ServiceConnectionImpl extends RemoteServiceServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	
	public void connection(String login, String password) throws AuthenticationException{
		HttpSession session = request.getSession(true); //création de la session
		//connexion
		//session.setAttribute("IdUser", idUser);
	}

}
