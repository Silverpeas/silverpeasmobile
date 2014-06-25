package com.silverpeas.mobile.client.common.navigation;

import java.util.Stack;

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
		
		//TODO : TODO : css3 transition
	}
	
	public void goBack(PageContent page) {
		
		while(pages.isEmpty()) {
			PageContent currentPage = pages.pop();
			if (currentPage.equals(page)) {
				break;
			}
		}
		
		//TODO : TODO : css3 transition
	}
	
	public PageContent back() {
		PageContent page = pages.pop();
		
		//TODO : css3 transition
				
		return page;
	}
	
	public PageContent getCurrent() {
		return pages.peek();
	}
}
