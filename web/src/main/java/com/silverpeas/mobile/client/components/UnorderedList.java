package com.silverpeas.mobile.client.components;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.Window;
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
    count++;
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

  @Override
  public void setStyleName(String style) {
    list.setClassName(style);
  }

  public void setStyledisplay(Style.Display display) {
    list.getStyle().setDisplay(display);
  }

  public void setId(String id) {
    list.setId(id);
  }
}