package com.silverpeas.mobile.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author: svu
 */
public interface ApplicationResources extends ClientBundle {

  @Source("avatar.png")
  ImageResource avatar();

  @Source("sound.png")
  ImageResource sound();

  @Source("video.png")
  ImageResource video();

  @Source("streaming.png")
  ImageResource streaming();
}
