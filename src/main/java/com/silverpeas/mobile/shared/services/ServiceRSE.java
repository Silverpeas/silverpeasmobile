package com.silverpeas.mobile.shared.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rse")
public interface ServiceRSE extends RemoteService {
	public void updateStatus(String status);
	
	public static final class App {
		 
        private static ServiceRSEAsync app = null;
 
        private App() {
          }
 
        public static synchronized ServiceRSEAsync getInstance() {
 
            if (app == null) {
                app = (ServiceRSEAsync) GWT.create(ServiceRSE.class);
            }
            return app;
        }
    }
}
