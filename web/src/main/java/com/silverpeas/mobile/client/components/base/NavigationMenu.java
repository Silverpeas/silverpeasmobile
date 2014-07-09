package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class NavigationMenu extends Composite {

	private static NavigationMenuUiBinder uiBinder = GWT.create(NavigationMenuUiBinder.class);

	@UiField HTMLPanel container, user;
	@UiField Anchor home, disconnect;
	
	@UiField(provided = true) protected ApplicationMessages msg = null;

	interface NavigationMenuUiBinder extends UiBinder<Widget, NavigationMenu> {
	}

	public NavigationMenu() {
		msg = GWT.create(ApplicationMessages.class);
		initWidget(uiBinder.createAndBindUi(this));
		container.getElement().setId("silverpeas-navmenu-panel");
		container.getElement().getStyle().setHeight(Window.getClientHeight(), Unit.PX);
		user.getElement().setId("user");				
	}

	public void toogleMenu() {
		if (container.getStyleName().contains("ui-panel-close")) {
			container.removeStyleName("ui-panel-close");
			container.addStyleName("ui-panel-open");
		} else {
			closeMenu();
		}
	}
	
	public void closeMenu() {
		container.removeStyleName("ui-panel-open");
		container.addStyleName("ui-panel-close");
	}
	
	@UiHandler("home")
	protected void goHome(ClickEvent event) {		
		PageHistory.getInstance().goToFirst();
		closeMenu();
	}
	
	@UiHandler("disconnect")
	protected void disconnect(ClickEvent event) {		
		closeMenu();
		SpMobil.clearIds();
		PageHistory.getInstance().clear();
		ConnexionPage connexionPage = new ConnexionPage();
		RootPanel.get().clear();
		RootPanel.get().add(connexionPage);		
	}
	
	public void setUser(DetailUserDTO currentUuser) {
		user.add(new Image(currentUuser.getAvatar()));
		user.add(new InlineHTML(" " + currentUuser.getFirstName() + " " + currentUuser.getLastName()));
	}
}
