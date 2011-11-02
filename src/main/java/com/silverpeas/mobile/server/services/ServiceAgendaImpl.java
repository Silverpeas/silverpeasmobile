package com.silverpeas.mobile.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.services.ServiceAgenda;

public class ServiceAgendaImpl extends RemoteServiceServlet implements ServiceAgenda{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void viewAgenda() throws AuthenticationException{
		getThreadLocalRequest().getSession().getAttribute("Id");
	}
}
