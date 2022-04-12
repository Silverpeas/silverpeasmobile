/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.pages.termsofservice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.TextCallback;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
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

    SpMobil.getMainPage().hideFooter();

    ServicesLocator.getServiceTermsOfService().getContent(new TextCallback() {
      @Override
      public void onFailure(final Method method, final Throwable throwable) {
        EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
      }

      @Override
      public void onSuccess(final Method method, final String text) {
        terms.setHTML(text);
      }
    });

    Notification.activityStop();
  }

  @UiHandler("accept")
  void accept(ClickEvent e) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceConnection().userAcceptsTermsOfService(this);
      }

      @Override
      public void onSuccess(final Method method, final Void unused) {
        super.onSuccess(method, unused);
        SpMobil.getMainPage().showFooter();
        SpMobil.displayMainPage();
      }
    };
    action.attempt();
  }

  @UiHandler("refuse")
  void refuse(ClickEvent e) {
    Notification.alert(msg.userRefuseTermsOfService());
    AuthentificationManager.getInstance().logout();
  }

}
