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

package org.silverpeas.mobile.client.apps.documents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.documents.events.app.*;
import org.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemPublishedEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemsLoadedEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationAttachmentsLoadedEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationPublishedEvent;
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
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.IframePage;
import org.silverpeas.mobile.client.components.Snackbar;
import org.silverpeas.mobile.client.components.attachments.AttachmentsManager;
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
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

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

    if (content.getType().equals(ContentsTypes.Publication.toString()) ||
        content.getType().equals(ContentsTypes.News.toString())) {
      getApplicationInstance().setAbleToStoreContent(true);
    }
  }

  private void loadAppInstance(final ContentDTO content, Command callback) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<ApplicationInstanceDTO>() {

      @Override
      public void attempt() {
        ServicesLocator.getServiceNavigation()
            .getApp(content.getInstanceId(), content.getId(), content.getType(), this);
      }

      @Override
      public void onSuccess(final Method method,
          final ApplicationInstanceDTO app) {
        super.onSuccess(method, app);
        setApplicationInstance(app);
        displayContent(content);
        callback.execute();
      }
    };
    action.attempt();
  }

  private void displayContent(final ContentDTO content) {
    if (content.getType().equals(ContentsTypes.Publication.toString()) ||
        content.getType().equals(ContentsTypes.News.toString())) {
      PublicationPage page = new PublicationPage();
      page.setPageTitle(msg.publicationTitle());
      page.setContent(content);
      setMainPage(page);
      page.show();
      EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(content));
    } else if (content.getType().equals(ContentsTypes.Component.toString())) {
      ApplicationInstanceDTO data = new ApplicationInstanceDTO();
      data.setId(content.getInstanceId());
      data.setType(Apps.kmelia.name());
      NavigationAppInstanceChangedEvent event = new NavigationAppInstanceChangedEvent(data);
      appInstanceChanged(event);
    } else if (content.getType().equals(ContentsTypes.Attachment.toString())) {
      MethodCallbackOnlineOnly action =
          new MethodCallbackOnlineOnly<AttachmentDTO>() {
            @Override
            public void attempt() {
              super.attempt();
              ServicesLocator.getServiceDocuments()
                  .getAttachment(content.getId(), content.getInstanceId(), this);
            }

            @Override
            public void onSuccess(final Method method, final AttachmentDTO attachement) {
              super.onSuccess(method, attachement);
              try {
                String url = Window.Location.getPath() + "spmobil/Attachment";
                url = url + "?id=" + attachement.getId() + "&instanceId=" +
                    attachement.getInstanceId() + "&lang=" + attachement.getLang() + "&userId=" +
                    attachement.getUserId();

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
          };
      action.attempt();
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
      page.setPersonnal(event.getInstance().getPersonnal());
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
        event.getContent().getType().equals(ContentsTypes.Folder.name())) {
        loadAppInstance(event.getContent(), new Command() {
          @Override
          public void execute() {
            startWithContent(event.getContent());
          }
        });
    } else if (event.getContent().getType().equals(ContentsTypes.Attachment.name())) {
      AttachmentsManager.viewDocument(event.getContent().getId(), event.getContent().getRole(), "");
    }
  }

  /**
   * Get subtopics.
   */
  @Override
  public void loadTopics(final DocumentsLoadGedItemsEvent event) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<BaseDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceDocuments()
            .getTopicsAndPublications(event.getInstanceId(), event.getRootTopicId(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<BaseDTO> result) {
        super.onSuccess(method, result);

        EventBus.getInstance().fireEvent(new GedItemsLoadedEvent(result, getApplicationInstance().getFolderSharing(),
                getCanImport(), event.isForceReload()));
      }
    };
    action.attempt();
  }

  private boolean getCanImport() {
    if (getApplicationInstance().getRights() == null) return true;
    //TODO : if writer manage validation and manage topic specific right
    boolean canImport = getApplicationInstance().getRights().getManager() ||
            getApplicationInstance().getRights().getPublisher();
    return canImport;
  }

  /**
   * Get publication infos.
   */
  @Override
  public void loadPublication(final DocumentsLoadPublicationEvent event) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<PublicationDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceDocuments()
            .getPublication(getApplicationInstance().getId(), event.getContent().getId(),
                event.getContent().getContributionId(), event.getContent().getType(), this);
      }

      @Override
      public void onSuccess(final Method method, final PublicationDTO result) {
        super.onSuccess(method, result);
        EventBus.getInstance().fireEvent(
            new PublicationLoadedEvent(result, getApplicationInstance().getCommentable(),
                getApplicationInstance().getAbleToStoreContent(),
                getApplicationInstance().getNotifiable(), getApplicationInstance().getPublicationSharing(),
                    event.getContent().getType(), getCanImport(), result.isPublishable()));
      }
    };
    action.attempt();
  }

  @Override
  public void loadAttachments(final DocumentsLoadAttachmentsEvent event) {
    MethodCallbackOnlineOnly action =
        new MethodCallbackOnlineOnly<List<SimpleDocumentDTO>>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getRestServiceDocuments()
                .getDocumentsByType(event.getInstanceId(), event.getPubId(),
                    DocumentType.attachment.name(), SpMobil.getUser().getLanguage(), this);
          }

          @Override
          public void onSuccess(final Method method, final List<SimpleDocumentDTO> attachments) {
            super.onSuccess(method, attachments);
            EventBus.getInstance().fireEvent(new PublicationAttachmentsLoadedEvent(attachments, getApplicationInstance().getFileSharing()));
          }
        };

    action.attempt();
  }

  @Override
  public void share(DocumentsSharingEvent event) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<TicketDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getRestServiceTickets().createTicket(event.getTicket().getComponentId(), event.getTicket(), this);
      }
      @Override
      public void onSuccess(Method method, TicketDTO dto) {
        super.onSuccess(method, dto);
        Snackbar.showWithCloseButton(dto.getUrl(), Snackbar.INFO);
      }
    };
    action.attempt();
  }

  @Override
  public void nextPublication(DocumentsNextPublicationEvent event) {
    ServicesLocator.getServiceDocuments().getNextPublication(event.getPublication().getInstanceId(), event.getPublication().getId(), event.getDirection(), new MethodCallback<PublicationDTO>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
      }

      @Override
      public void onSuccess(Method method, PublicationDTO publicationDTO) {
        PublicationPage page = new PublicationPage();
        page.setPageTitle(msg.publicationTitle());
        page.show();
        ContentDTO content = new ContentDTO();
        content.setId(publicationDTO.getId());
        content.setType(ContentsTypes.Publication.toString());
        EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(content));
      }
    });
  }

  @Override
  public void publish(DocumentsPublishEvent event) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<PublicationDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceDocuments().publish(event.getPublication().getInstanceId(),
                event.getPublication().getId(), this);
      }

      @Override
      public void onSuccess(Method method, PublicationDTO publication) {
        super.onSuccess(method, publication);
        EventBus.getInstance().fireEvent(new PublicationPublishedEvent(publication));
        // update folder
        EventBus.getInstance().fireEvent(new GedItemPublishedEvent(event.getPublication()));
      }
    };
    action.attempt();
  }
}
