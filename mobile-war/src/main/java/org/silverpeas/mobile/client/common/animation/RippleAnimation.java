package org.silverpeas.mobile.client.common.animation;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class RippleAnimation {

    public static void play(ClickEvent event, int duration) {
        Element element = ((Widget) event.getSource()).getElement();
        SpanElement ripple = Document.get().createSpanElement();
        ripple.addClassName("ripple");
        element.appendChild(ripple);

        int x = event.getClientX() - element.getOffsetLeft();
        int y = event.getClientY() - element.getOffsetTop();
        ripple.getStyle().setLeft(x, Style.Unit.PX);
        ripple.getStyle().setRight(y, Style.Unit.PX);

        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
            @Override
            public boolean execute() {
                ripple.removeFromParent();
                return false;
            }
        }, duration);
    }

}
