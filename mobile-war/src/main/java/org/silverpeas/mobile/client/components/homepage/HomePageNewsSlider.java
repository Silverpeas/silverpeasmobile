/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.client.components.homepage;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;

/**
 * @author svu
 */
public class HomePageNewsSlider {

  private static HomePageNewsSlider instance = null;
  private static boolean stop = false;
  private static HomePageContent currentHomePageContent = null;

  public static HomePageNewsSlider getInstance() {
    if (instance == null) {
      instance = new HomePageNewsSlider();
      createScheduler();
    }
    return instance;
  }

  public void setCurrentHomePageContent(HomePageContent current) {
    HomePageNewsSlider.currentHomePageContent = current;
    HomePageNewsSlider.stop = false;
  }

  public void stopAutoSlider() {
    HomePageNewsSlider.stop = true;
  }

  private static void createScheduler() {
    Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
      @Override
      public boolean execute() {
        if (!stop) {
          currentHomePageContent.slideToRight();
        }
        return true;
      }
    }, 5000);
  }
}
