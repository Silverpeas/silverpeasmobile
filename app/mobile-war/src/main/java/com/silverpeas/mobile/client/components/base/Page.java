package com.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.components.base.events.EndPageEvent;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;

public class Page extends Composite implements Window.ScrollHandler {

  private static PageUiBinder uiBinder = GWT.create(PageUiBinder.class);

  interface PageUiBinder extends UiBinder<Widget, Page> {
  }

  @UiField protected SimplePanel contentPlace;
  @UiField protected PageHeader header;
  @UiField protected NavigationMenu menu;
  @UiField protected DivElement contentContainer;
  @UiField protected PageFooter footer;

  protected PageContent content;

  public Page() {
    initWidget(uiBinder.createAndBindUi(this));
    Window.addWindowScrollHandler(this);
  }

  public void setContent(PageContent content) {
    this.content = content;
    contentPlace.setWidget(content);
    header.setPageTitle(content.getPageTitle());
    header.setVisibleBackButton(PageHistory.getInstance().size() > 1);
  }

  public void toogleMenu() {
    menu.toogleMenu();
  }

  public void closeMenu() {
    menu.closeMenu();
  }

  public void setUser(DetailUserDTO user) {
    menu.setUser(user);
  }

  public void resetSearchField() {
    menu.resetSearchField();
  }

  @Override
  public void onWindowScroll(final Window.ScrollEvent event) {
    if (Document.get().getScrollTop() + Document.get().getClientHeight() >= Document.get().getScrollHeight()) {
      EventBus.getInstance().fireEvent(new EndPageEvent());
   }
  }

  public int getHeaderHeight() {
    return header.getHeight();
  }
  public int getFooterHeight() { return footer.getOffsetHeight() ; }

  public void showOfflineIndicator() {
    header.showOfflineIndicator();
  }

  public void hideOfflineIndicator() {
    header.hideOfflineIndicator();
  }

}
