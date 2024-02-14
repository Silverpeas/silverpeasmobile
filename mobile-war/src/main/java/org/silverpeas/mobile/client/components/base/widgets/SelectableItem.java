package org.silverpeas.mobile.client.components.base.widgets;

import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.components.base.PageContent;

public class SelectableItem extends Composite {

    private PageContent parent;
    private HTMLPanel container;
    private boolean onMove = false;
    private boolean unSelected = false;

    private Timer timerSelection = null;
    private Timer timerScroll = null;

    public void setParent(PageContent page) {
        this.parent = page;
    }

    public void setContainer(HTMLPanel container) {
        this.container = container;
    }

    public boolean isSelected() {
        return container.getElement().hasClassName("selected");
    }

    protected void startTouch(TouchStartEvent event, boolean selectable) {
        if (!parent.isSelectionMode() && selectable) {
            timerSelection = new Timer() {
                @Override
                public void run() {
                    parent.setSelectionMode(true);
                    container.getElement().addClassName("selected");
                    parent.changeSelectionNumber(1);
                }
            };
            timerSelection.schedule(400);
        } else if (parent.isSelectionMode() && selectable) {
            timerScroll = new Timer() {
                @Override
                public void run() {
                    if (!onMove) {
                        if (container.getElement().hasClassName("selected")) {
                            container.getElement().removeClassName("selected");
                            parent.changeSelectionNumber(-1);
                            unSelected = true;
                        } else {
                            container.getElement().addClassName("selected");
                            parent.changeSelectionNumber(1);
                        }
                    } else {
                        onMove = false;
                    }
                }
            };
            timerScroll.schedule(100);
        }
    }

    protected void moveTouch(TouchMoveEvent event) {
        if (timerSelection != null) timerSelection.cancel();
        if (timerScroll != null) timerScroll.cancel();
        onMove = true;
    }

    protected void endTouch(TouchEndEvent event, boolean selectable, Command onClickAction) {
        if (timerSelection != null) timerSelection.cancel();
        if (!onMove) {
            if (selectable) {
                if (!parent.isSelectionMode() && !unSelected) {
                    onClickAction.execute();
                }
            } else {
                onClickAction.execute();
            }
        } else {
            onMove = false;
        }
        unSelected = false;
    }

}
