package com.silverpeas.mobile.client.apps.comments.resources;

import com.google.gwt.i18n.client.Messages;

public interface CommentsMessages extends Messages {

  String comments(String number);
  String commentsTitle();
  String comment();
  String noComment();

  String commentsPageTitle(String publicationTitle);
  String addComment();

}
