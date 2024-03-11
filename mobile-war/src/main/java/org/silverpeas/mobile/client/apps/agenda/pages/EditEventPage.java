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
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.agenda.events.TimeRange;
import org.silverpeas.mobile.client.apps.agenda.events.app.CalendarLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.EventCreateEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.*;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.userselection.UserSelectionPage;
import org.silverpeas.mobile.client.components.userselection.events.components.AbstractUserSelectionComponentEvent;
import org.silverpeas.mobile.client.components.userselection.events.components.UserSelectionComponentEventHandler;
import org.silverpeas.mobile.client.components.userselection.events.components.UsersAndGroupsSelectedEvent;
import org.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import org.silverpeas.mobile.client.resources.ApplicationResources;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.almanach.*;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author svu
 */
public class EditEventPage extends PageContent implements EditEventPagesEventHandler, UserSelectionComponentEventHandler {
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
  DivElement participantsSelected, icon;

  private List<BaseDTO> allowedUsersAndGroups;

  private List<BaseDTO> selectedUsersAndGroups;

  private TimeRange currentTimeRange;

  public void setAllowedUsersAndGroups(List<BaseDTO> allowedUsersAndGroups) {
    this.allowedUsersAndGroups = allowedUsersAndGroups;
  }

  public void setCurrentTimeRange(TimeRange currentTimeRange) {
    this.currentTimeRange = currentTimeRange;
  }

  interface EditEventPageUiBinder extends UiBinder<Widget, EditEventPage> {
  }

  public EditEventPage() {
    msg = GWT.create(AgendaMessages.class);
    setPageTitle(msg.newtitleEvent());
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("eventForm");
    title.getElement().setAttribute("placeholder", msg.titleField());
    description.getElement().setAttribute("placeholder", msg.descriptionField());
    description.getElement().setAttribute("rows", "6");
    startDate.getElement().setAttribute("type", "date");
    endDate.getElement().setAttribute("type", "date");
    notimportant.setValue(true);
    publique.setValue(true);
    submit.getElement().addClassName("formIncomplete");
    EventBus.getInstance().addHandler(AbstractEditEventPagesEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractUserSelectionComponentEvent.TYPE, this);
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

    frequency.addItem(msg.frequencyNone(), "NONE");
    frequency.addItem(msg.frequencyDay(), "DAY");
    frequency.addItem(msg.frequencyWeek(), "WEEK");
    frequency.addItem(msg.frequencyMouth(), "MONTH");
    frequency.addItem(msg.frequencyYear(), "YEAR");

    icon.setInnerHTML(resources.peoples().getText());
  }

  public void setData(ApplicationInstanceDTO instance, List<CalendarDTO> calendarsDTO) {
    for (CalendarDTO dto : calendarsDTO) {
      calendars.addItem(dto.getTitle(), dto.getId());
    }
  }

  @UiHandler("title")
  protected void changeTitle(ChangeEvent event) {
    validateForm();
  }

  @UiHandler("startDate")
  protected void changeStartDate(ChangeEvent event) {
    endDate.getElement().setAttribute("min", startDate.getValue());
    if (startDate.getText().isEmpty()) {
      startDate.getElement().addClassName("formMandatoryField");
    } else {
      startDate.getElement().removeClassName("formMandatoryField");
    }
    validateForm();
  }

  @UiHandler("endDate")
  protected void changeEndDate(ChangeEvent event) {
    startDate.getElement().setAttribute("max", endDate.getValue());
    if (endDate.getText().isEmpty()) {
      endDate.getElement().addClassName("formMandatoryField");
    } else {
      endDate.getElement().removeClassName("formMandatoryField");
    }
    validateForm();
  }

  private boolean validateForm() {
    boolean valid = !startDate.getText().isEmpty() && !endDate.getText().isEmpty() && !title.getText().isEmpty();
    if (valid) {
      submit.getElement().removeClassName("formIncomplete");
    } else {
      submit.getElement().addClassName("formIncomplete");
    }
    return valid;
  }

