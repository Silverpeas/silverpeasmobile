/*
 * Copyright (C) 2000 - 2018 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.apps.agenda;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.agenda.events.TimeRange;
import org.silverpeas.mobile.client.apps.agenda.events.app.AbstractAgendaAppEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.AgendaAppEventHandler;
import org.silverpeas.mobile.client.apps.agenda.events.app.AttachmentsLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.CalendarLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.ReminderCreateEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.ReminderDeleteEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.ReminderUpdateEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.RemindersLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AttachmentsLoadedEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.CalendarLoadedEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.ReminderDeletedEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.RemindersLoadedEvent;
import org.silverpeas.mobile.client.apps.agenda.pages.AgendaPage;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.documents.DocumentType;
import org.silverpeas.mobile.shared.dto.documents.SimpleDocumentDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.reminder.ReminderDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgendaApp extends App implements AgendaAppEventHandler, NavigationEventHandler {

  private AgendaMessages msg;
  private String key;
  private String keyCalendars;

  public static final String EVENT_REMINDER_TYPE = "CalendarEvent";

  public AgendaApp(){
    super();
    msg = GWT.create(AgendaMessages.class);
    EventBus.getInstance().addHandler(AbstractAgendaAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start(){
    // always start
  }

  @Override
  public void stop() {
    // nevers stop
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.almanach.name())) {
      setApplicationInstance(event.getInstance());
      key = "events_" + getApplicationInstance().getId();
      keyCalendars = "calendars_" + getApplicationInstance().getId();

      AgendaPage page = new AgendaPage();
      page.setPageTitle(event.getInstance().getLabel());

      Command offlineAction = new Command() {
        @Override
        public void execute() {
          List<CalendarDTO> calendars = LocalStorageHelper.load(keyCalendars, List.class);
          if (calendars == null) {
            calendars = new ArrayList<CalendarDTO>();
          }
          page.setApp(AgendaApp.this);
          page.setCalendars(calendars);
          page.show();
        }
      };

      MethodCallbackOnlineOrOffline action = new MethodCallbackOnlineOrOffline<List<CalendarDTO>>(offlineAction) {
        @Override
        public void attempt() {
          ServicesLocator.getServiceAlmanach().getCalendars(getApplicationInstance().getId(), this);
        }

        @Override
        public void onSuccess(final Method method, final List<CalendarDTO> calendars) {
          super.onSuccess(method, calendars);
          page.setApp(AgendaApp.this);
          page.setCalendars(calendars);
          page.show();
        }
      };
      action.attempt();
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {

  }

  @Override
  public void loadCalendarEvents(final CalendarLoadEvent event) {


    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<CalendarEventDTO> events = LocalStorageHelper.load(key+event.getCalendar().getId(), List.class);
        if (events == null) {
          events = new ArrayList<CalendarEventDTO>();
        }
        EventBus.getInstance().fireEvent(new CalendarLoadedEvent(getApplicationInstance(), events));
      }
    };

    MethodCallbackOnlineOrOffline action = new MethodCallbackOnlineOrOffline<List<CalendarEventDTO>>(offlineAction) {
      @Override
      public void attempt() {
        // Date format sample : 2018-03-24T23:00:00.000Z
        String startDateOfWindowTime;
        String endDateOfWindowTime;

        Date today = new Date();
        DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        startDateOfWindowTime = dtf.format(today);
        Date end = new Date();
        if (event.getRange().equals(TimeRange.weeks)) {
          CalendarUtil.addDaysToDate(end, 7*4+1); // for include last day
        } else if (event.getRange().equals(TimeRange.months)) {
          CalendarUtil.addMonthsToDate(end,12);
          CalendarUtil.addDaysToDate(end,1); // for include last day
        }
        endDateOfWindowTime = dtf.format(end);
        ServicesLocator.getServiceAlmanach()
            .getOccurences(getApplicationInstance().getId(), event.getCalendar().getId(),
                startDateOfWindowTime, endDateOfWindowTime, SpMobil.getUser().getZone(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<CalendarEventDTO> events) {
        super.onSuccess(method, events);
        LocalStorageHelper.store(key + event.getCalendar().getId(), List.class, events);
        EventBus.getInstance().fireEvent(new CalendarLoadedEvent(getApplicationInstance(), events));
      }
    };
    action.attempt();

  }

  @Override
  public void loadReminders(final RemindersLoadEvent event) {
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<String> durations = LocalStorageHelper.load("durations", List.class);
        if (durations == null) {
          durations = new ArrayList<String>();
        }

        List<ReminderDTO> reminders = LocalStorageHelper.load("reminders"+event.getEvent().getEventId() , List.class);
        if (reminders == null) {
          reminders = new ArrayList<ReminderDTO>();
        }

        EventBus.getInstance().fireEvent(new RemindersLoadedEvent(reminders, durations));
      }
    };

    MethodCallbackOnlineOrOffline action = new MethodCallbackOnlineOrOffline<List<ReminderDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceReminder()
            .getReminders(getApplicationInstance().getId(), EVENT_REMINDER_TYPE, event.getEvent().getEventId(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<ReminderDTO> reminders) {
        super.onSuccess(method, reminders);
        LocalStorageHelper.store("reminders" + event.getEvent().getEventId(), List.class, reminders);

        Command offlineAction2 = new Command() {
          @Override
          public void execute() {
            List<String> durations = LocalStorageHelper.load("durations", List.class);
            if (durations == null) {
              durations = new ArrayList<String>();
            }
            EventBus.getInstance().fireEvent(new RemindersLoadedEvent(reminders, durations));
          }
        };

        MethodCallbackOnlineOrOffline action2 = new MethodCallbackOnlineOrOffline<List<String>>(offlineAction2) {
          @Override
          public void attempt() {
            ServicesLocator.getServiceReminder().getPossibleDurations(getApplicationInstance().getId(),
                EVENT_REMINDER_TYPE, event.getEvent().getEventId(), "NEXT_START_DATE_TIME", this);
          }

          @Override
          public void onSuccess(final Method method, final List<String> durations) {
            super.onSuccess(method, durations);
            EventBus.getInstance().fireEvent(new RemindersLoadedEvent(reminders, durations));
          }
        };
        action2.attempt();
      }
    };
    action.attempt();
  }

  @Override
  public void updateReminder(final ReminderUpdateEvent event) {
    Notification.activityStart();
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<ReminderDTO>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceReminder().updateReminder(getApplicationInstance().getId(), EVENT_REMINDER_TYPE,
            event.getEvent().getEventId(), event.getReminder().getId(), event.getReminder(), this);
      }

      @Override
      public void onSuccess(final Method method, final ReminderDTO result) {
        super.onSuccess(method, result);
      }
    };
    action.attempt();
  }

  @Override
  public void createReminder(final ReminderCreateEvent event) {
    Notification.activityStart();
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<ReminderDTO>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceReminder().createReminder(getApplicationInstance().getId(), EVENT_REMINDER_TYPE,
            event.getEvent().getEventId(), event.getReminder(), this);
      }

      @Override
      public void onSuccess(final Method method, final ReminderDTO result) {
        super.onSuccess(method, result);
        //TODO : send event on page
      }
    };
    action.attempt();
  }

  @Override
  public void deleteReminder(final ReminderDeleteEvent event) {
    Notification.activityStart();
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceReminder().deleteReminder(getApplicationInstance().getId(), EVENT_REMINDER_TYPE,
            event.getEvent().getEventId(), event.getReminder().getId(), this);
      }

      @Override
      public void onSuccess(final Method method, final Void result) {
        super.onSuccess(method, result);
        EventBus.getInstance().fireEvent(new ReminderDeletedEvent());
      }
    };
    action.attempt();
  }

  @Override
  public void loadAttachments(final AttachmentsLoadEvent event) {
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<SimpleDocumentDTO> attachments = LocalStorageHelper.load("event_attachments_"+event.getEvent().getEventId(), List.class);
        if (attachments == null) {
          attachments = new ArrayList<SimpleDocumentDTO>();
        }
        EventBus.getInstance().fireEvent(new AttachmentsLoadedEvent(attachments));
      }
    };

    MethodCallbackOnlineOrOffline action = new MethodCallbackOnlineOrOffline<List<SimpleDocumentDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getRestServiceDocuments().getDocumentsByType(getApplicationInstance().getId(), event.getEvent().getEventId(),
            DocumentType.attachment.name(), SpMobil.getUser().getLanguage(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<SimpleDocumentDTO> attachments) {
        super.onSuccess(method, attachments);
        LocalStorageHelper.store("event_attachments_" + event.getEvent().getEventId(), List.class, attachments);
        EventBus.getInstance().fireEvent(new AttachmentsLoadedEvent(attachments));
      }
    };
    action.attempt();
  }
}

