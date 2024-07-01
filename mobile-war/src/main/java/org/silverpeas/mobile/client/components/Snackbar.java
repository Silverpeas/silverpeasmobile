package org.silverpeas.mobile.client.components;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;

public class Snackbar {

    public final static int DELAY = 3000;
    public final static int INFO = 0;
    public final static int WARNING = 1;
    public final static int ERROR = 2;

    public static void show(String message, int delay, int type) {
        BodyElement body = Document.get().getBody();
        DivElement div = Document.get().createDivElement();
        div.setAttribute("id","snackbar");
        div.setClassName("show");
        if (type == WARNING) { div.addClassName("warning");}
        if (type == ERROR) { div.addClassName("error");}
        div.setInnerText(message);
        body.appendChild(div);
        close(delay, div);
    }

    private static void close(int delay, DivElement div) {
        Timer timer = new Timer() {
            public void run() {
                div.setClassName("");
                Timer cleaner = new Timer() {
                    public void run() {
                        div.removeFromParent();
                    }
                };
                cleaner.schedule(DELAY);
            }
        };
        timer.schedule(delay);
    }

    public static void showWithCloseButton(String message, int type) {
        BodyElement body = Document.get().getBody();
        DivElement div = Document.get().createDivElement();
        div.setAttribute("id","snackbar");
        div.setClassName("show-static");
        if (type == WARNING) { div.addClassName("warning");}
        if (type == ERROR) { div.addClassName("error");}
        div.setInnerHTML("<span>"+message+"</span>");
        AnchorElement close = Document.get().createAnchorElement();
        close.setClassName("close");
        close.setInnerHTML("<svg height=\"24\" viewBox=\"0 0 24 24\" width=\"24\"><path d=\"M0 0h24v24H0z\" fill=\"none\"/><path d=\"M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z\"/></svg>");
        div.appendChild(close);
        body.appendChild(div);
        Anchor.wrap(close).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                close(0, div);
            }
        });
    }
}