  @UiHandler("submit")
  protected void save(ClickEvent event) {
    if (!submit.getElement().hasClassName("formIncomplete")) {
      CalendarEventCreationDTO dto = new CalendarEventCreationDTO();
      dto.setEventType("CalendarEvent");
      dto.setOccurrenceType("CalendarEventOccurrence");
      dto.setEventId("volatile-" + new Date().getTime());
      CalendarDTO calendarDTO = new CalendarDTO();
      calendarDTO.setId(calendars.getSelectedValue());
      dto.setCalendar(calendarDTO);
      dto.setTitle(title.getText());
      dto.setOnAllDay(allDay.getValue());
      if (allDay.getValue()) {
        dto.setStartDate(startDate.getText());
        dto.setEndDate(endDate.getText());
      } else {
        dto.setStartDate(startDate.getText()+":00+01:00");
        dto.setEndDate(endDate.getText()+":00+01:00");
      }
      dto.setDescription(description.getText());
      if (important.getValue()) dto.setPriority(PriorityDTO.HIGH.name());
      if (notimportant.getValue()) dto.setPriority(PriorityDTO.NORMAL.name());
      if (publique.getValue()) dto.setVisibility(VisibilityLevelDTO.PUBLIC.name());
      if (prive.getValue()) dto.setVisibility(VisibilityLevelDTO.PRIVATE.name());
      if (!frequency.getSelectedValue().equals("NONE")) {
        CalendarEventRecurrenceDTO rec = new CalendarEventRecurrenceDTO();
        CalendarEventRecurrenceDTO.FrequencyDTO freq = new CalendarEventRecurrenceDTO.FrequencyDTO();
        freq.setTimeUnit(TimeUnitDTO.valueOf(frequency.getSelectedValue()));
        freq.setInterval(1);
        rec.setFrequency(freq);
        dto.setRecurrence(rec);
      }
      dto.setAttendees(getAttendees());
      EventCreateEvent ev = new EventCreateEvent(dto);
      EventBus.getInstance().fireEvent(ev);
    }
  }

  private List<CalendarEventAttendeeDTO> getAttendees() {
    List<CalendarEventAttendeeDTO> attendees = new ArrayList<>();
    if (selectedUsersAndGroups != null) {
      for (BaseDTO sel : selectedUsersAndGroups) {
        CalendarEventAttendeeDTO a = new CalendarEventAttendeeDTO();
        a.setId(sel.getId());
        if (sel instanceof UserDTO) {
          a.setFullName(((UserDTO) sel).getFirstName() + ((UserDTO) sel).getLastName());
        }
        a.setParticipationStatus(ParticipationStatusDTO.AWAITING);
        a.setPresenceStatus(PresenceStatusDTO.INFORMATIVE);
        attendees.add(a);
      }
    }
    return attendees;
  }

  @UiHandler("selectionButton")
  protected  void onUserSelection(ClickEvent event) {
    UserSelectionPage page = new UserSelectionPage();
    if (selectedUsersAndGroups != null) {
      List<String> ids = new ArrayList<>();
      for (BaseDTO sel : selectedUsersAndGroups) {
        if (sel instanceof UserDTO) ids.add(sel.getId());
      }
      page.setPreSelectedUsersIds(ids);
    }
    page.show();
    AllowedUsersAndGroupsLoadedEvent ev = new AllowedUsersAndGroupsLoadedEvent(allowedUsersAndGroups, true);
    EventBus.getInstance().fireEvent(ev);
  }

  @Override
  public void onUsersAndGroupSelected(UsersAndGroupsSelectedEvent event) {
    this.selectedUsersAndGroups = event.getUsersAndGroupsSelected();
    String selectionNames = "";
    for (BaseDTO sel : selectedUsersAndGroups) {
      if (sel instanceof UserDTO) {
        selectionNames += ((UserDTO) sel).getFirstName() + " " + ((UserDTO) sel).getLastName() + " , ";
      } else if (sel instanceof GroupDTO) {
        selectionNames += ((GroupDTO) sel).getName() + " , ";
      }
    }
    selectionNames = selectionNames.substring(0, selectionNames.length() - 2);
    participantsSelected.setInnerText(selectionNames);
  }

  @Override
  public void onEventSaved(EventSavedEvent event) {
    back();
    EventBus.getInstance().fireEvent(new CalendarLoadEvent(null, currentTimeRange));
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractUserSelectionComponentEvent.TYPE, this);
    EventBus.getInstance().removeHandler(AbstractEditEventPagesEvent.TYPE, this);
    super.stop();
  }

}