/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.client.common.storage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.seanchenxi.gwt.storage.client.serializer.StorageSerializer;


/**
 * @author: svu
 */
public class LocalStorageHelper {

  private static StorageSerializer serializer = GWT.create(StorageSerializer.class);

  public static <T> void store(String key, Class<? super T> cBean , T bean) {
    Storage storage = Storage.getLocalStorageIfSupported();
    Class<T> c;
    if (storage != null) {
      String data = null;
      try {
        data = serializer.serialize(cBean , bean);
        storage.setItem(key, data);
      } catch (Throwable t) {GWT.log("error store", t);}

    }
  }

  public static <T> T load(String key, Class<T> beanClass) {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      String dataItem = storage.getItem(key);
      if (dataItem != null) {
        try {
          return serializer.deserialize(beanClass, dataItem);
        } catch (Throwable t) {
        }
      }
    }
    return  null;
  }

  public static void clear() {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) storage.clear();
  }

  public static void remove(String key) {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      storage.removeItem(key);
    }
  }
}
