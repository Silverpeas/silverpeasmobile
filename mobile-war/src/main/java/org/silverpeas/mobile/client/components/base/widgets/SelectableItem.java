package org.silverpeas.mobile.client.components.base.widgets;

import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.u i.HTMLPanel;
import org.silverpeas.mobile.client.components.base.PageContent;

public class SelectableItem extends Composite {

    private PageContent parent;
    private HTMLPanel container;
    private boolean selectionMode = false;
    private boolean onMove = false;

    private Timer timer = null;

    public void setParent(PageContent page) {
        this.parent = page;
    }

    public void setContainer(HTMLPanel container) {
        this.container = container;
    }

    public void setSelectionMode(boolean mode) {
        this.selectionMode = mode;
    }

    public boolean isSelected() {
        return container.getElement().hasClassName("selected");
    }

    protected void startTouch(TouchStartEvent event, boolean selectable) {
        if (!parent.isSelectionMode() && selectable) {
            timer = new Timer() {
                @Override
                public void run() {
                    selectionMode = true;
                    container.getElement().addClassName("selected");
                }
            };
            timer.schedule(400);
        }
    }

    protected void moveTouch(TouchMoveEvent event) {
        if (timer != null) timer.cancel();
        onMove = true;
    }

    protected void endTouch(TouchEndEvent event, boolean selectable, Command onClickAction) {
        if (!onMove) {
            if (!selectable) {
                onClickAction.execute();
                return;
            }
            if (parent.isSelectionMode()) {
                if (container.getElement().hasClassName("selected")) {
                    container.getElement().removeClassName("selected");
                    parent.changeSelectionNumber(-1);
                } else {
                    container.getElement().addClassName("selected");
                    parent.changeSelectionNumber(1);
                }
            } else {
                if (selectionMode) {
                    container.getElement().addClassName("selected");
                    parent.changeSelectionNumber(1);
                    parent.setSelectionMode(true);
                } else {
                    onClickAction.execute();
                    if (timer != null) timer.cancel();
                }
            }
        } else {
            onMove = false;
        }
    }

}
