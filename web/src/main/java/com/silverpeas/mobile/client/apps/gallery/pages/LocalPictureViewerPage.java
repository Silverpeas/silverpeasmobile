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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.Slide;
import com.gwtmobile.ui.client.widgets.SlidePanel;
import com.silverpeas.mobile.client.apps.gallery.events.controller.DeleteLocalPictureEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.local.AbstractLocalPicturesPageEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.local.DeletedLocalPictureEvent;
import com.silverpeas.mobile.client.apps.gallery.events.pages.local.LocalPicturesPageEventHandler;
import com.silverpeas.mobile.client.apps.gallery.persistances.Picture;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryMessages;
import com.silverpeas.mobile.client.apps.gallery.resources.GalleryResources;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.common.mobil.Orientation;

/**
 * Local pictures browser on device.
 * @author svuillet
 */
public class LocalPictureViewerPage extends Page implements View, LocalPicturesPageEventHandler,
    ValueChangeHandler<Boolean> {

  private static LocalPictureViewerPageUiBinder uiBinder =
      GWT.create(LocalPictureViewerPageUiBinder.class);
  private static int nbPictures = 0;
  private PageResizeHandler pageResizeHandler = new PageResizeHandler();
  @UiField
  SlidePanel content;
  @UiField
  InlineHTML number;
  @UiField
  Button prevButton, nextButton, delButton;
  @UiField(provided = true)
  protected GalleryMessages msg = null;
  @UiField(provided = true)
  protected GalleryResources ressources = null;

  interface LocalPictureViewerPageUiBinder extends UiBinder<Widget, LocalPictureViewerPage> {
  }

  private class PageResizeHandler implements ResizeHandler {
    @Override
    public void onResize(ResizeEvent event) {
      Image img = (Image) content.getSlide(content.getCurrentSlideIndex()).iterator().next();
      if (MobilUtils.getOrientation().equals(Orientation.Landscape)) {
        img.setSize("auto", "100%");
      } else {
        img.setSize("100%", "auto");
      }
    }
  }

  /**
   * Construct page.
   */
  public LocalPictureViewerPage() {
    ressources = GWT.create(GalleryResources.class);
    ressources.css().ensureInjected();
    msg = GWT.create(GalleryMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    int size = delButton.getText().length();
    delButton.setWidth(size + "em");
    EventBus.getInstance().addHandler(AbstractLocalPicturesPageEvent.TYPE, this);
    content.addValueChangeHandler(this);
    Window.addResizeHandler(pageResizeHandler);
  }

  /**
   * Set pictures to browse.
   * @param pictures
   */
  public void setPictures(Picture[] pictures) {
    nbPictures = pictures.length;
    for (int i = 0; i < pictures.length; i++) {
      Picture picture = pictures[i];
      Image image = new Image(picture.getURI());
      image.setWidth("100%");
      Slide s = new Slide();
      s.addStyleName(ressources.css().localPicture());
      s.getElement().setId(picture.getId());
      s.add(image);
      content.add(s);
    }
    displayPicturesNumber();
  }

  private void displayPicturesNumber() {
    int index = content.getCurrentSlideIndex() + 1;
    number.setHTML("&nbsp;" + index + "/" + nbPictures);
  }

  /**
   * Invoke delete order.
   */
  private void deletePicture() {
    com.gwtmobile.phonegap.client.Notification.confirm(msg.localPicture_deleteConfirmation(),
        new com.gwtmobile.phonegap.client.Notification.ConfirmCallback() {
          public void onComplete(int selection) {
            if (selection == 1) {
              int index = content.getCurrentSlideIndex();
              Slide currentSlide = content.getSlide(index);
              EventBus.getInstance().fireEvent(
                  new DeleteLocalPictureEvent(currentSlide.getElement().getId()));
            }
          }
                }, "Confirm", "OK,Cancel");
  }

  @UiHandler("delButton")
  void delPicture(ClickEvent e) {
    deletePicture();
  }

  @UiHandler("prevButton")
  void previousPicture(ClickEvent e) {
    content.previous();
  }

  @UiHandler("nextButton")
  void nextPicture(ClickEvent e) {
    content.next();
  }

  /**
   * Picture has been deleted.
   */
  @Override
  public void onDeletedLocalPicture(DeletedLocalPictureEvent event) {
    int index = content.getCurrentSlideIndex();
    Slide currentSlide = content.getSlide(index);

    content.remove(currentSlide);
    if (event.isNoMorePicture()) {
      goBack(null);
    } else {
      content.next();
    }
    nbPictures--;
    displayPicturesNumber();
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractLocalPicturesPageEvent.TYPE, this);
  }

  @Override
  public void goBack(Object returnValue) {
    stop();
    super.goBack(returnValue);
  }

  /**
   * Refresh current picture position on picture change.
   */
  @Override
  public void onValueChange(ValueChangeEvent<Boolean> event) {
    displayPicturesNumber();
    pageResizeHandler.onResize(null);
  }
}
