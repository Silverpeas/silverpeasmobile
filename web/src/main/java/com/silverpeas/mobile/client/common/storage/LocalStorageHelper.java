package com.silverpeas.mobile.client.common.storage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.rpc.SerializationException;
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
      } catch (SerializationException e) {}
      storage.setItem(key, data);
    }
  }

  public static <T> T load(String key, Class<T> beanClass) {
    Storage storage = Storage.getLocalStorageIfSupported();
    if (storage != null) {
      String dataItem = storage.getItem(key);
      if (dataItem != null) {
        try {
          return serializer.deserialize(beanClass, dataItem);
        } catch (SerializationException e) {
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
