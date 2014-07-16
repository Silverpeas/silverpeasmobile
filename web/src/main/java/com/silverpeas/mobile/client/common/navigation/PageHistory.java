package com.silverpeas.mobile.client.common.navigation;

import java.util.Stack;

import com.google.gwt.user.client.Window;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.components.base.PageContent;

public class PageHistory {
	
	private static PageHistory instance = null;
	
	private Stack<PageContent> pages = new Stack<PageContent>();
	
	public static PageHistory getInstance() {
		if (instance == null) {
			instance = new PageHistory();
		}
		return instance;
	}
	
	public void goTo(PageContent page) {		
		pages.push(page);
		SpMobil.mainPage.setContent(page);		
		//TODO : TODO : css3 transition
	}
	
	public void goBack(PageContent page) {
		
		while(!pages.isEmpty()) {
			PageContent currentPage = pages.pop();			
			if (currentPage.equals(page)) {
				pages.push(currentPage);
				SpMobil.mainPage.setContent(page);
				break;
			}
		}
		//TODO : TODO : css3 transition
	}
	
	public PageContent back() {		
		PageContent page = pages.pop();
		page = pages.peek();
		SpMobil.mainPage.setContent(page);
		//TODO : css3 transition
		
		return page;
	}
	
	public PageContent getCurrent() {
		return pages.peek();
	}
	
	public int size() {
		return pages.size();
	}

	public void goToFirst() {
		while(!pages.isEmpty()) {
			PageContent currentPage = pages.pop();			
			if (pages.isEmpty()) {
				pages.push(currentPage);
				SpMobil.mainPage.setContent(currentPage);
				break;
			}
		}	
	}
	
	public void clear() {
		pages.clear();
	}
	
	public boolean isVisible(PageContent page) {
		PageContent currentPage = pages.peek();
		//Window.alert(page.toString() + " == " + currentPage.toString() + (currentPage == page));
		
		
		return (currentPage == page);
	}
}
