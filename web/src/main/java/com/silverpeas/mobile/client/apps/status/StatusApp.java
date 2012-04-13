/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.client.apps.status;

import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.status.events.app.internal.AbstractStatusEvent;
import com.silverpeas.mobile.client.apps.status.events.app.internal.StatusEventHandler;
import com.silverpeas.mobile.client.apps.status.events.app.internal.StatusStopEvent;
import com.silverpeas.mobile.client.apps.status.pages.StatusPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class StatusApp extends App implements StatusEventHandler {

  public StatusApp() {
    super();
    EventBus.getInstance().addHandler(AbstractStatusEvent.TYPE, this);
  }

  public void start(Page lauchingPage) {
    setController(new StatusController());
    setMainPage(new StatusPage());
    super.start(lauchingPage);
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractStatusEvent.TYPE, this);
    super.stop();
  }

  @Override
  public void onStop(StatusStopEvent event) {
    stop();
  }
}
