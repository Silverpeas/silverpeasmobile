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

package org.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.media.events.app.MediaViewShowEvent;
import org.silverpeas.mobile.client.apps.media.events.app.MediasLoadMediaItemsEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.navigation
    .AbstractMediaNavigationPagesEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaItemClickEvent;
import org.silverpeas.mobile.client.apps.media.events.pages.navigation
    .MediaNavigationPagesEventHandler;
import org.silverpeas.mobile.client.apps.media.pages.widgets.AddMediaButton;
import org.silverpeas.mobile.client.apps.media.pages.widgets.AlbumItem;
import org.silverpeas.mobile.client.apps.media.pages.widgets.MediaItem;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.common.navigation.LinksManager;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.LoadingItem;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.events.page.DataLoadedEvent;
import org.silverpeas.mobile.client.components.base.events.page.LoadingDataFinishEvent;
import org.silverpeas.mobile.client.components.base.events.page.MoreDataLoadedEvent;
import org.silverpeas.mobile.client.components.base.widgets.ShareButton;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.RightDTO;
import org.silverpeas.mobile.shared.dto.media.AlbumDTO;
import org.silverpeas.mobile.shared.dto.media.MediaDTO;

import java.util.List;

public class MediaNavigationPage extends PageContent implements View, MediaNavigationPagesEventHandler {

  private static MediaNavigationPageUiBinder uiBinder = GWT.create(MediaNavigationPageUiBinder.class);
  @UiField UnorderedList list;
  private AddMediaButton buttonImport = new AddMediaButton();
  private LoadingItem endline = new LoadingItem();
  @UiField ActionsMenu actionsMenu;

  private String rootAlbumId, instanceId;
  private RightDTO rights;
  private AlbumDTO root;
  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private ShareButton share = new ShareButton();

  interface MediaNavigationPageUiBinder extends UiBinder<Widget, MediaNavigationPage> {
  }

  public MediaNavigationPage() {
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractMediaNavigationPagesEvent.TYPE, this);
  }

  public void init(String instanceId, String rootAlbumId, RightDTO rights) {
    Notification.activityStart();
    this.instanceId = instanceId;
    this.rootAlbumId = rootAlbumId;
    this.rights = rights;
    buttonImport.init(instanceId, rootAlbumId);
    EventBus.getInstance().fireEvent(new MediasLoadMediaItemsEvent(instanceId, rootAlbumId));
  }

  @Override
  public void loadedDataEvent(final DataLoadedEvent event) {
    Notification.activityStart();
    if (isVisible() && ((event.getLocationId() == null && rootAlbumId == null) || event.getLocationId().equals(rootAlbumId))) {
      list.clear();
      if (rights.getWriter() || rights.getPublisher() || rights.getManager()) {
        if (rootAlbumId != null) list.add(buttonImport);
      }
      List<BaseDTO> dataItems = event.getData();
      populateList(dataItems);
      list.add(endline);

      actionsMenu.addAction(favorite);
      actionsMenu.addAction(share);
      if (root.getId() == null) {
        favorite.init(instanceId, instanceId, ContentsTypes.Component.name(), root.getName());
        share.init(root.getName(), root.getName(), LinksManager.createApplicationPermalink(instanceId));
      } else {
        favorite.init(instanceId, root.getId(), ContentsTypes.Album.name(), root.getName());
        share.init(root.getName(), root.getName(),LinksManager.createAlbumPermalink(instanceId, root.getId()));
      }
    }
    Notification.activityStop();
  }

  @Override
  public void loadedMoreDataEvent(final MoreDataLoadedEvent event) {
    if (isVisible() && ((event.getLocationId() == null && rootAlbumId == null) ||
        event.getLocationId() .equals(rootAlbumId))) {
      List<BaseDTO> dataItems = event.getData();
      list.remove(list.getWidgetCount() - 1);
      populateList(dataItems);
      list.add(endline);
    }
  }


  @Override
  public void finishLoadingData(final LoadingDataFinishEvent loadingDataFinishEvent) {
    if (isVisible()) {
      endline.hide();
    }
  }

  private void populateList(final List<BaseDTO> dataItems) {
    for (BaseDTO dataItem : dataItems) {
      if (dataItem instanceof AlbumDTO) {
        if (((AlbumDTO) dataItem).getRoot()) {
          setPageTitle(((AlbumDTO) dataItem).getName());
          root = (AlbumDTO) dataItem;
        } else {
          AlbumItem item = new AlbumItem();
          item.setData((AlbumDTO) dataItem);
          list.add(item);
        }
      } else if (dataItem instanceof MediaDTO) {
        MediaItem item = new MediaItem();
        item.setData((MediaDTO) dataItem);
        list.add(item);
      }
    }
  }

  @Override
  public void onMediaItemClicked(final MediaItemClickEvent event) {
    if (isVisible()) {
      if (event.getMediaItem() instanceof AlbumDTO) {
        showAlbum((AlbumDTO) event.getMediaItem());
      }
      else {
        EventBus.getInstance().fireEvent(new MediaViewShowEvent((MediaDTO)event.getMediaItem()));
      }
    }
  }

  private void showAlbum(final AlbumDTO album) {
    MediaNavigationPage page = new MediaNavigationPage();
    page.setApp(getApp());
    page.init(instanceId, album.getId(), rights);
    page.show();
  }

}
