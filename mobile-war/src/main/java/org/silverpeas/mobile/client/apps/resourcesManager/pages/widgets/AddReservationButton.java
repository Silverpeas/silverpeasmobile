package org.silverpeas.mobile.client.apps.resourcesManager.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.resourcesManager.pages.ReservationPage;
import org.silverpeas.mobile.client.apps.resourcesManager.resources.ResourcesManagerMessages;
import org.silverpeas.mobile.client.components.base.ActionItem;
import org.silverpeas.mobile.client.components.base.ActionsMenu;

/**
 * @author svu
 */
public class AddReservationButton extends ActionItem {
  interface AddReservationButtonUiBinder extends UiBinder<HTMLPanel, AddReservationButton> {
  }

  private static AddReservationButton.AddReservationButtonUiBinder
      uiBinder = GWT.create(AddReservationButton.AddReservationButtonUiBinder.class);

  @UiField
  HTMLPanel container;
  @UiField
  Anchor addReservation;

  @UiField(provided = true) protected ResourcesManagerMessages msg = null;
  private String instanceId, contentId, contentType, title;


  public AddReservationButton() {
    msg = GWT.create(ResourcesManagerMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    setId("addReservation");
  }

  @UiHandler("addReservation")
  void displayReservationPage(ClickEvent event){

    ReservationPage page = new ReservationPage();
    page.setPageTitle(msg.newReservation());
    page.show();

    // hide menu
    ActionsMenu.close(getElement());
  }
}
