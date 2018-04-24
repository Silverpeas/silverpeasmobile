package org.silverpeas.mobile.client.apps.agenda.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AbstractAgendaPagesEvent;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;

/**
 * @author svu
 */
public class EventPage  extends PageContent {
  interface EventPageUiBinder extends UiBinder<Widget, EventPage> {}

  private static EventPageUiBinder uiBinder = GWT.create(EventPageUiBinder.class);

  @UiField(provided = true) protected AgendaMessages msg = null;

  @UiField
  ActionsMenu actionsMenu;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  public EventPage() {
    msg = GWT.create(AgendaMessages.class);
    setPageTitle(msg.titleEvent());
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(CalendarEventDTO event) {
    event.getTitle();
    event.getDescription();
    //TODO
  }
}