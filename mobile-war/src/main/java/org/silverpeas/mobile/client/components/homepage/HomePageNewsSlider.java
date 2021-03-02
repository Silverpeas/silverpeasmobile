package org.silverpeas.mobile.client.components.homepage;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;

/**
 * @author svu
 */
public class HomePageNewsSlider {

  private static HomePageNewsSlider instance = null;
  private static boolean stop = false;
  private static HomePageContent currentHomePageContent = null;

  public static HomePageNewsSlider getInstance() {
    if (instance == null) {
      instance = new HomePageNewsSlider();
      createScheduler();
    }
    return instance;
  }

  public void setCurrentHomePageContent(HomePageContent current) {
    HomePageNewsSlider.currentHomePageContent = current;
    HomePageNewsSlider.stop = false;
  }

  public void stopAutoSlider() {
    HomePageNewsSlider.stop = true;
  }

  private static void createScheduler() {
    Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
      @Override
      public boolean execute() {
        if (!stop) {
          currentHomePageContent.slideToRight();
        }
        return true;
      }
    }, 5000);
  }
}
