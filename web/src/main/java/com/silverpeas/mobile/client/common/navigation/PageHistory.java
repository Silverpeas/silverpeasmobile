package com.silverpeas.mobile.client.common.navigation;

import java.util.Iterator;
import java.util.Stack;

import com.google.gwt.user.client.History;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.components.base.PageContent;

public class PageHistory {
	
	private static PageHistory instance = null;
	
	private Stack<PageContent> pages = new Stack<PageContent>();
	private String firstToken = "";
	
	public static PageHistory getInstance() {
		if (instance == null) {
			instance = new PageHistory();
		}
		return instance;
	}
	
	public void goTo(PageContent page) {		
		if (pages.isEmpty()) firstToken = "" + page.hashCode();
		pages.push(page);
		SpMobil.mainPage.setContent(page);
		History.newItem(""+page.hashCode());
		//TODO : TODO : css3 transition
	}
	
	public PageContent back() {		
		PageContent page = pages.pop();
		page.stop();		
		page = pages.peek();
		SpMobil.mainPage.setContent(page);
		History.back();
		//TODO : css3 transition
		
		return page;
	}
	
	public PageContent getCurrent() {
		return pages.peek();
	}
	
	public int size() {
		return pages.size();
	}

	public void goBackToFirst() {
		while(!pages.isEmpty()) {
			PageContent currentPage = pages.pop();
			History.back();
			if (pages.isEmpty()) {
				pages.push(currentPage);
				SpMobil.mainPage.setContent(currentPage);
				break;
			} else {
				currentPage.stop();				
			}
		}	
	}
	
	public void clear() {
		while(!pages.isEmpty()) {
			PageContent currentPage = pages.pop();
			currentPage.stop();			
		}		
	}
	
	public boolean isVisible(PageContent page) {
		PageContent currentPage = pages.peek();		
		return (currentPage == page);
	}
	
	public boolean back(String token) {	
		boolean back = false;
		if (token.isEmpty()) {			
			// prevent exit
			History.newItem(firstToken);			
		} else {
			back = isBackAction(token);
			if (back) {			
				PageContent page = pages.pop();
				page.stop();		
				page = pages.peek();
				SpMobil.mainPage.setContent(page);			
			}
		}		
		return back;
	}	
	
	private boolean isBackAction(String token) {
		if (!pages.isEmpty()) {
			if (String.valueOf(pages.peek().hashCode()).equals(token)) {
				return false;
			} else {
				Iterator<PageContent> iPages = pages.iterator();
				while (iPages.hasNext()) {
					PageContent page = (PageContent) iPages.next();
					if (String.valueOf(page.hashCode()).equals(token)) {
						return true;
					}
				}			
			}
		}
		return false;
	}
}
