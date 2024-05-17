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

package org.silverpeas.mobile.client.apps.comments;

import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.comments.events.app.*;
import org.silverpeas.mobile.client.apps.comments.events.pages.CommentAddedEvent;
import org.silverpeas.mobile.client.apps.comments.events.pages.CommentDeletedEvent;
import org.silverpeas.mobile.client.apps.comments.events.pages.CommentsLoadedEvent;
import org.silverpeas.mobile.client.apps.comments.pages.CommentsPage;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;

import java.util.List;

/**
 * @author: svu
 */
public class CommentsApp extends App implements CommentsAppEventHandler {

    private CommentsPage mainPage = new CommentsPage();
    private String instanceId;

    public CommentsApp(String contentId, String instanceId, String contentType, String pageTitle, String title) {
        super();
        this.instanceId = instanceId;
        EventBus.getInstance().addHandler(AbstractCommentsAppEvent.TYPE, this);
        mainPage.setTitle(title);
        mainPage.setPageTitle(pageTitle);
        mainPage.setContentInfos(contentId, instanceId, contentType);
    }

    public void start() {
        setMainPage(mainPage);
        super.start();
    }

    @Override
    public void stop() {
        EventBus.getInstance().removeHandler(AbstractCommentsAppEvent.TYPE, this);
        super.stop();
    }

    @Override
    public void loadComments(final CommentsLoadEvent event) {
        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<CommentDTO>>() {
            @Override
            public void attempt() {
                super.attempt();
                ServicesLocator.getRestServiceComment().getAllComments(instanceId,
                        event.getContentType(), event.getContentId(), this);
            }

            @Override
            public void onSuccess(final Method method, final List<CommentDTO> result) {
                super.onSuccess(method, result);
                EventBus.getInstance().fireEvent(new CommentsLoadedEvent(result));
            }
        };
        action.attempt();
    }

    @Override
    public void addComment(final AddCommentEvent event) {
        CommentDTO dto = new CommentDTO();
        dto.setText(event.getMessage());
        dto.setAuthor(SpMobil.getUserProfile());
        dto.setComponentId(event.getInstanceId());
        dto.setResourceId(event.getContentId());
        dto.setResourceType(event.getContentType());

        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<CommentDTO>() {
            @Override
            public void attempt() {
                super.attempt();
                ServicesLocator.getRestServiceComment().saveNewComment(event.getInstanceId(), event.getContentType(), event.getContentId(), dto, this);
            }

            @Override
            public void onSuccess(final Method method, final CommentDTO result) {
                super.onSuccess(method, result);
                EventBus.getInstance().fireEvent(new CommentAddedEvent(result));
            }
        };
        action.attempt();
    }

    @Override
    public void deleteComment(DeleteCommentEvent event) {
        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {

            @Override
            public void attempt() {
                super.attempt();
                ServicesLocator.getRestServiceComment().deleteComment(event.getComment().getComponentId(),
                        event.getComment().getResourceType(), event.getComment().getResourceId(), event.getComment().getId(), this);
            }

            @Override
            public void onSuccess(Method method, Void unused) {
                super.onSuccess(method, unused);
                EventBus.getInstance().fireEvent(new CommentDeletedEvent(event.getComment()));
            }
        };
        action.attempt();


    }

    @Override
    public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {

    }
}
