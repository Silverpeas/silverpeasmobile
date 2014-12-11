package com.silverpeas.mobile.client.apps.tasks.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.components.base.PageContent;

/**
 * @author: svu
 */
public class TaskPage extends PageContent {
  interface TaskPageUiBinder extends UiBinder<HTMLPanel, TaskPage> {}

  private static TaskPageUiBinder uiBinder = GWT.create(TaskPageUiBinder.class);

  public TaskPage() {
    initWidget(uiBinder.createAndBindUi(this));
  }
}