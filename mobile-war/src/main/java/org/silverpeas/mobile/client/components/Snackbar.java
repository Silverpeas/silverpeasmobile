package org.silverpeas.mobile.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.resources.ApplicationMessages;

public class Snackbar {

    private static ApplicationMessages msg = GWT.create(ApplicationMessages.class);
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
        SpMobil.getMainPage().unfreeze();
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

    public static void showConfirmation(String message, Command yesCommand, Command noCommand, boolean buttonsCenter) {
        String yesLabel = msg.yes();
        String noLabel = msg.no();
        showConfirmation(message, yesLabel, noLabel, yesCommand, noCommand, buttonsCenter);
    }

    public static void showConfirmation(String message, Command yesCommand, Command noCommand) {
        String yesLabel = msg.yes();
        String noLabel = msg.no();
        showConfirmation(message, yesLabel, noLabel, yesCommand, noCommand, true);
    }

    public static void showConfirmation(String message, String yesLabel, String noLabel, Command yesCommand, Command noCommand, boolean buttonsCenter) {
        SpMobil.getMainPage().freeze();
        BodyElement body = Document.get().getBody();
        DivElement div = Document.get().createDivElement();
        div.setAttribute("id","snackbar");
        div.setClassName("show-static");

        SpanElement messageContainer = Document.get().createSpanElement();
        messageContainer.setClassName("snackbar-message");
        messageContainer.setInnerText(message);
        div.appendChild(messageContainer);

        SpanElement actions = Document.get().createSpanElement();
        if (buttonsCenter) {
            actions.setClassName("snackbar-actions-center");
        } else {
            actions.setClassName("snackbar-actions");
        }
        AnchorElement yes = Document.get().createAnchorElement();
        yes.setInnerText(yesLabel);
        yes.setClassName("yes");
        AnchorElement no = Document.get().createAnchorElement();
        no.setInnerText(noLabel);
        no.setClassName("no");
        actions.appendChild(yes);
        actions.appendChild(no);
        div.appendChild(actions);
        body.appendChild(div);
        Anchor.wrap(yes).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (yesCommand != null) yesCommand.execute();
                close(0, div);
            }
        });
        Anchor.wrap(no).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (noCommand != null) noCommand.execute();
                close(0, div);
            }
        });
        messageContainer.getStyle().setWidth(actions.getOffsetLeft() - messageContainer.getOffsetLeft(), Style.Unit.PX);
    }

}
