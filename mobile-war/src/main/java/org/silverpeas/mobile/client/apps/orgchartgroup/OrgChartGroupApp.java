package org.silverpeas.mobile.client.apps.orgchartgroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.orgchartgroup.pages.OrgChartGroupPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.orgchart.GroupOrgChartDTO;

public class OrgChartGroupApp extends App implements NavigationEventHandler {

    private ApplicationMessages msg;

    public OrgChartGroupApp() {
        super();
        msg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
    }

    public void start() {
        // no "super.start(lauchingPage);" this apps is used in another apps
    }

    @Override
    public void showContent(NavigationShowContentEvent event) {
        super.showContent(event);
    }

    @Override
    public void startWithContent(ContentDTO content) {
        super.startWithContent(content);
    }

    @Override
    public void stop() {
        // never stop
    }

    @Override
    public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
        if (event.getInstance().getType().equals(Apps.orgchartGroup.name())) {
            setApplicationInstance(event.getInstance());

            MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<GroupOrgChartDTO>() {
                @Override
                public void attempt() {
                    super.attempt();
                    ServicesLocator.getServiceOrgChartGroup().getOrgChart(event.getInstance().getId(), this);
                }

                @Override
                public void onSuccess(Method method, GroupOrgChartDTO groupOrgChartDTO) {
                    super.onSuccess(method, groupOrgChartDTO);
                    OrgChartGroupPage page = new OrgChartGroupPage();
                    page.setApp(OrgChartGroupApp.this);
                    page.setPageTitle(event.getInstance().getLabel());
                    page.setData(groupOrgChartDTO);
                    page.show();
                }
            };
            action.attempt();





        }
    }
}
