package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.media.events.app.MediaPreviewLoadEvent;
import com.silverpeas.mobile.client.apps.media.events.app.MediasLoadMediaItemsEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.AbstractMediaNavigationPagesEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaItemClickEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaItemsLoadedEvent;
import com.silverpeas.mobile.client.apps.media.events.pages.navigation.MediaNavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.media.pages.widgets.AddMediaButton;
import com.silverpeas.mobile.client.apps.media.pages.widgets.MediaItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.RightDTO;
import com.silverpeas.mobile.shared.dto.media.AlbumDTO;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;

import java.util.List;

public class MediaNavigationPage extends PageContent implements View, MediaNavigationPagesEventHandler {

	private static MediaNavigationPageUiBinder uiBinder = GWT.create(MediaNavigationPageUiBinder.class);
  @UiField UnorderedList list;
  private AddMediaButton buttonImport= new AddMediaButton();

  private String rootAlbumId, instanceId;
  private RightDTO rights;

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
  public void onLoadedAlbums(final MediaItemsLoadedEvent event) {
    Notification.activityStart();
    if (isVisible()) {
      list.clear();
      if (rights.isWriter() || rights.isPublisher() || rights.isManager()) {
        if (rootAlbumId != null) list.add(buttonImport);
      }
      List<BaseDTO> dataItems = event.getAlbumsAndMedias();
      for (BaseDTO dataItem : dataItems) {
        MediaItem item = new MediaItem();
        item.setData(dataItem);
        list.add(item);
      }
    }
    Notification.activityStop();
  }

  @Override
  public void onMediaItemClicked(final MediaItemClickEvent event) {
    if (isVisible()) {
      if (event.getMediaItem() instanceof AlbumDTO) {
        MediaNavigationPage page = new MediaNavigationPage();
        page.setPageTitle(getPageTitle());
        page.init(instanceId, ((AlbumDTO) event.getMediaItem()).getId(), rights);
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
