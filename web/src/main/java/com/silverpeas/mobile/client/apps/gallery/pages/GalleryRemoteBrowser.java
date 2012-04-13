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

package com.silverpeas.mobile.client.apps.gallery.pages;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;
import com.silverpeas.mobile.client.apps.gallery.events.controller.RemotePicturesLoadEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.AbstractRemotePicturesPageEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.RemotePictureLoadedEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.remote.RemotePicturesPageEventHandler;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryResources;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.shared.dto.gallery.PhotoDTO;

public class GalleryRemoteBrowser extends Page implements View, RemotePicturesPageEventHandler {

  private static GalleryRemoteBrowserUiBinder uiBinder =
      GWT.create(GalleryRemoteBrowserUiBinder.class);
  private String galleryId;
  private String albumId;
  private List<PhotoDTO> photos = null;

  @UiField(provided = true)
  protected GalleryResources ressources = null;

  @UiField
  FlexTable content;

  public void setGalleryId(String galleryId) {
    this.galleryId = galleryId;
  }

  public void setAlbumId(String albumId) {
    this.albumId = albumId;
  }

  interface GalleryRemoteBrowserUiBinder extends
      UiBinder<Widget, GalleryRemoteBrowser> {
  }

  public GalleryRemoteBrowser() {
    ressources = GWT.create(GalleryResources.class);
    ressources.css().ensureInjected();
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void init() {
    EventBus.getInstance().addHandler(AbstractRemotePicturesPageEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new RemotePicturesLoadEvent(galleryId, albumId));
  }

  @Override
  public void onPicturesLoaded(RemotePictureLoadedEvent event) {
    this.photos = event.getPhotos();
    if (photos.isEmpty()) {
      content.add(new Label("no pictures"));
    } else {
      int index = 0;
      for (PhotoDTO photo : photos) {
        Image photoW = new Image(photo.getDataPhoto());
        photoW.getElement().setId(photo.getId());
        photoW.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            PictureViewerPage viewer = new PictureViewerPage();
            viewer.init(galleryId, ((Image) event.getSource()).getElement().getId());
            goTo(viewer, Transition.SLIDE);
          }
        });

        content.setWidget(index / 3, index % 3, photoW);
        content.getFlexCellFormatter().setHorizontalAlignment(index / 3, index % 3,
            HasHorizontalAlignment.ALIGN_CENTER);
        content.getFlexCellFormatter().setVerticalAlignment(index / 3, index % 3,
            HasVerticalAlignment.ALIGN_MIDDLE);
        index++;
      }
    }
  }

  @Override
  public void goBack(Object returnValue) {
    stop();
    super.goBack(returnValue);
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractRemotePicturesPageEvent.TYPE, this);
  }
}