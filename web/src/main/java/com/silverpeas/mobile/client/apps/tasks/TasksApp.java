package com.silverpeas.mobile.client.apps.tasks;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.status.events.app.AbstractStatusAppEvent;
import com.silverpeas.mobile.client.apps.status.events.app.StatusAppEventHandler;
import com.silverpeas.mobile.client.apps.status.events.app.StatusPostEvent;
import com.silverpeas.mobile.client.apps.status.events.pages.StatusPostedEvent;
import com.silverpeas.mobile.client.apps.status.pages.StatusPage;
import com.silverpeas.mobile.client.apps.tasks.events.app.AbstractTasksAppEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TasksAppEventHandler;
import com.silverpeas.mobile.client.apps.tasks.events.app.TasksLoadEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TasksLoadedEvent;
import com.silverpeas.mobile.client.apps.tasks.pages.TasksPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.silverpeas.mobile.shared.dto.TaskDTO;
import org.apache.poi.ss.formula.functions.Even;

import java.util.Date;
import java.util.List;

public class TasksApp extends App implements TasksAppEventHandler {

  public TasksApp(){
    super();
    EventBus.getInstance().addHandler(AbstractTasksAppEvent.TYPE, this);
  }

  public void start(){
    setMainPage(new TasksPage());
    super.start();
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractTasksAppEvent.TYPE, this);
    super.stop();
  }

  @Override
  public void loadTasks(final TasksLoadEvent event) {
    Notification.activityStart();
    ServicesLocator.serviceTasks.loadTasks(new AsyncCallback<List<TaskDTO>>() {
      @Override
      public void onFailure(final Throwable caught) {
        EventBus.getInstance().fireEvent(new ErrorEvent(caught));
      }

      @Override
      public void onSuccess(final List<TaskDTO> tasks) {
        EventBus.getInstance().fireEvent(new TasksLoadedEvent(tasks));
      }
    });
  }
}
