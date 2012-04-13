/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.server.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

@SuppressWarnings("serial")
class AutoRefreshingProperties extends Properties {
  private long LAST_LOADED = System.currentTimeMillis();
  private List<FileChangeListener> listeners =
      Collections.synchronizedList(new ArrayList<FileChangeListener>());

  public AutoRefreshingProperties(String absolutePathToPriopertiesFile) throws IOException {
    this(null, absolutePathToPriopertiesFile);
  }

  public AutoRefreshingProperties(Properties defaults, String absolutePathToPriopertiesFile)
      throws IOException {
    super(defaults);
    load(absolutePathToPriopertiesFile);
  }

  public AutoRefreshingProperties removeListener(FileChangeListener listener) {
    listeners.remove(listener);
    return this;
  }

  public AutoRefreshingProperties addListener(FileChangeListener listener) {
    listeners.add(listener);
    return this;
  }

  public synchronized void load(String absoluteFilePath) throws IOException {
    InputStream is = new FileInputStream(new File(absoluteFilePath));
    this.load(is);
    LAST_LOADED = System.currentTimeMillis();
    keepUpdated(absoluteFilePath);
  }

  abstract class FileWatcher implements Runnable {
  }

  private void keepUpdated(final String absoluteFilePath) {
    Executors.newSingleThreadExecutor().submit(
        new FileWatcher() {
        public void run() {
        try {
          while (true) {
            if (new File(absoluteFilePath).lastModified() > LAST_LOADED) {
              InputStream is = new FileInputStream(new File(absoluteFilePath));
              load(is);
              LAST_LOADED = System.currentTimeMillis();
              notifyListeners();
            }
            Thread.yield();
            Thread.sleep(2000);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        }
            }
        );
  }

  private void notifyListeners() {
    final Properties properties = new Properties(this);
    for (FileChangeListener aListener : listeners) {
      aListener.notifyFileChanged(properties);
    }
  }
}
