package com.silverpeas.mobile.client.apps.notifications.resources;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface NotificationsMessages extends Messages {
  String notifyContent();

  SafeHtml message();
  SafeHtml recipients();


  SafeHtml modify();
  SafeHtml send();
  SafeHtml subject();
  SafeHtml notifiedContacts();
  String sended();

}
