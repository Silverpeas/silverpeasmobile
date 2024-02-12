package org.silverpeas.mobile.client.components.base.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.tasks.pages.TaskPage;
import org.silverpeas.mobile.client.components.base.PageContent;

public class SelectableItem extends Composite {

    private PageContent parent;
    private HTMLPanel container;
    private boolean selectionMode = false;

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
            Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                @Override
                public boolean execute() {
                    selectionMode = true;
                    container.getElement().addClassName("selected");
                    return false;
                }
            }, 400);
        }
    }

    protected void endTouch(TouchEndEvent event, boolean selectable, Command onClickAction) {
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
                Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                    @Override
                    public boolean execute() {
                        selectionMode = false;
                        container.getElement().removeClassName("selected");
                        return false;
                    }
                }, 400);
            }
        }
    }

}
