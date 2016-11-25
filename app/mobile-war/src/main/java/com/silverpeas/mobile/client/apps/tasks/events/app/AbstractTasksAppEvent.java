package com.silverpeas.mobile.client.apps.tasks.events.app;

import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractTasksAppEvent extends GwtEvent<TasksAppEventHandler>{

  public static Type<TasksAppEventHandler> TYPE = new Type<TasksAppEventHandler>();

  public AbstractTasksAppEvent(){
  }

  @Override
  public Type<TasksAppEventHandler> getAssociatedType() {
    return TYPE;
  }
}
