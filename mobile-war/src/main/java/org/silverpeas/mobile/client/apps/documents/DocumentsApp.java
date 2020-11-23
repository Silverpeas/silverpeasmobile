/*
 * Copyright (C) 2000 - 2020 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.apps.documents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.documents.events.app.AbstractDocumentsAppEvent;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsAppEventHandler;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadAttachmentsEvent;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadGedItemsEvent;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemsLoadedEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationAttachmentsLoadedEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import org.silverpeas.mobile.client.apps.documents.pages.GedNavigationPage;
import org.silverpeas.mobile.client.apps.documents.pages.PublicationPage;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.components.IframePage;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import org.silverpeas.mobile.shared.dto.documents.DocumentType;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.documents.SimpleDocumentDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.ArrayList;
import java.util.List;

public class DocumentsApp extends App implements NavigationEventHandler, DocumentsAppEventHandler {

    private ApplicationMessages globalMsg;
    private DocumentsMessages msg;

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

      if (content.getType().equals(ContentsTypes.Publication.toString()) || content.getType().equals(ContentsTypes.News.toString())) {
        getApplicationInstance().setAbleToStoreContent(true);
      }
    }

  private void loadAppInstance(final ContentDTO content, Command callback) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<ApplicationInstanceDTO>() {

      @Override
      public void attempt() {
        ServicesLocator.getServiceNavigation()
            .getApp(content.getInstanceId(), content.getId(), content.getType(), this);
      }

      @Override
      public void onSuccess(final ApplicationInstanceDTO app) {
        super.onSuccess(app);
        setApplicationInstance(app);
        displayContent(content);
        callback.execute();
      }
    };
    action.attempt();
  }

  private void displayContent(final ContentDTO content) {
        if (content.getType().equals(ContentsTypes.Publication.toString()) || content.getType().equals(ContentsTypes.News.toString())) {
            PublicationPage page = new PublicationPage();
            page.setPageTitle(msg.publicationTitle());
            page.setContent(content);
            setMainPage(page);
            page.show();
            EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(content));
        } else if(content.getType().equals(ContentsTypes.Attachment.toString())) {
            final DocumentsApp app = this;
            ServicesLocator.getServiceDocuments().getAttachment(content.getId(), content.getInstanceId(), new AsyncCallback<AttachmentDTO>() {
                @Override
                public void onFailure(final Throwable caught) {
                    Notification.activityStop();
                    EventBus.getInstance().fireEvent(new ErrorEvent(caught));
                }

                @Override
                public void onSuccess(final AttachmentDTO attachement) {
                    Notification.activityStop();
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
        setApplicationInstance(event.getInstance());
        GedNavigationPage page = new GedNavigationPage();
        page.setPageTitle(event.getInstance().getLabel());
        page.setInstanceId(event.getInstance().getId());
        page.setTopicId(null);
        page.show();
      }
    }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals("Component") &&
        event.getContent().getInstanceId().startsWith(Apps.kmelia.name())) {
      loadAppInstance(event.getContent(), new Command() {
        @Override
        public void execute() {
          DocumentsApp.super.showContent(event);
        }
      });
    } else if (event.getContent().getType().equals(ContentsTypes.Publication.name()) ||
        event.getContent().getType().equals(ContentsTypes.News.name()) ||
        event.getContent().getType().equals(ContentsTypes.Attachment.name()) ||
        event.getContent().getType().equals(ContentsTypes.Folder.name())) {
      loadAppInstance(event.getContent(), new Command() {
        @Override
        public void execute() {
          startWithContent(event.getContent());
        }
      });
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
        final String key = "publication_" + event.getContent().getId();
        Command offlineAction = new Command() {
            @Override
            public void execute() {
                PublicationDTO result = LocalStorageHelper.load(key, PublicationDTO.class);
                if (result == null) {
                    result = new PublicationDTO();
                }
                EventBus.getInstance().fireEvent(new PublicationLoadedEvent(result, getApplicationInstance().isCommentable(), getApplicationInstance().isAbleToStoreContent(), getApplicationInstance().isNotifiable(), event.getContent().getType()));
            }
        };

        AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<PublicationDTO>(offlineAction) {
            @Override
            public void attempt() {
                ServicesLocator.getServiceDocuments().getPublication(event.getContent(), this);
            }

            @Override
            public void onSuccess(PublicationDTO result) {
                super.onSuccess(result);
                LocalStorageHelper.store(key, PublicationDTO.class, result);
                EventBus.getInstance().fireEvent(new PublicationLoadedEvent(result, getApplicationInstance().isCommentable(), getApplicationInstance().isAbleToStoreContent(), getApplicationInstance().isNotifiable(), event.getContent().getType()));
            }

        };
        action.attempt();
    }

  @Override
  public void loadAttachments(final DocumentsLoadAttachmentsEvent event) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<SimpleDocumentDTO>>() {
      @Override
      public void attempt() {
        ServicesLocator.getRestServiceDocuments().getDocumentsByType(getApplicationInstance().getId(), event.getPubId(),
            DocumentType.attachment.name(), SpMobil.getUser().getLanguage(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<SimpleDocumentDTO> attachments) {
        EventBus.getInstance().fireEvent(new PublicationAttachmentsLoadedEvent(attachments));
      }
    };

    action.attempt();
  }

}
