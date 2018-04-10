/*
 * Copyright (C) 2000 - 2018 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.common.navigation;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.components.base.PageContent;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

public class PageHistory implements ValueChangeHandler<String> {

  private static PageHistory instance = null;
  private boolean noHistoryEvent = false;

  private Stack<PageContent> pages = new Stack<PageContent>();
  private String firstToken = "";

  public static PageHistory getInstance() {
    if (instance == null) {
      instance = new PageHistory();
      History.addValueChangeHandler(instance);
    }
    return instance;
  }

  public void gotoToFullScreen(String token) {
    hideCallback();
    pages.push(null);
    browserGoto(""+token);
  }

  public void goTo(PageContent page) {
    hideCallback();
    if (pages.isEmpty()) firstToken = "" + page.hashCode();
    pages.push(page);
    SpMobil.getMainPage().setContent(page);
    browserGoto(""+page.hashCode());
    //TODO : TODO : css3 transition
  }

  private void hideCallback() {
    PageContent currentPage = null;
    try { currentPage = pages.peek(); } catch (EmptyStackException e) {}
    if (currentPage != null) currentPage.hide();
  }

  public PageContent back() {
    hideCallback();
    PageContent page = pages.pop();
    page.stop();
    page = pages.peek();
    SpMobil.getMainPage().setContent(page);
    browserBack();
    //TODO : css3 transition

    return page;
  }

  public PageContent getCurrent() {
    return pages.peek();
  }

  public int size() {
    return pages.size();
  }

  public void goBackToFirst() {
    hideCallback();
    while(!pages.isEmpty()) {
      PageContent currentPage = pages.pop();
      if (pages.isEmpty()) {
        pages.push(currentPage);
        SpMobil.getMainPage().setContent(currentPage);
        browserGoto(""+currentPage.hashCode());
        break;
      } else {
        currentPage.stop();
      }
    }
  }

  public void clear() {
    while(!pages.isEmpty()) {
      PageContent currentPage = pages.pop();
      currentPage.stop();
    }
  }

  public boolean isVisible(PageContent page) {
    PageContent currentPage = pages.peek();
    return (currentPage == page);
  }

  private void back(String token) {
    boolean back = false;
    if (token.isEmpty()) {
      // prevent exit
      browserGoto(firstToken);
    } else {
      back = isBackAction(token);
      if (back) {
        PageContent page = pages.pop();
        if (page != null) {
          page.stop();
          page = pages.peek();
          SpMobil.getMainPage().setContent(page);
        } else {
          SpMobil.restoreMainPage();
        }
      }
    }
  }

  private boolean isBackAction(String token) {
    if (!pages.isEmpty()) {
      if (pages.peek()==null) return true;
      if (String.valueOf(pages.peek().hashCode()).equals(token)) {
        return false;
      } else {
        Iterator<PageContent> iPages = pages.iterator();
        while (iPages.hasNext()) {
          PageContent page = (PageContent) iPages.next();
          if (String.valueOf(page.hashCode()).equals(token)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private void browserBack() {
    noHistoryEvent = true;
    History.back();
  }

  private void browserGoto(String token) {
    noHistoryEvent = true;
    History.newItem(token);
  }

  @Override
  public void onValueChange(ValueChangeEvent<String> event) {
    if (!noHistoryEvent) {
      back(event.getValue());
    }
    noHistoryEvent = false;
  }
}
