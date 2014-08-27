package com.silverpeas.mobile.client.apps.media.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GalleryRemoteBrowser extends PageContent implements View {

	private static GalleryRemoteBrowserUiBinder uiBinder = GWT.create(GalleryRemoteBrowserUiBinder.class);
	private String galleryId;
	private String albumId;
	private List<PhotoDTO> photos = null;
	private static double rowHeight = 0;
	private static int photoMargin = 5;	
	private boolean rendering = false;
	private ArrayList<ArrayList<Image>> grid = new ArrayList<ArrayList<Image>>();
	
	
	//@UiField(provided = true) protected GalleryResources ressources = null;
	
	@UiField AbsolutePanel content;
	
	public void setGalleryId(String galleryId) {
		this.galleryId = galleryId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	interface GalleryRemoteBrowserUiBinder extends
			UiBinder<Widget, GalleryRemoteBrowser> {
	}

	public GalleryRemoteBrowser() {
		//ressources = GWT.create(GalleryResources.class);
		//ressources.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
				
		Window.addResizeHandler(new ResizeHandler() {				
			@Override
			public void onResize(ResizeEvent event) {
				renderAlbum();				
			}
		});		
	}
	
	public void init() {
		//EventBus.getInstance().addHandler(AbstractRemotePicturesPageEvent.TYPE, this);
		//EventBus.getInstance().fireEvent(new RemotePicturesLoadEvent(galleryId, albumId));
	}


	public void onPicturesLoaded(/*RemotePictureLoadedEvent event*/) {
		//this.photos = event.getPhotos();
		renderAlbum();	
	}

	private void renderAlbum() {
		if (!rendering) {
			rendering = true;
			content.clear();
			if (photos.isEmpty()) {
				content.add(new Label("no pictures"));
			} else {
				content.setVisible(false);
				grid.clear();
				renderPhoto(photos.iterator(),0,0,0);		
			}
		}
	}
	
	private void renderPhoto(final Iterator<PhotoDTO> itPhotos, final int index, final int colPos, final int rowPos) {
		if(itPhotos.hasNext()) {			
				final PhotoDTO photo = itPhotos.next();			
				Image photoW = new Image(photo.getDataPhoto());
			
				photoW.addLoadHandler(new LoadHandler() {					
					@Override
					public void onLoad(LoadEvent event) {						
						Image photoW = (Image) event.getSource();
						int colPos2 = colPos;
						int rowPos2 = rowPos;
						
						if (index == 0) {
							rowHeight = photoW.getHeight();
						}						
						photoW.getElement().setId(photo.getId());
						photoW.getElement().getStyle().setDisplay(Display.BLOCK);
						photoW.getElement().getStyle().setPosition(Position.ABSOLUTE);
																							
						// Picture resize
						int w = photoW.getWidth();
						double h = photoW.getHeight();
						double r = rowHeight / h;
						long photoWidth = Math.round(r * w);				
						photoW.getElement().getStyle().setHeight(rowHeight, Unit.PX);	
						photoW.getElement().getStyle().setWidth(photoWidth, Unit.PX);
						
						// Picture position
						if (colPos2 + photoWidth > Window.getClientWidth()) {
							rowPos2 = rowPos2 + (int)Math.round(rowHeight) + photoMargin;
							colPos2 = 0;
							ArrayList<Image> rowGrid = new ArrayList<Image>();
							rowGrid.add(photoW);
							grid.add(rowGrid);
						} else {
							if (grid.size() > 0) {
								grid.get(grid.size()-1).add(photoW);
							} else {
								ArrayList<Image> rowGrid = new ArrayList<Image>();
								rowGrid.add(photoW);
								grid.add(rowGrid);
							}
						}
											
						photoW.getElement().getStyle().setTop(rowPos2, Unit.PX);
						photoW.getElement().getStyle().setLeft(colPos2, Unit.PX);
						colPos2 = colPos2 +  (int)photoWidth + photoMargin;
						
						content.setHeight(rowPos2+rowHeight+"px");
						
						// render next picture
						renderPhoto(itPhotos, index+1, colPos2, rowPos2);
					}
				});
				
				photoW.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(final ClickEvent event) {
						clickGesture(new Command() {							
							@Override
							public void execute() {
								PictureViewerPage viewer = new PictureViewerPage();
								viewer.init(galleryId, ((Image)event.getSource()).getElement().getId());
								viewer.show();
							}
						});
					}
				});	
									
				// display picture
				content.add(photoW);
		} else {
			rendering = false;
			
			// resize pictures to use entire screen
			/*int lastGrowHeight = 0;
			int r = 0;
			for (ArrayList<Image> row : grid) {				
				int spaceWidth = 0;
				for(Image img : row) {
					spaceWidth = spaceWidth + img.getWidth();
				}
				int growWidth = (Window.getClientWidth() - spaceWidth) / row.size();
				int c = 0;
				for(Image img : row) {
					int growHeight = (img.getWidth() / img.getHeight()) * growWidth;					
					//img.setWidth(img.getWidth() + growWidth + "px");					
					//img.setHeight(img.getHeight() + growHeight + "px");
					if (c > 0) {
						//img.getElement().getStyle().setLeft(Integer.parseInt(img.getElement().getStyle().getLeft().replace("px", "")) + growWidth * c, Unit.PX);						
					}
					if (r > 0) {
						//img.getElement().getStyle().setTop(Integer.parseInt(img.getElement().getStyle().getTop().replace("px", "")) + lastGrowHeight, Unit.PX);
					}			
					c++;
					lastGrowHeight = growHeight;
				}
				r++;
			}	*/		
			
			content.setVisible(true);
		}
	}
	


	@Override
	public void stop() {
		//EventBus.getInstance().removeHandler(AbstractRemotePicturesPageEvent.TYPE, this);
	}
}