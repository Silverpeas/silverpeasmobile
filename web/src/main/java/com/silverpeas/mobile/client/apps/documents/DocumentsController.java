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

package com.silverpeas.mobile.client.apps.documents;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.silverpeas.mobile.client.apps.documents.events.controller.AbstractDocumentsControllerEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsControllerEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadPublicationsEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadTopicsEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsSaveSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.DocumentsLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.NewInstanceLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.PublicationsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.TopicsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.persistances.DocumentsSettings;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
import com.silverpeas.mobile.client.common.Database;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public class DocumentsController implements Controller, DocumentsControllerEventHandler,
    NavigationEventHandler {

  public DocumentsController() {
    super();
    EventBus.getInstance().addHandler(AbstractDocumentsControllerEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractDocumentsControllerEvent.TYPE, this);
    EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
  }

  @Override
  public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
    EventBus.getInstance().fireEvent(new NewInstanceLoadedEvent(event.getInstance()));
  }

  @Override
  public void loadSettings(DocumentsLoadSettingsEvent event) {
    Database.open();
    final Entity<DocumentsSettings> settingsEntity = GWT.create(DocumentsSettings.class);
    final Collection<DocumentsSettings> settings = settingsEntity.all().limit(1);
    settings.one(new ScalarCallback<DocumentsSettings>() {
      public void onSuccess(final DocumentsSettings settings) {
        ApplicationInstanceDTO instance = new ApplicationInstanceDTO();
        instance.setId(settings.getSelectedInstanceId());
        instance.setLabel(settings.getSelectedInstanceLabel());
        instance.setType(Apps.kmelia.name());

        TopicDTO topic = new TopicDTO();
        topic.setId(settings.getSelectedTopicId());
        topic.setName(settings.getSelectedTopicLabel());

        EventBus.getInstance().fireEvent(new DocumentsLoadedSettingsEvent(instance, topic));
      }
    });
  }

  /**
   * Store settings.
   */
  @Override
  public void saveSettings(final DocumentsSaveSettingsEvent event) {
    Database.open();
    final Entity<DocumentsSettings> settingsEntity = GWT.create(DocumentsSettings.class);
    final Collection<DocumentsSettings> settings = settingsEntity.all();
    Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {
      @Override
      public void onSuccess() {
        settings.destroyAll(new com.gwtmobile.persistence.client.Callback() {
          public void onSuccess() {
            Persistence.flush();
            final Entity<DocumentsSettings> settingsEntity = GWT.create(DocumentsSettings.class);
            Persistence.schemaSync(new com.gwtmobile.persistence.client.Callback() {
              public void onSuccess() {
                final DocumentsSettings settings = settingsEntity.newInstance();
                settings.setSelectedInstanceId(event.getInstance().getId());
                settings.setSelectedInstanceLabel(event.getInstance().getLabel());
                if (event.getTopic() != null) {
                  settings.setSelectedTopicId(event.getTopic().getId());
                  settings.setSelectedTopicLabel(event.getTopic().getName());
                }
                Persistence.flush();
              }
            });
          }
        });
      }
    });
  }

  /**
   * Get subtopics.
   */
  @Override
  public void loadTopics(DocumentsLoadTopicsEvent event) {
    ServicesLocator.serviceDocuments.getTopics(event.getInstanceId(), event.getRootTopicId(),
        new AsyncCallback<List<TopicDTO>>() {
          @Override
          public void onSuccess(List<TopicDTO> result) {
            EventBus.getInstance().fireEvent(new TopicsLoadedEvent(result));
          }

          @Override
          public void onFailure(Throwable caught) {
            EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
          }
                });
  }

  /**
   * Get publications.
   */
  @Override
  public void loadPublications(DocumentsLoadPublicationsEvent event) {
    ServicesLocator.serviceDocuments.getPublications(event.getInstanceId(), event.getTopicId(),
        new AsyncCallback<List<PublicationDTO>>() {
          @Override
          public void onSuccess(List<PublicationDTO> result) {
            EventBus.getInstance().fireEvent(new PublicationsLoadedEvent(result));
          }

          @Override
          public void onFailure(Throwable caught) {
            EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
          }
                });

  }

  /**
   * Get publication infos.
   */
  @Override
  public void loadPublication(DocumentsLoadPublicationEvent event) {
    ServicesLocator.serviceDocuments.getPublication(event.getPubId(),
        new AsyncCallback<PublicationDTO>() {
          @Override
          public void onSuccess(PublicationDTO result) {
            EventBus.getInstance().fireEvent(new PublicationLoadedEvent(result));
          }

          @Override
          public void onFailure(Throwable caught) {
            EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
          }
                });
  }
}
