package com.silverpeas.mobile.client.apps.comments;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.comments.events.app.AbstractCommentsAppEvent;
import com.silverpeas.mobile.client.apps.comments.events.app.CommentsAppEventHandler;
import com.silverpeas.mobile.client.apps.comments.events.app.CommentsLoadEvent;
import com.silverpeas.mobile.client.apps.comments.events.pages.CommentsLoadedEvent;
import com.silverpeas.mobile.client.apps.comments.pages.CommentsPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.documents.CommentDTO;

import java.util.List;

/**
 * @author: svu
 */
public class CommentsApp extends App implements CommentsAppEventHandler {

  private CommentsPage mainPage = new CommentsPage();

  public CommentsApp(String contentId, String title) {
    super();
    EventBus.getInstance().addHandler(AbstractCommentsAppEvent.TYPE, this);
    mainPage.setTitle(title);
    mainPage.setContentId(contentId);
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
    ServicesLocator.serviceDocuments.getComments(event.getContentId(), new AsyncCallback<List<CommentDTO>>() {
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
}
