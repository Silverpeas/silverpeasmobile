package com.silverpeas.mobile.client.components.base.events;


public class EndPageEvent extends AbstractScrollEvent {


  public EndPageEvent(){
    super();
  }

  @Override
  protected void dispatch(ScrollEventHandler handler) {
    handler.onScrollEndPage(this);
  }
}
