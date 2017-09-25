package com.silverpeas.mobile.client.components.userselection.widgets.events;


import com.silverpeas.mobile.client.components.userselection.widgets.UserGroupItem;

public class ChangeEvent extends AbstractSelectionEvent {

  private UserGroupItem select;

  public ChangeEvent(UserGroupItem select){
    super();
    this.select = select;
  }

  @Override
  protected void dispatch(SelectionEventHandler handler) {
    handler.onSelectionChange(this);
  }

  public UserGroupItem getItem() {
    return select;
  }
}
