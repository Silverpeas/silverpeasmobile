/*
 * Copyright (C) 2000 - 2022 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.apps.agenda.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.agenda.events.pages.*;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.userselection.UserSelectionPage;
import org.silverpeas.mobile.client.components.userselection.events.components.UserSelectionComponentEventHandler;
import org.silverpeas.mobile.client.components.userselection.events.components.UsersAndGroupsSelectedEvent;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

import java.util.List;

/**
 * @author svu
 */
public class EditEventPage extends PageContent implements EventPagesEventHandler, UserSelectionComponentEventHandler {
  private static EditEventPageUiBinder uiBinder = GWT.create(EditEventPageUiBinder.class);
  private ApplicationResources resources = GWT.create(ApplicationResources.class);

  @UiField(provided = true) protected AgendaMessages msg = null;

  @UiField
  HTMLPanel container;

  @UiField
  TextBox title, startDate, endDate;

  @UiField
  TextArea description;

  @UiField
  CheckBox allDay;

  @UiField
  RadioButton important, notimportant, publique, prive;

  @UiField
  ListBox frequency, calendars;

  @UiField
  Anchor submit, selectionButton;

  @UiField
  SpanElement selection;

  @Override
  public void onUsersAndGroupSelected(UsersAndGroupsSelectedEvent event) {
    //TODO
  }

  interface EditEventPageUiBinder extends UiBinder<Widget, EditEventPage> {
  }

  public EditEventPage() {
    msg = GWT.create(AgendaMessages.class);
    setPageTitle(msg.newtitleEvent());
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("eventForm");
    title.getElement().setAttribute("placeholder", "Titre");
    description.getElement().setAttribute("placeholder", "Description");
    description.getElement().setAttribute("rows", "6");
    startDate.getElement().setAttribute("type", "date");
    endDate.getElement().setAttribute("type", "date");
    important.setValue(true);
    publique.setValue(true);
    EventBus.getInstance().addHandler(AbstractEventPagesEvent.TYPE, this);
    allDay.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
        if (valueChangeEvent.getValue()) {
          startDate.getElement().setAttribute("type", "date");
          endDate.getElement().setAttribute("type", "date");
        } else {
          startDate.getElement().setAttribute("type", "datetime-local");
          endDate.getElement().setAttribute("type", "datetime-local");
        }
      }
    });

    frequency.addItem("Aucune", "string:NONE");
    frequency.addItem("Tous les jours", "string:DAY");
    frequency.addItem("Toutes les semaines", "string:WEEK");
    frequency.addItem("Tous les mois", "string:MONTH");
    frequency.addItem("Tous les ans", "string:YEAR");

    selectionButton.getElement().setInnerHTML(resources.peoples().getText() + selectionButton.getElement().getInnerHTML());
  }

  public void setData(ApplicationInstanceDTO instance, List<CalendarDTO> calendarsDTO) {
    for (CalendarDTO dto : calendarsDTO) {
      calendars.addItem(dto.getTitle(), dto.getId());
    }
  }

  @UiHandler("submit")
  protected void save(ClickEvent event) {
    back();
  }

  @UiHandler("selectionButton")
  protected  void onUserSelection(ClickEvent event) {
    UserSelectionPage page = new UserSelectionPage();

    //page.setPreSelectedIds(ids);
    //sendEventToGetPossibleUsers(fieldName);
    page.show();
  }

  @Override
  public void onRemindersLoaded(final RemindersLoadedEvent event) { }

  @Override
  public void onRemindersDeleted(final ReminderDeletedEvent event) {}

  @Override
  public void onAttachmentLoaded(final AttachmentsLoadedEvent event) {}

  @Override
  public void onRemindersAdding(final RemindersAddingEvent remindersAddingEvent) {}

  @Override
  public void onReminderAdded(final ReminderAddedEvent event) {}

  @Override
  public void onParticipationUpdated(final ParticipationUpdatedEvent event) {}

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractEventPagesEvent.TYPE, this);
    super.stop();
  }

}