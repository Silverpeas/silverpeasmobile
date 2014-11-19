package com.silverpeas.mobile.client.apps.tasks.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.TaskDTO;

public class TaskItem extends Composite {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);

  @UiField HTMLPanel container, name, endDate, delegator, priority, percentCompleted;

  interface ContactItemUiBinder extends UiBinder<Widget, TaskItem> {
  }

  public TaskItem() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(TaskDTO data) {
    name.add(new HTML(data.getName()));
    name.add(new HTML(data.getEndDate()));
    name.add(new HTML(data.getDelegator()));
    name.add(new HTML(data.getPriority()+""));
    name.add(new HTML(data.getPercentCompleted() + " %"));
  }

}
