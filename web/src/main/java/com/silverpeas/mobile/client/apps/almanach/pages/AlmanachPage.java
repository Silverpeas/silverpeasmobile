package com.silverpeas.mobile.client.apps.almanach.pages;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.almanach.events.controller.AlmanachLoadEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AbstractAlmanachPagesEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachLoadedEvent;
import com.silverpeas.mobile.client.apps.almanach.events.pages.AlmanachPagesEventHandler;
import com.silverpeas.mobile.client.apps.almanach.pages.widgets.AlmanachWidget;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;

public class AlmanachPage extends Page implements AlmanachPagesEventHandler,
		View {

	private static AlmanachPageUiBinder uiBinder = GWT
			.create(AlmanachPageUiBinder.class);
	@UiField
	HorizontalPanel l;

	interface AlmanachPageUiBinder extends UiBinder<Widget, AlmanachPage> {
	}

	@SuppressWarnings("deprecation")
	public AlmanachPage() {
		initWidget(uiBinder.createAndBindUi(this));
		Date date = new Date();
		EventBus.getInstance()
				.addHandler(AbstractAlmanachPagesEvent.TYPE, this);
		EventBus.getInstance().fireEvent(new AlmanachLoadEvent(date.getMonth()));
	}

	@Override
	public void stop() {
		EventBus.getInstance().removeHandler(AbstractAlmanachPagesEvent.TYPE,
				this);
	}

	public void onAlmanachLoaded(AlmanachLoadedEvent event) {
		AlmanachWidget al = new AlmanachWidget(true, event.getListEventDetailDTO());
		l.add(al);
	}
}
