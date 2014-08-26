package com.silverpeas.mobile.client.apps.media.resources;

import com.google.gwt.i18n.client.Messages;

public interface MediaMessages extends Messages {
  String title();
  String lastUpdate(String updateDate, String updater);
  String sizeK(String size);
  String sizeM(String size);
  String dimensions(String l, String h);

  String comments(String number);
  String comment();
  String noComment();

}
