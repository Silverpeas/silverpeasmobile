package com.silverpeas.mobile.client.apps.contacts.events.app;


public class ContactsLoadEvent extends AbstractContactsAppEvent{

  private String filter;
  private int pageSize, startIndex;

  public ContactsLoadEvent(String filter, int pageSize, int startIndex){
    super();
    this.filter = filter;
    this.pageSize = pageSize;
    this.startIndex = startIndex;
  }

  @Override
  protected void dispatch(ContactsAppEventHandler handler) {
    handler.loadContacts(this);
  }

  public String getFilter(){
    return filter;
  }

  public int getPageSize() {
    return pageSize;
  }

  public int getStartIndex() {
    return startIndex;
  }
}
