package com.silverpeas.mobile.client.components.base.events.apps;

public class AppEvent extends AbstractAppEvent {

    private Object data;
    private Object sender;
    private String name;

    public AppEvent(Object sender, String name, Object data) {
        super();
        this.sender = sender;
        this.name = name;
        this.data = data;
    }

    @Override
    protected void dispatch(AppEventHandler handler) {
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
