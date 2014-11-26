package com.silverpeas.mobile.client.apps.documents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.silverpeas.mobile.client.apps.documents.events.app.AbstractDocumentsAppEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsAppEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadGedItemsEvent;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.pages.GedNavigationPage;
import com.silverpeas.mobile.client.apps.documents.pages.PublicationPage;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.components.IframePage;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

import java.util.List;

public class DocumentsApp extends App implements NavigationEventHandler, DocumentsAppEventHandler {

  private DocumentsMessages msg;
  private NavigationApp navApp = new NavigationApp();
  private boolean commentable;
  private Anchor sourceLink;

  public DocumentsApp() {
    super();
    msg = GWT.create(DocumentsMessages.class);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractDocumentsAppEvent.TYPE, this);
  }

  public void setSourceLink(Anchor source) {
    this.sourceLink = source;
  }

  @Override
  public void start() {
    navApp.setTypeApp(Apps.kmelia.name());
    navApp.setTitle(msg.title());
    navApp.start();

    // app main is navigation app main page
    setMainPage(navApp.getMainPage());

    super.start();
  }

  @Override
  public void startWithContent(final String appId, final String contentType, final String contentId) {
    ServicesLocator.serviceNavigation.getApp(appId, new AsyncCallback<ApplicationInstanceDTO>() {
      @Override
      public void onFailure(final Throwable caught) {
        EventBus.getInstance().fireEvent(new ErrorEvent(caught));
      }

      @Override
      public void onSuccess(final ApplicationInstanceDTO app) {
        commentable = app.isCommentable();
        displayContent(appId, contentType, contentId);
      }
    });
  }

  private void displayContent(String appId, String contentType, String contentId) {
    if (contentType.equals(ContentsTypes.Publication.toString())) {
      PublicationPage page = new PublicationPage();
      page.setPageTitle(msg.publicationTitle());
      setMainPage(page);
      page.show();
      EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(contentId));
    } else if(contentType.equals(ContentsTypes.Attachment.toString())) {
      //TODO : implement attachments results display
      final DocumentsApp app = this;
      ServicesLocator.serviceDocuments.getAttachment(contentId, appId, new AsyncCallback<AttachmentDTO>() {
        @Override
        public void onFailure(final Throwable caught) {
          EventBus.getInstance().fireEvent(new ErrorEvent(caught));
        }

        @Override
        public void onSuccess(final AttachmentDTO attachement) {
          try {
            String url = Window.Location.getPath() + "spmobil/Attachment";
            url = url + "?id=" + attachement.getId() + "&instanceId=" + attachement.getInstanceId() + "&lang=" + attachement.getLang()  + "&userId=" + attachement.getUserId();

            if (MobilUtils.isIOS()) {
              IframePage page = new IframePage(url);
              page.setPageTitle(attachement.getTitle());
              page.show();
            } else {
              sourceLink.setHref(url);
              sourceLink.setTarget("_self");
              clickElement(sourceLink.getElement());
              app.stop();
            }

          } catch(JavaScriptException e) {
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
    EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
    EventBus.getInstance().removeHandler(AbstractDocumentsAppEvent.TYPE, this);
    navApp.stop();
    super.stop();
  }

  @Override
  public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
    this.commentable = event.getInstance().isCommentable();
    GedNavigationPage page = new GedNavigationPage();
    page.setPageTitle(msg.title());
    page.setInstanceId(event.getInstance().getId());
    page.setTopicId(null);
    page.show();
  }

  /**
   * Get subtopics.
   */
  @Override
  public void loadTopics(DocumentsLoadGedItemsEvent event) {
    ServicesLocator.serviceDocuments.getTopicsAndPublications(event.getInstanceId(), event.getRootTopicId(), new AsyncCallback<List<BaseDTO>>() {
      @Override
      public void onSuccess(List<BaseDTO> result) {
        EventBus.getInstance().fireEvent(new GedItemsLoadedEvent(result));
      }
      @Override
      public void onFailure(Throwable caught) {
        EventBus.getInstance().fireEvent(new ErrorEvent(caught));
      }
    });
  }

  /**
   * Get publication infos.
   */
  @Override
  public void loadPublication(DocumentsLoadPublicationEvent event) {
    ServicesLocator.serviceDocuments.getPublication(event.getPubId(), new AsyncCallback<PublicationDTO>() {
      @Override
      public void onSuccess(PublicationDTO result) {
        EventBus.getInstance().fireEvent(new PublicationLoadedEvent(result, commentable));
      }
      @Override
      public void onFailure(Throwable caught) {
        EventBus.getInstance().fireEvent(new ErrorEvent(caught));
      }
    });
  }

}
