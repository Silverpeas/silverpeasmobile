package com.silverpeas.mobile.client.apps.comments;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.comments.events.app.AbstractCommentsAppEvent;
import com.silverpeas.mobile.client.apps.comments.events.app.AddCommentEvent;
import com.silverpeas.mobile.client.apps.comments.events.app.CommentsAppEventHandler;
import com.silverpeas.mobile.client.apps.comments.events.app.CommentsLoadEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentAddedEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentsLoadedEvent;
import com.silverpeas.mobile.client.apps.comments.pages.CommentsPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;

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
    ServicesLocator.serviceComments.getComments(event.getContentId(), event.getContentType(), new AsyncCallback<List<CommentDTO>>() {
      @Override
      public void onSuccess(List<CommentDTO> result) {
        EventBus.getInstance().fireEvent(new CommentsLoadedEvent(result));
      }
      @Override
      public void onFailure(Throwable caught) {
        EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
      }
    });
  }

  @Override
  public void addComment(final AddCommentEvent event) {
    ServicesLocator.serviceComments.addComment(event.getContentId(), event.getInstanceId(), event.getContentType(), event.getMessage(), new AsyncCallback<CommentDTO>() {
      @Override
      public void onFailure(final Throwable caught) {
        EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
      }
      @Override
      public void onSuccess(final CommentDTO result) {
        EventBus.getInstance().fireEvent(new CommentAddedEvent(result));
      }
    });
  }
}
