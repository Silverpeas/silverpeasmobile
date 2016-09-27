package com.silverpeas.mobile.client.components.base.events.page;

public class PageEvent extends AbstractPageEvent {

    private Object data;
    private Object sender;
    private String name;

    public PageEvent(Object sender, String name, Object data) {
        super();
        this.sender = sender;
        this.name = name;
        this.data = data;
    }

    @Override
    protected void dispatch(PageEventHandler handler) {
        handler.receiveEvent(this);
    }

    public Object getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public Object getSender() {
        return sender;
    }

}
