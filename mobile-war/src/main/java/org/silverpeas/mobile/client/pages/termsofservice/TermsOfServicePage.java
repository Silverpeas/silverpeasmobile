/*
 * Copyright (C) 2000 - 2018 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.pages.termsofservice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;


public class TermsOfServicePage extends PageContent {

  interface TermsOfServicePageUiBinder extends UiBinder<Widget, TermsOfServicePage> {}

  private static TermsOfServicePageUiBinder uiBinder = GWT.create(TermsOfServicePageUiBinder.class);

  @UiField
  Anchor accept, refuse;

  @UiField
  HTML terms;

  @UiField(provided = true)
  protected ApplicationMessages msg = null;

  public TermsOfServicePage() {
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));

    ServicesLocator.getServiceConnection().getTermsOfServiceText(new AsyncCallback<String>() {
      @Override
      public void onFailure(final Throwable t) {
        EventBus.getInstance().fireEvent(new ErrorEvent(t));
      }

      @Override
      public void onSuccess(final String text) {
        terms.setHTML(text);
      }
    });

    Notification.activityStop();
  }

  @UiHandler("accept")
  void accept(ClickEvent e) {
    ServicesLocator.getServiceConnection().userAcceptsTermsOfService(null);
    SpMobil.displayMainPage();
  }

  @UiHandler("refuse")
  void refuse(ClickEvent e) {
    Notification.alert(msg.userRefuseTermsOfService());
    AuthentificationManager.getInstance().logout();
  }

}
