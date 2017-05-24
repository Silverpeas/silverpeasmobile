package com.silverpeas.mobile.client.apps.contacts.events.app;


public class ContactsLoadEvent extends AbstractContactsAppEvent{

  private String filter, type;
  private int pageSize, startIndex;

  public ContactsLoadEvent(String type, String filter, int pageSize, int startIndex){
    super();
    this.type = type;
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

  public String getType() {
    return type;
  }
}
