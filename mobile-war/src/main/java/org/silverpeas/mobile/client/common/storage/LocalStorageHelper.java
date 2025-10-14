/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.client.common.storage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;


/**
 * @author: svu
 */
public class LocalStorageHelper {

  public static AutoBeanFactory factory = GWT.create(AutoBeanFactory.class);
  public static LocalStorageHelper instance;

  public static LocalStorageHelper getInstance() {
    if (instance == null) {
      instance = new LocalStorageHelper();
    }
    return instance;
  }

  public <T> void store(String key, AutoBean<T> autobean) {
    Storage storage = Storage.getLocalStorageIfSupported();

    if (storage != null) {
      String data = null;
      try {
        data = AutoBeanCodex.encode(autobean).getPayload();
        storage.setItem(key, data);
      } catch (Throwable t) {GWT.log("error store", t);}

    }
  }

  public void store(String key, String data) {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      try {
        storage.setItem(key, data);
      } catch (Throwable t) {GWT.log("error store", t);}

    }
  }
  public String load(String key) {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      String dataItem = storage.getItem(key);
      return dataItem;
    }
    return  null;
  }
  public <T> AutoBean<T> load(String key, Class<T> beanClass) {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      String dataItem = storage.getItem(key);
      if (dataItem != null) {
        try {

          AutoBean<T> bean = AutoBeanCodex.decode(factory, beanClass, dataItem);
          return bean;

        } catch (Throwable t) {
        }
      }
    }
    return  null;
  }

  public void clear() {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) storage.clear();
  }

  public void remove(String key) {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      storage.removeItem(key);
    }
  }

  public void storeBuildDate() {
    String  buildDate = ResourcesManager.getParam("build.date");
    store("build.date", buildDate);
  }
}
