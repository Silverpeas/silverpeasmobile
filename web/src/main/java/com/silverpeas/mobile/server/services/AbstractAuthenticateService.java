package com.silverpeas.mobile.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException.AuthenticationError;
import com.stratelia.webactiv.beans.admin.UserDetail;

@SuppressWarnings("serial")
public abstract class AbstractAuthenticateService extends RemoteServiceServlet {
	public static final String USER_ATTRIBUT_NAME = "user";

	protected void setUserInSession(UserDetail user) {
		getThreadLocalRequest().getSession().setAttribute(USER_ATTRIBUT_NAME, user);
	}
	
	protected UserDetail getUserInSession() {
		return (UserDetail) getThreadLocalRequest().getSession().getAttribute(USER_ATTRIBUT_NAME);
	}
	
	protected void checkUserInSession() throws AuthenticationException {
		if (getThreadLocalRequest().getSession().getAttribute(USER_ATTRIBUT_NAME) == null) {
			throw new AuthenticationException(AuthenticationError.NotAuthenticate);
		}
	}
}
