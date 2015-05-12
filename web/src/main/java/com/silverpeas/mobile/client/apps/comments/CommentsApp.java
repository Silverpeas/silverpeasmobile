package com.silverpeas.mobile.client.apps.comments;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.comments.events.app.AbstractCommentsAppEvent;
import com.silverpeas.mobile.client.apps.comments.events.app.AddCommentEvent;
import com.silverpeas.mobile.client.apps.comments.events.app.CommentsAppEventHandler;
import com.silverpeas.mobile.client.apps.comments.events.app.CommentsLoadEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentAddedEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentsLoadedEvent;
import com.silverpeas.mobile.client.apps.comments.pages.CommentsPage;
import com.silverpeas.mobile.client.apps.contacts.events.app.ContactsLoadEvent;
import com.silverpeas.mobile.client.apps.contacts.events.pages.ContactsLoadedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: svu
 */
public class CommentsApp extends App implements CommentsAppEventHandler {

    private CommentsPage mainPage = new CommentsPage();

    public CommentsApp(String contentId, String instanceId, String contentType, String pageTitle, String title) {
        super();
        EventBus.getInstance().addHandler(AbstractCommentsAppEvent.TYPE, this);
        mainPage.setTitle(title);
        mainPage.setPageTitle(pageTitle);
        mainPage.setContentInfos(contentId, instanceId, contentType);
    }

    public void start(){
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
        final String key = "comments" + event.getContentType() + "_" + event.getContentId();
        AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<CommentDTO>>(getOfflineAction(event, key)) {
            @Override
            public void onSuccess(List<CommentDTO> result) {
                super.onSuccess(result);
                LocalStorageHelper.store(key, List.class, result);
                EventBus.getInstance().fireEvent(new CommentsLoadedEvent(result));
            }

            @Override
            public void attempt() {
                ServicesLocator.getServiceComments().getComments(event.getContentId(), event.getContentType(), this);
            }
        };
        action.attempt();
    }

    private Command getOfflineAction(final CommentsLoadEvent event, final String key) {
        Command offlineAction = new Command() {

            @Override
            public void execute() {
                List<CommentDTO> result = LocalStorageHelper.load(key, List.class);
                if (result == null) {
                    result = new ArrayList<CommentDTO>();
                }
                EventBus.getInstance().fireEvent(new CommentsLoadedEvent(result));
            }
        };

        return offlineAction;
    }

    @Override
    public void addComment(final AddCommentEvent event) {
        AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<CommentDTO>() {
            @Override
            public void attempt() {
                ServicesLocator.getServiceComments().addComment(event.getContentId(), event.getInstanceId(), event.getContentType(), event.getMessage(), this);
            }

            @Override
            public void onSuccess(final CommentDTO result) {
                super.onSuccess(result);
                EventBus.getInstance().fireEvent(new CommentAddedEvent(result));
            }
        };
        action.attempt();
    }
}
