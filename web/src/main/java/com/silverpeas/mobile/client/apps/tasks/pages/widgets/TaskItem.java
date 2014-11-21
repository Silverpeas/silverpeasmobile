package com.silverpeas.mobile.client.apps.tasks.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.shared.dto.TaskDTO;

public class TaskItem extends Composite {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);

  @UiField HTMLPanel container, name, endDate, delegator, priority, percentCompleted;
  @UiField TextBox range;

  private HTML percent;

  interface ContactItemUiBinder extends UiBinder<Widget, TaskItem> {
  }

  public TaskItem() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(TaskDTO data) {
    name.add(new HTML(data.getName()));
    endDate.add(new HTML(data.getEndDate()));
    delegator.add(new HTML(data.getDelegator()));
    priority.add(new HTML(data.getPriority() + ""));
    percent = new HTML(data.getPercentCompleted() + " %");
    percentCompleted.add(percent);
    if (!data.getExternalId().isEmpty()) {
      //TODO : display edit percent completed
      range.getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    range.getElement().setAttribute("type", "range");
    range.getElement().setAttribute("min", "0");
    range.getElement().setAttribute("max", "100");
    range.getElement().setAttribute("step", "5");
    range.getElement().setAttribute("value", String.valueOf(data.getPercentCompleted()));

  }

  @UiHandler("range")
  void changePercent(ValueChangeEvent<String> event)  {
    percent.setHTML(event.getValue() + " %");
  }
}
