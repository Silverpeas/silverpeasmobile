package com.silverpeas.mobile.client.apps.documents;

import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.documents.events.app.internal.AbstractDocumentsEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.internal.DocumentsEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.app.internal.DocumentsStopEvent;
import com.silverpeas.mobile.client.apps.documents.pages.DocumentsPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.components.base.PageContent;

public class DocumentsApp extends App implements DocumentsEventHandler {
	
	public DocumentsApp() {
		super();
		EventBus.getInstance().addHandler(AbstractDocumentsEvent.TYPE, this);
	}
	
	@Override
	public void start(PageContent lauchingPage) {
		setController(new DocumentsController());
		setMainPage(new DocumentsPage());
		super.start(lauchingPage);
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractDocumentsEvent.TYPE, this);
		super.stop();
	}

	@Override
	public void onStop(DocumentsStopEvent event) {
		stop();		
	}
}
