package com.silverpeas.mobile.client.components;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class UnorderedList extends ComplexPanel {

	private UListElement list;
	
	public UnorderedList() {
		super();
		 list = Document.get().createULElement(); 
	     setElement(list); 
	}
	
	@Override 
    public void add(Widget child) { 
        Element li = Document.get().createLIElement().cast(); 
        list.appendChild(li); 
        super.add(child, li); 
    }

	@Override
	public void clear() {		
		super.clear();
		list.removeAllChildren();
	}

	@Override
	public void setStyleName(String style) {
		list.setClassName(style);
	}	
}