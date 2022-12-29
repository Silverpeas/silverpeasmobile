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

package org.silverpeas.mobile.client.apps.documents.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.comments.pages.widgets.CommentsButton;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadAttachmentsEvent;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.publication.AbstractPublicationPagesEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationAttachmentsLoadedEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationNavigationPagesEventHandler;
import org.silverpeas.mobile.client.apps.documents.pages.widgets.LinkedPublicationItem;
import org.silverpeas.mobile.client.apps.documents.pages.widgets.ShareButton;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.notifications.pages.widgets.NotifyButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.PublicationContentHelper;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.attachments.Attachment;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.documents.SimpleDocumentDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;

public class PublicationPage extends PageContent
    implements View, PublicationNavigationPagesEventHandler {

  private static PublicationPageUiBinder uiBinder = GWT.create(PublicationPageUiBinder.class);

  private PublicationDTO publication;

  @UiField
  HeadingElement title;
  @UiField
  HTMLPanel container;
  @UiField
  UnorderedList attachments, linkedPublications;
  @UiField
  ParagraphElement desc, lastUpdate, creator, nbViews;
  @UiField
  CommentsButton comments;
  @UiField
  Anchor contentLink;
  @UiField
  DivElement content;
  @UiField
  ActionsMenu actionsMenu;

  @UiField(provided = true)
  protected DocumentsMessages msg;

  private NotifyButton notification = new NotifyButton();
  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  private ShareButton share = new ShareButton();
  private ContentDTO contentDTO = null;


  interface PublicationPageUiBinder extends UiBinder<Widget, PublicationPage> {}

  public PublicationPage() {
    msg = GWT.create(DocumentsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("publication");
    attachments.getElement().setId("attachments");
    linkedPublications.getElement().setId("linkedPublications");
    content.setId("content");
    EventBus.getInstance().addHandler(AbstractPublicationPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    comments.stop();
    EventBus.getInstance().removeHandler(AbstractPublicationPagesEvent.TYPE, this);
  }

  public void setContent(final ContentDTO content) {
    this.contentDTO = content;
  }

  public void setPublicationId(String id, String type) {
    // send event to controler for retrieve pub infos
    Notification.activityStart();

    ContentDTO content = new ContentDTO();
    content.setId(id);
    content.setType(type);
    EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(content));
  }

  @Override
  public void onLoadedPublication(PublicationLoadedEvent event) {
    Notification.activityStop();
    this.publication = event.getPublication();
    display(event.isCommentable(), event.isAbleToStoreContent(), event.getType());
    actionsMenu.addAction(favorite);
    if (event.isNotifiable()) {
      actionsMenu.addAction(notification);
    }

    if (event.getSharing() > 0) {
      share.init(event.getSharing(), event.getPublication().getInstanceId(),
              event.getPublication().getId(), "Publication", "", "");
      actionsMenu.addAction(share);
    }

    if (Boolean.parseBoolean(ResourcesManager.getParam("content.display.embedded")) && publication.getContent()) {
      PublicationContentHelper.showContent(publication.getId(), publication.getInstanceId(), content);
    }
    contentLink.setVisible(publication.getContent());
  }

  @Override
  public void onLoadedPublicationAttachments(final PublicationAttachmentsLoadedEvent event) {
    for (SimpleDocumentDTO attachment : event.getAttachments()) {
      Attachment a = new Attachment();
      a.setAttachment(attachment);
      a.setSharing(event.getShare());
      attachments.add(a);
    }
  }

  /**
   * Refesh view informations.
   */
  private void display(boolean commentable, boolean ableToStoreContent, String type) {
    if (isVisible()) {
      EventBus.getInstance().fireEvent(new DocumentsLoadAttachmentsEvent(publication.getId(), publication.getInstanceId()));
      title.setInnerHTML(publication.getName());
      desc.setInnerHTML(publication.getDescription());

      if (publication.getViewsNumber() == 0) {
        nbViews.setInnerHTML(msg.noview());
      } else if (publication.getViewsNumber() == 1) {
        nbViews.setInnerHTML(msg.view());
      } else {
        nbViews.setInnerHTML(msg.views(publication.getViewsNumber()));
      }

      creator.setInnerHTML(msg.creation(publication.getCreationDate(), publication.getCreator()));

      if (publication.getUpdater() != null && publication.getUpdateDate() != null) {
        lastUpdate
            .setInnerHTML(msg.lastUpdate(publication.getUpdateDate(), publication.getUpdater()));
      }

      if (commentable) {
        String id = publication.getId();
        if (contentDTO != null && contentDTO.getContributionId() != null) {
          id = contentDTO.getContributionId();
        }
        comments.init(id, publication.getInstanceId(), type, getPageTitle(), publication.getName(),
            publication.getCommentsNumber());
        comments.getElement().getStyle().clearDisplay();
      } else {
        comments.getElement().getStyle().setDisplay(Style.Display.NONE);
      }
      if (ableToStoreContent) {
        content.getStyle().clearDisplay();
      } else {
        content.getStyle().setDisplay(Style.Display.NONE);
      }
      // links
      for (PublicationDTO linkedPublication : publication.getLinkedPublications()) {
        LinkedPublicationItem item = new LinkedPublicationItem();
        item.setData(linkedPublication);
        linkedPublications.add(item);
      }
    }
    notification
        .init(publication.getInstanceId(), publication.getId(), NotificationDTO.TYPE_PUBLICATION,
            publication.getName(), getPageTitle());
    favorite
        .init(publication.getInstanceId(), publication.getId(), ContentsTypes.Publication.name(),
            publication.getName());
  }

  @UiHandler("contentLink")
  protected void showContent(ClickEvent event) {
    showPublicationContent(publication.getId(), publication.getInstanceId(), msg.content());
  }

  public static void showWebPageContent(String pubId, String appId, String title) {
    PublicationContentHelper.showContent(pubId, appId, title);
  }

  private static void showPublicationContent(String pubId, String appId, String title) {
    PublicationContentHelper.showContent(pubId, appId, title);
  }

  @Override
  public void setVisible(final boolean visible) {
    super.setVisible(visible);
    Element iframeC = content.getElementsByTagName("iframe").getItem(0);
    if (iframeC != null) {
      // need to re display
      iframeC.removeFromParent();
      PublicationContentHelper.showContent(publication.getId(), publication.getInstanceId(), content);
    }
  }
}
