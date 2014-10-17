package com.silverpeas.mobile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.silverpeas.mobile.client.common.ErrorManager;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.event.ExceptionEvent;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.components.base.Page;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import com.silverpeas.mobile.client.pages.main.AppList;
import com.silverpeas.mobile.client.pages.search.SearchResultPage;
import com.silverpeas.mobile.client.persist.User;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.search.ResultDTO;

import java.util.List;

public class SpMobil implements EntryPoint {

  public final static ConfigurationProvider configuration = GWT.create(ConfigurationProvider.class);
  public final static Page mainPage = new Page();
  public static DetailUserDTO user;
  private static String viewport, bodyClass, bodyId;
  private static ApplicationMessages msg;

  /**
   * Init. spmobile.
   */
  public void onModuleLoad() {
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(ExceptionEvent.TYPE, new ErrorManager());
    loadIds();

    NodeList<Element> tags = Document.get().getElementsByTagName("meta");
    for (int i = 0; i < tags.getLength(); i++) {
      MetaElement metaTag = ((MetaElement) tags.getItem(i));
      if (metaTag.getName().equals("viewport")) {
        viewport = metaTag.getContent();
      }
    }
  }

  /**
   * Auto login.
   * @param login
   * @param password
   * @param domainId
   * @param auto
   */
  private void login(String login, String password, String domainId, final boolean auto) {
    ServicesLocator.serviceConnection.login(login, password, domainId, new AsyncCallback<DetailUserDTO>() {
      public void onFailure(Throwable reason) {
        clearIds();
        ConnexionPage connexionPage = new ConnexionPage();
        RootPanel.get().clear();
        RootPanel.get().add(connexionPage);
      }
      public void onSuccess(DetailUserDTO user) {
        SpMobil.user = user;
        mainPage.setUser(user);
        RootPanel.get().clear();
        RootPanel.get().add(mainPage);
        PageHistory.getInstance().goTo(new AppList());
      }
    });
  }

  /**
   * Clean ids in SQL Web Storage.
   */
  public static void clearIds() {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      storage.clear();
    }
  }

  /**
   * Load ids in SQL Web Storage.
   */
  private void loadIds() {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      String dataItem = storage.getItem("userConnected");
      if (dataItem != null) {
        User user = User.getInstance(dataItem);
        String password = decryptPassword(user.getPassword());
        if (password != null) {
          login(user.getLogin(), password, user.getDomainId(), true);
        }
      } else {
        ConnexionPage connexionPage = new ConnexionPage();
        RootPanel.get().clear();
        RootPanel.get().add(connexionPage);
      }
    }
  }

  /**
   * Decrypt password in SQL Web Storage.
   * @param passwordEncrysted
   * @return
   */
  private String decryptPassword(String passwordEncrysted) {
    TripleDesCipher cipher = new TripleDesCipher();
    cipher.setKey(configuration.getDESKey().getBytes());
    String plainPassword = null;
    try {
      plainPassword = cipher.decrypt(passwordEncrysted);
    } catch (Exception e) {
      EventBus.getInstance().fireEvent(new ErrorEvent(e));
    }
    return plainPassword;
  }

  public static void search(String query) {
    ServicesLocator.serviceSearch.search(query, new AsyncCallback<List<ResultDTO>>() {
      @Override
      public void onFailure(final Throwable throwable) {
        EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
      }

      @Override
      public void onSuccess(final List<ResultDTO> results) {
        mainPage.resetSearchField();
        mainPage.closeMenu();
        SearchResultPage page = new SearchResultPage();
        page.setPageTitle(msg.results());
        page.setResults(results);
        page.show();
      }
    });
  }

  public static void showFullScreen(final Widget content, final boolean zoomable, String bodyClass, String bodyId) {
    PageHistory.getInstance().gotoToFullScreen("viewer");
    RootPanel.get().clear();
    RootPanel.get().add(content);

    if (zoomable) {
      NodeList<Element> tags = Document.get().getElementsByTagName("meta");
      for (int i = 0; i < tags.getLength(); i++) {
        MetaElement metaTag = ((MetaElement) tags.getItem(i));
        if (metaTag.getName().equals("viewport")) {
          metaTag.setContent("");
        }
      }
    }
    SpMobil.bodyClass = Document.get().getBody().getClassName();
    SpMobil.bodyId = Document.get().getBody().getId();
    Document.get().getBody().setClassName(bodyClass);
    Document.get().getBody().setId(bodyId);
    Document.get().getBody().getStyle().setPaddingTop(0, Style.Unit.PX);
  }

  public static void restoreMainPage() {
    RootPanel.get().clear();
    RootPanel.get().add(SpMobil.mainPage);

    Document.get().getBody().setId(bodyId);
    Document.get().getBody().setClassName(bodyClass);
    Document.get().getBody().getStyle().clearPaddingTop();

    NodeList<Element> tags = Document.get().getElementsByTagName("meta");
    for (int i = 0; i < tags.getLength(); i++) {
      MetaElement metaTag = ((MetaElement) tags.getItem(i));
      if (metaTag.getName().equals("viewport")) {
        metaTag.setContent(viewport);
      }
    }

  }
}
