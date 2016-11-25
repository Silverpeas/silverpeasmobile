package com.silverpeas.mobile.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsCSS;

/**
 * @author: svu
 */
public interface ApplicationResources extends ClientBundle {

  @Source("application.css")
  ApplicationCSS css();

  @Source("avatar.png")
  ImageResource avatar();

  @Source("sound.png")
  ImageResource sound();

  @Source("video.png")
  ImageResource video();

  @Source("streaming.png")
  ImageResource streaming();

  @Source("offline.png")
  ImageResource offline();
}
