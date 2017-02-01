package com.silverpeas.mobile.client.apps.news.pages;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.apps.news.events.app.NewsLoadEvent;
import com.silverpeas.mobile.client.apps.news.events.pages.AbstractNewsPagesEvent;
import com.silverpeas.mobile.client.apps.news.events.pages.NewsLoadedEvent;
import com.silverpeas.mobile.client.apps.news.events.pages.NewsPagesEventHandler;
import com.silverpeas.mobile.client.apps.news.pages.widgets.NewsItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: svu
 */
public class NewsPage extends Composite implements NewsPagesEventHandler, SwipeEndHandler {

  private List<NewsItem> items = new ArrayList<NewsItem>();
  private HTMLPanel container;
  private HTML content = new HTML();
  private SwipeRecognizer swipeRecognizer;

  public NewsPage() {
    EventBus.getInstance().addHandler(AbstractNewsPagesEvent.TYPE, this);
    container = new HTMLPanel("");
    container.getElement().setId("lastNews");
    initWidget(container);

    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        swipeRecognizer = new SwipeRecognizer(content);
      }
    });
    EventBus.getInstance().addHandler(SwipeEndEvent.getType(), this);
    EventBus.getInstance().fireEvent(new NewsLoadEvent());

  }

  @Override
  public void onSwipeEnd(final SwipeEndEvent event) {
      if (isVisible()) {
        if (event.getDirection() == SwipeEvent.DIRECTION.RIGHT_TO_LEFT) {
          // suivant
          for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isVisible()) {
              items.get(i).setVisible(false);
              if ((i + 1) >= items.size()) {
                items.get(0).setVisible(true);
              } else {
                items.get(i + 1).setVisible(true);
              }
              break;
            }
          }
        } else if (event.getDirection() == SwipeEvent.DIRECTION.LEFT_TO_RIGHT) {
          // precedent
          for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isVisible()) {
              items.get(i).setVisible(false);
              if ((i - 1) < 0) {
                items.get(items.size() - 1).setVisible(true);
              } else {
                items.get(i - 1).setVisible(true);
              }
              break;
            }
          }
        }
      }
  }

  public void stop() {
    EventBus.getInstance().removeHandler(AbstractNewsPagesEvent.TYPE, this);
    EventBus.getInstance().removeHandler(SwipeEndEvent.getType(), this);
  }

  @Override
  public void onNewsLoad(final NewsLoadedEvent event) {
    Notification.activityStop();
    content.setHTML(event.getNews());
    container.add(content);

    NodeList<Element> news = getElement().getElementsByTagName("li");
    for (int i = 0; i < news.getLength() ; i++) {
      Element n = news.getItem(i);
      NewsItem item = NewsItem.wrap(container, n);
      item.setVisible(i==0);
      items.add(item);
    }
  }
}