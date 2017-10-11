package com.silverpeas.mobile.client.apps.tasks.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.silverpeas.mobile.client.apps.tasks.pages.TaskPage;
import com.silverpeas.mobile.client.apps.tasks.resources.TasksMessages;
import com.silverpeas.mobile.shared.dto.TaskDTO;

public class TaskItem extends Composite {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);

  @UiField HTMLPanel container;
  @UiField SpanElement name, endDate, delegator, priority, percentCompleted;
  @UiField InputElement range;
  @UiField Anchor link;

  @UiField(provided = true) protected TasksMessages msg = null;

  private TaskDTO task;

  interface ContactItemUiBinder extends UiBinder<Widget, TaskItem> {
  }

  public TaskItem() {
    msg = GWT.create(TasksMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(TaskDTO data) {
    this.task = data;
    name.setInnerHTML(data.getName());
    endDate.setInnerHTML(data.getEndDate());
    delegator.setInnerHTML(data.getDelegator());
    priority.setInnerHTML(data.getPriority() + "");
    percentCompleted.setInnerHTML(data.getPercentCompleted() + " %");
    if (!data.getExternalId().isEmpty()) {
      //TODO : display edit percent completed
      range.getStyle().setDisplay(Style.Display.NONE);
    }


    range.setAttribute("min", "0");
    range.setAttribute("max", "100");
    range.setAttribute("step", "5");
    range.setAttribute("value", String.valueOf(data.getPercentCompleted()));
    range.setDisabled(true);
    updateRange(data.getPercentCompleted());
  }

  public TaskDTO getData() {
    return task;
  }

  public native void addListenerInput(Element range, TaskItem item) /*-{
    range.addEventListener('input', function () {
      item.@com.silverpeas.mobile.client.apps.tasks.pages.widgets.TaskItem::updateRange(I)(range.value);
    }, false);
  }-*/;

  @UiHandler("link")
  protected void edit(ClickEvent event) {
    if (task.getExternalId().isEmpty()) {
      TaskPage page = new TaskPage();
      page.setPageTitle(msg.edit());
      page.setData(task);
      page.show();
    } else {
      //TODO : redirect on content
    }
  }

  private void updateRange(final int value) {
    double val = value / 100.0;
    String css = "background-image: -webkit-gradient(linear, 0% 0%, 100% 0%, color-stop(" + val + ", rgb(114, 171, 14)), color-stop(" + val + ", rgb(197, 197, 197)));";
    range.setAttribute("style", css);
  }


}
