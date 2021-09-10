/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.components;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class UnorderedList extends ComplexPanel {

  private UListElement list;
  private int count = 0;

  public UnorderedList() {
    super();
    list = Document.get().createULElement();
    setElement(list);
  }

  @Override
  public void add(Widget child) {
    add(child, "");
  }

  public void add(Widget child, String styleName) {
    if (!child.getElement().getTagName().equalsIgnoreCase("li")) {
      Element li = Document.get().createLIElement().cast();
      if (!styleName.isEmpty()) {
        li.setAttribute("class", styleName);
      }
      list.appendChild(li);
      super.add(child, li);
    } else {
      super.add(child, list);
    }
    count++;
  }

  @Override
  public void clear() {
    super.clear();
    list.removeAllChildren();
    count = 0;
  }

  public boolean isEmpty() {
    return (count == 0);
  }

  public int getCount() {
    return count;
  }

  @Override
  public void setStyleName(String style) {
    list.setClassName(style);
  }

  public void setStyledisplay(Style.Display display) {
    list.getStyle().setDisplay(display);
  }

  public String getStyleDisplay() {
    return list.getStyle().getDisplay();
  }

  public void setId(String id) {
    list.setId(id);
  }
}