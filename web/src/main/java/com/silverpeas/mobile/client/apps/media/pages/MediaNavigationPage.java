package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.media.events.app.MediaPreviewLoadEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediasLoadMediaItemsEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation
    .AbstractMediaNavigationPagesEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaItemClickEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaItemsLoadedEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation
    .MediaNavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.media.pages.widgets.AddMediaButton;
import com.silverpeas.mobile.client.apps.media.pages.widgets.MediaItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.media.AlbumDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;

import java.util.List;

public class MediaNavigationPage extends PageContent implements View, MediaNavigationPagesEventHandler {

	private static MediaNavigationPageUiBinder uiBinder = GWT.create(MediaNavigationPageUiBinder.class);
  @UiField
  UnorderedList list;

  private String rootAlbumId, instanceId;
  private boolean dataLoaded = false;

  interface MediaNavigationPageUiBinder extends UiBinder<Widget, MediaNavigationPage> {
	}

	public MediaNavigationPage() {
		initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractMediaNavigationPagesEvent.TYPE, this);
	}
	
	public void setAlbumId(String rootAlbumId) {
    Notification.activityStart();
    this.rootAlbumId = rootAlbumId;
    EventBus.getInstance().fireEvent(new MediasLoadMediaItemsEvent(instanceId, rootAlbumId));
	}
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

  @Override
  public void onLoadedAlbums(final MediaItemsLoadedEvent event) {
    Notification.activityStart();
    if (isVisible() && dataLoaded == false) {

      list.clear();
      list.add(new AddMediaButton()); //TODO : manage user rights
      List<BaseDTO> dataItems = event.getAlbumsAndMedias();
      for (BaseDTO dataItem : dataItems) {
        MediaItem item = new MediaItem();
        item.setData(dataItem);
        list.add(item);
      }
      dataLoaded = true;
    }
    Notification.activityStop();
  }

  @Override
  public void onMediaItemClicked(final MediaItemClickEvent event) {
    if (isVisible()) {
      if (event.getMediaItem() instanceof AlbumDTO) {
        MediaNavigationPage page = new MediaNavigationPage();
        page.setPageTitle(getPageTitle());
        page.setInstanceId(instanceId);
        page.setAlbumId(((AlbumDTO) event.getMediaItem()).getId());
        page.show();
      } else if (event.getMediaItem() instanceof PhotoDTO) {
        MediaPage page = new MediaPage();
        page.setPageTitle(getPageTitle());
        page.show();
        EventBus.getInstance().fireEvent(new MediaPreviewLoadEvent(instanceId, ((PhotoDTO) event.getMediaItem()).getId()));
      }
    }
  }

}
