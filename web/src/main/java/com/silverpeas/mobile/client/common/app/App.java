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

package com.silverpeas.mobile.client.common.app;

import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;

public abstract class App {

  private Page mainPage;
  private Page lauchingPage;
  private Controller controller;
  protected Transition transition = Transition.SLIDEDOWN;

  public void start(Page lauchingPage) {
    this.lauchingPage = lauchingPage;
    lauchingPage.goTo(mainPage, transition);
  }

  public void stop() {
    setMainPage(null);
    getController().stop();
    setController(null);
  }

  protected Page getMainPage() {
    return mainPage;
  }

  protected void setMainPage(Page mainPage) {
    this.mainPage = mainPage;
  }

  public Page getLauchingPage() {
    return lauchingPage;
  }

  public void setLauchingPage(Page lauchingPage) {
    this.lauchingPage = lauchingPage;
  }

  public Controller getController() {
    return controller;
  }

  public void setController(Controller controller) {
    this.controller = controller;
  }
}
