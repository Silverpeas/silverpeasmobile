package com.silverpeas.mobile.client.components.userselection.widgets.events;


public class ChangeEvent extends AbstractSelectionEvent {

  private boolean select;

  public ChangeEvent(boolean select){
    super();
    this.select = select;
  }

  @Override
  protected void dispatch(SelectionEventHandler handler) {
    handler.onSelectionChange(this);
  }

  public boolean isSelect() {
    return select;
  }
}
