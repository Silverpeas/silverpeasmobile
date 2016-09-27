package com.silverpeas.mobile.client.apps.documents.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.comments.pages.widgets.CommentsButton;
import com.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.AbstractPublicationPagesEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationNavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.documents.pages.widgets.Attachment;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.notifications.pages.widgets.NotifyButton;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.common.navigation.UrlUtils;
import com.silverpeas.mobile.client.components.IframePage;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.ActionsMenu;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

public class PublicationPage extends PageContent implements View, PublicationNavigationPagesEventHandler {

  private static PublicationPageUiBinder uiBinder = GWT.create(PublicationPageUiBinder.class);

  private PublicationDTO publication;

  @UiField HeadingElement title;
  @UiField HTMLPanel container;
  @UiField UnorderedList attachments;
  @UiField ParagraphElement desc, lastUpdate;
  @UiField CommentsButton comments;
  @UiField Anchor contentLink;
  @UiField DivElement content;
  @UiField ActionsMenu actionsMenu;

  @UiField(provided = true) protected DocumentsMessages msg = null;

  private NotifyButton notification = new NotifyButton();

  interface PublicationPageUiBinder extends UiBinder<Widget, PublicationPage> {
  }

  public PublicationPage() {
    msg = GWT.create(DocumentsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("publication");
    attachments.getElement().setId("attachments");
    content.setId("content");
    EventBus.getInstance().addHandler(AbstractPublicationPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    comments.stop();
    EventBus.getInstance().removeHandler(AbstractPublicationPagesEvent.TYPE, this);
  }

  public void setPublicationId(String id) {
    // send event to controler for retrieve pub infos
    Notification.activityStart();
    EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(id));
  }

  @Override
  public void onLoadedPublication(PublicationLoadedEvent event) {
    Notification.activityStop();
    this.publication = event.getPublication();
    display(event.isCommentable(), event.isAbleToStoreContent());
    if (event.isNotifiable()) {
      actionsMenu.addAction(notification);
    }
    contentLink.setVisible(publication.getContent());
  }

  /**
   * Refesh view informations.
   */
  private void display(boolean commentable, boolean ableToStoreContent) {
    if (isVisible()) {
      title.setInnerHTML(publication.getName());
      desc.setInnerHTML(publication.getDescription());
      if (publication.getUpdater() != null && publication.getUpdateDate() != null) lastUpdate.setInnerHTML(msg.lastUpdate(publication.getUpdateDate(), publication.getUpdater()));

      for (AttachmentDTO attachment : publication.getAttachments()) {
        Attachment a = new Attachment();
        a.setAttachment(attachment);
        attachments.add(a);
      }
      if (commentable) {
        comments.init(publication.getId(), publication.getInstanceId(), CommentDTO.TYPE_PUBLICATION,
            getPageTitle(), publication.getName(), publication.getCommentsNumber());
        comments.getElement().getStyle().clearDisplay();
      } else {
        comments.getElement().getStyle().setDisplay(Style.Display.NONE);
      }
      if (ableToStoreContent) {
        content.getStyle().clearDisplay();
      } else {
        content.getStyle().setDisplay(Style.Display.NONE);
      }
    }
    notification.init(publication.getInstanceId(), publication.getId(), NotificationDTO.TYPE_PUBLICATION, publication.getName(), getPageTitle());
  }

  @UiHandler("contentLink")
  protected void showContent(ClickEvent event) {

    // compute height available for content
    int available = Window.getClientHeight() - SpMobil.mainPage.getHeaderHeight();

    // display content
    String url = UrlUtils.getLocation();
    url += "spmobil/PublicationContent";
    url += "?id=" + publication.getId();
    IframePage page = new IframePage(url);
    page.setSize("100%", available + "px");
    page.setPageTitle(msg.content());
    page.show();
  }
}
