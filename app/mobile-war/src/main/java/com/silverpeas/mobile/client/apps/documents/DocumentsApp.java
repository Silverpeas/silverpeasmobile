package com.silverpeas.mobile.client.apps.documents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.documents.events.app.AbstractDocumentsAppEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsAppEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadGedItemsEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.pages.GedNavigationPage;
import com.silverpeas.mobile.client.apps.documents.pages.PublicationPage;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.client.components.IframePage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.ContentDTO;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.ArrayList;
import java.util.List;

public class DocumentsApp extends App implements NavigationEventHandler, DocumentsAppEventHandler {

    private ApplicationMessages globalMsg;
    private DocumentsMessages msg;

    private boolean commentable, ableToStoreContent, notifiable;
    //private Anchor sourceLink;

    public DocumentsApp() {
        super();
        msg = GWT.create(DocumentsMessages.class);
        globalMsg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
        EventBus.getInstance().addHandler(AbstractDocumentsAppEvent.TYPE, this);
    }

    @Override
    public void start() {
      // no "super.start(lauchingPage);" this apps is used in another apps
    }

    @Override
    public void startWithContent(final ContentDTO content) {
      if (content.getType().equals(ContentsTypes.Folder.toString())) {
        GedNavigationPage page = new GedNavigationPage();
        page.setInstanceId(content.getInstanceId());
        page.setTopicId(content.getId());
        page.show();
      }

      if (content.getType().equals(ContentsTypes.Publication.toString())) {
        ableToStoreContent = true;
      }
      AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<ApplicationInstanceDTO>() {

        @Override
        public void attempt() {
          ServicesLocator.getServiceNavigation()
              .getApp(content.getInstanceId(), content.getId(), content.getType(), this);
        }

        @Override
        public void onSuccess(final ApplicationInstanceDTO app) {
          OfflineHelper.hideOfflineIndicator();
          commentable = app.isCommentable();
          notifiable = app.isNotifiable();
          displayContent(content);
        }
      };
      action.attempt();
    }

    private void displayContent(final ContentDTO content) {
        if (content.getType().equals(ContentsTypes.Publication.toString())) {
            PublicationPage page = new PublicationPage();
            page.setPageTitle(msg.publicationTitle());
            setMainPage(page);
            page.show();
            EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(content.getId()));
        } else if(content.getType().equals(ContentsTypes.Attachment.toString())) {
            final DocumentsApp app = this;
            ServicesLocator.getServiceDocuments().getAttachment(content.getId(), content.getInstanceId(), new AsyncCallback<AttachmentDTO>() {
                @Override
                public void onFailure(final Throwable caught) {
                    EventBus.getInstance().fireEvent(new ErrorEvent(caught));
                }

                @Override
                public void onSuccess(final AttachmentDTO attachement) {
                    try {
                        String url = Window.Location.getPath() + "spmobil/Attachment";
                        url = url + "?id=" + attachement.getId() + "&instanceId=" + attachement.getInstanceId() + "&lang=" + attachement.getLang() + "&userId=" + attachement.getUserId();

                        if (MobilUtils.isIOS()) {
                            IframePage page = new IframePage(url);
                            page.setPageTitle(attachement.getTitle());
                            page.show();
                        } else {
                            content.getLink().setHref(url);
                          content.getLink().setTarget("_self");
                            clickElement(content.getLink().getElement());
                        }

                    } catch (JavaScriptException e) {
                        Notification.alert(e.getMessage());
                    }
                }
            });
        }
    }

    private static native void clickElement(Element elem) /*-{
        elem.click();
    }-*/;

    @Override
    public void stop() {
        // never stop
    }

    @Override
    public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
      if (event.getInstance().getType().equals(Apps.kmelia.name())) {
        this.commentable = event.getInstance().isCommentable();
        this.notifiable = event.getInstance().isNotifiable();
        this.ableToStoreContent = event.getInstance().isAbleToStoreContent();
        GedNavigationPage page = new GedNavigationPage();
        page.setPageTitle(event.getInstance().getLabel());
        page.setInstanceId(event.getInstance().getId());
        page.setTopicId(null);
        page.show();
      }
    }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.Publication.name()) || event.getContent().getType().equals(ContentsTypes.Attachment.name()) || event.getContent().getType().equals(ContentsTypes.Folder.name())) {
      startWithContent(event.getContent());
    }
  }

  /**
     * Get subtopics.
     */
    @Override
    public void loadTopics(final DocumentsLoadGedItemsEvent event) {
        final String key = "topic_" + event.getInstanceId() + "_" + event.getRootTopicId();
        Command offlineAction = new Command() {

            @Override
            public void execute() {
                List<BaseDTO> result = LocalStorageHelper.load(key, List.class);
                if (result == null) {
                    result = new ArrayList<BaseDTO>();
                }
                EventBus.getInstance().fireEvent(new GedItemsLoadedEvent(result));
            }
        };

        AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<BaseDTO>>(offlineAction) {
            @Override
            public void attempt() {
                ServicesLocator.getServiceDocuments().getTopicsAndPublications(event.getInstanceId(), event.getRootTopicId(), this);
            }

            @Override
            public void onSuccess(List<BaseDTO> result) {
                super.onSuccess(result);
                LocalStorageHelper.store(key, List.class, result);
                EventBus.getInstance().fireEvent(new GedItemsLoadedEvent(result));
            }

        };
        action.attempt();
    }

    /**
     * Get publication infos.
     */
    @Override
    public void loadPublication(final DocumentsLoadPublicationEvent event) {
        final String key = "publication_" + event.getPubId();
        Command offlineAction = new Command() {
            @Override
            public void execute() {
                PublicationDTO result = LocalStorageHelper.load(key, PublicationDTO.class);
                if (result == null) {
                    result = new PublicationDTO();
                }
                EventBus.getInstance().fireEvent(new PublicationLoadedEvent(result, commentable, ableToStoreContent, notifiable));
            }
        };

        AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<PublicationDTO>(offlineAction) {
            @Override
            public void attempt() {
                ServicesLocator.getServiceDocuments().getPublication(event.getPubId(), this);
            }

            @Override
            public void onSuccess(PublicationDTO result) {
                super.onSuccess(result);
                LocalStorageHelper.store(key, PublicationDTO.class, result);
                EventBus.getInstance().fireEvent(new PublicationLoadedEvent(result, commentable, ableToStoreContent, notifiable));
            }

        };
        action.attempt();
    }

}
