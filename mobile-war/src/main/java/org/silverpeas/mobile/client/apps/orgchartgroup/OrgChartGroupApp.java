/*
 * Copyright (C) 2000 - 2026 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.mobile.client.apps.orgchartgroup;

import com.google.gwt.core.client.GWT;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.orgchartgroup.events.app.AbstractOrgChartGroupAppEvent;
import org.silverpeas.mobile.client.apps.orgchartgroup.events.app.ContactLoadEvent;
import org.silverpeas.mobile.client.apps.orgchartgroup.events.app.OrgChartGroupAppEventHandler;
import org.silverpeas.mobile.client.apps.orgchartgroup.events.ui.ContactLoadedEvent;
import org.silverpeas.mobile.client.apps.orgchartgroup.pages.OrgChartGroupPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.rest.RestMethod;
import org.silverpeas.mobile.client.common.network.rest.RestMethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.orgchart.GroupOrgChartDTO;

public class OrgChartGroupApp extends App implements NavigationEventHandler, OrgChartGroupAppEventHandler {

    private ApplicationMessages msg;

    public OrgChartGroupApp() {
        super();
        msg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
        EventBus.getInstance().addHandler(AbstractOrgChartGroupAppEvent.TYPE, this);
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

            RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<GroupOrgChartDTO>() {
                @Override
                public void attempt() {
                    super.attempt();
                    ServicesLocator.getServiceOrgChartGroup().getOrgChart(event.getInstance().getId(), this);
                }

                @Override
                public void onSuccess(RestMethod method, GroupOrgChartDTO groupOrgChartDTO) {
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

    @Override
    public void loadContact(ContactLoadEvent event) {
        RestMethodCallbackOnlineOnly action = new RestMethodCallbackOnlineOnly<DetailUserDTO>() {
            @Override
            public void attempt() {
                super.attempt();
                ServicesLocator.getServiceContact().getContact(event.getUserId(), this);
            }

            @Override
            public void onSuccess(RestMethod method, DetailUserDTO detailUserDTO) {
                super.onSuccess(method, detailUserDTO);
                EventBus.getInstance().fireEvent(new ContactLoadedEvent(detailUserDTO));
            }
        };
        action.attempt();
    }
}
