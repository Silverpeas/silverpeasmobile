package com.silverpeas.mobile.client.apps.tasks.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.tasks.pages.TaskPage;
import com.silverpeas.mobile.client.apps.tasks.resources.TasksMessages;

/**
 * @author: svu
 */
public class AddTaskItem  extends Composite {

  interface AddTaskItemUiBinder extends UiBinder<Widget, AddTaskItem> {
  }

  @UiField Anchor link;
  @UiField(provided = true) protected TasksMessages msg = null;

  private static AddTaskItemUiBinder uiBinder = GWT.create(AddTaskItemUiBinder.class);

  public AddTaskItem() {
    msg = GWT.create(TasksMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("link")
  void createTask(ClickEvent event) {
    TaskPage page = new TaskPage();
    page.setPageTitle(msg.create());
    page.show();
  }
}