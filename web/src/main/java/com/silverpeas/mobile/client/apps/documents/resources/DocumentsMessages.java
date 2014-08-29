package com.silverpeas.mobile.client.apps.documents.resources;

import com.google.gwt.i18n.client.Messages;

public interface DocumentsMessages extends Messages {
  String title();

  // Publication page
  String lastUpdate(String updateDate, String updater);
  String publicationTitle();
  String sizeK(String size);
  String sizeM(String size);



}
