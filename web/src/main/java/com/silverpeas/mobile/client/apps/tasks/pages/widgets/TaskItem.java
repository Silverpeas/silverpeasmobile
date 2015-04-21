package com.silverpeas.mobile.client.apps.tasks.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.tasks.events.app.TaskUpdateEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.shared.dto.TaskDTO;

public class TaskItem extends Composite {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);

  @UiField HTMLPanel container;
  @UiField SpanElement name, endDate, delegator, priority, percentCompleted;
  @UiField TextBox range;

  private TaskDTO task;

  interface ContactItemUiBinder extends UiBinder<Widget, TaskItem> {
  }

  public TaskItem() {
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
      range.getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    range.getElement().setAttribute("type", "range");
    range.getElement().setAttribute("min", "0");
    range.getElement().setAttribute("max", "100");
    range.getElement().setAttribute("step", "5");
    range.getElement().setAttribute("value", String.valueOf(data.getPercentCompleted()));
    updateRange(data.getPercentCompleted());
    addListenerInput(range.getElement(), this);
  }

  public native void addListenerInput(Element range, TaskItem item) /*-{
    range.addEventListener('input', function () {
      item.@com.silverpeas.mobile.client.apps.tasks.pages.widgets.TaskItem::updateRange(I)(range.value);
    }, false);
  }-*/;

  @UiHandler("range")
  void changePercent(final ValueChangeEvent<String> event)  {
    percentCompleted.setInnerText(event.getValue() + " %");
    int value = Integer.valueOf(event.getValue());
    updateRange(value);
    EventBus.getInstance().fireEvent(new TaskUpdateEvent(task, event.getValue()));
  }

  private void updateRange(final int value) {
    double val = value / 100.0;
    String css = "background-image: -webkit-gradient(linear, 0% 0%, 100% 0%, color-stop(" + val + ", rgb(114, 171, 14)), color-stop(" + val + ", rgb(197, 197, 197)));";
    range.getElement().setAttribute("style", css);
  }
}
