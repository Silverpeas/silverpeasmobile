package com.silverpeas.mobile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.common.*;
import com.silverpeas.mobile.client.common.event.ExceptionEvent;
import com.silverpeas.mobile.client.common.gwt.SuperDevModeUtil;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.network.NetworkHelper;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.components.base.Page;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import com.silverpeas.mobile.client.pages.main.AppList;
import com.silverpeas.mobile.client.pages.search.SearchResultPage;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.FullUserDTO;
import com.silverpeas.mobile.shared.dto.search.ResultDTO;

import java.util.List;

public class SpMobil implements EntryPoint {

    public final static ConfigurationProvider configuration = GWT.create(ConfigurationProvider.class);
    public final static Page mainPage = new Page();
    public static DetailUserDTO user;
    private static String viewport, bodyClass, bodyId;
    private static ApplicationMessages msg;
    private static String shortcutAppId;
    private static String shortcutContentType;
    private static String shortcutContentId;
    private static SpMobil instance = null;

    /**
     * Init. spmobile.
     */
    public void onModuleLoad() {
        SuperDevModeUtil.showDevMode();
        instance = this;

        shortcutAppId = Window.Location.getParameter("shortcutAppId");
        shortcutContentType = Window.Location.getParameter("shortcutContentType");
        shortcutContentId = Window.Location.getParameter("shortcutContentId");
        msg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(ExceptionEvent.TYPE, new ErrorManager());
        loadIds(null, false);

        NodeList<Element> tags = Document.get().getElementsByTagName("meta");
        for (int i = 0; i < tags.getLength(); i++) {
            MetaElement metaTag = ((MetaElement) tags.getItem(i));
            if (metaTag.getName().equals("viewport")) {
                viewport = metaTag.getContent();
            }
        }
        Notification.activityStop();
    }

    public static SpMobil getInstance() {
        return instance;
    }

    /**
     * Auto login.
     * @param login
     * @param password
     * @param domainId
     * @param auto
     */
    private void login(final String login, final String password, final String domainId, final boolean auto, final Command attempt) {
        AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<DetailUserDTO>(null) {
            @Override
            public void attempt() {
                ServicesLocator.getServiceConnection().login(login, password, domainId,this);
            }

            @Override
            public void onFailure(Throwable reason) {
                if (OfflineHelper.needToGoOffine(reason)) {
                    FullUserDTO user = AuthentificationManager.getInstance().loadUser();
                    if (user != null) {
                        if (!auto) displayMainPage(user);
                        // dont't do attempt.execute() because connexion is lost again
                    } else {
                        displayLoginPage();
                    }
                } else {
                    displayLoginPage();
                }
            }
            @Override
            public void onSuccess(DetailUserDTO user) {
                super.onSuccess(user);
                if (!auto) displayMainPage(user);
                if (attempt != null) attempt.execute();
            }
        };
        action.attempt();
    }

    public static void displayMainPage(final DetailUserDTO user) {
        if (!Window.Location.getHref().contains("?locale=") && !user.getLanguage().equalsIgnoreCase("fr")) {
            Window.Location.replace(Window.Location.getHref() + "?locale=" + user.getLanguage());
        }
        SpMobil.user = user;
        mainPage.setUser(user);
        RootPanel.get().clear();
        RootPanel.get().add(mainPage);
        PageHistory.getInstance().goTo(new AppList());

        if (shortcutAppId != null && shortcutContentType != null && shortcutContentId != null) {
            ShortCutRouter.route(user, shortcutAppId, shortcutContentType, shortcutContentId);
        }
    }

    /**
     * Load ids in SQL Web Storage.
     */
    public void loadIds(Command attempt, boolean auto) {
        FullUserDTO user = AuthentificationManager.getInstance().loadUser();
        if (user != null) {
            String password = AuthentificationManager.getInstance().decryptPassword(user.getPassword());
            if (password != null) {
                login(user.getLogin(), password, user.getDomainId(), auto, attempt);
            }
        } else {
            displayLoginPage();
        }
    }

    private void displayLoginPage() {
        AuthentificationManager.getInstance().clearUserStorage();
        ConnexionPage connexionPage = new ConnexionPage();
        RootPanel.get().clear();
        RootPanel.get().add(connexionPage);
    }

    public static void search(final String query) {
        Notification.activityStart();
        AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<List<ResultDTO>>() {
            @Override
            public void attempt() {
                ServicesLocator.getServiceSearch().search(query, this);
            }

            @Override
            public void onSuccess(final List<ResultDTO> results) {
                super.onSuccess(results);
                mainPage.resetSearchField();
                mainPage.closeMenu();
                SearchResultPage page = new SearchResultPage();
                page.setPageTitle(msg.results());
                page.setResults(results);
                page.show();
                Notification.activityStop();
            }
        };
        action.attempt();
    }

    public static void showFullScreen(final Widget content, final boolean zoomable, String bodyClass,
                                      String bodyId) {
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
