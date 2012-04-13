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

package com.silverpeas.mobile.client.apps.gallery;

import com.gwtmobile.ui.client.page.Page;
import com.silverpeas.mobile.client.apps.gallery.events.app.internal.AbstractGalleryEvent;
import com.silverpeas.mobile.client.apps.gallery.events.app.internal.GalleryEventHandler;
import com.silverpeas.mobile.client.apps.gallery.events.app.internal.GalleryStopEvent;
import com.silverpeas.mobile.client.apps.gallery.pages.GalleryPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class GalleryApp extends App implements GalleryEventHandler {

  public GalleryApp() {
    super();
    EventBus.getInstance().addHandler(AbstractGalleryEvent.TYPE, this);
  }

  @Override
  public void start(Page lauchingPage) {
    setController(new GalleryController());
    setMainPage(new GalleryPage());
    super.start(lauchingPage);
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractGalleryEvent.TYPE, this);
    super.stop();
  }

  @Override
  public void onStop(GalleryStopEvent event) {
    stop();
  }
}
