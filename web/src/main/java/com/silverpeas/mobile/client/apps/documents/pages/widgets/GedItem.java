package com.silverpeas.mobile.client.apps.documents.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemClickEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;

public class GedItem extends Composite {

  private Object data;
  private TopicDTO dataTopic;
  private PublicationDTO dataPublication;
  private static GedItemUiBinder uiBinder = GWT.create(GedItemUiBinder.class);
  @UiField Anchor link;
  protected ApplicationMessages msg = null;


  interface GedItemUiBinder extends UiBinder<Widget, GedItem> {
  }

  public GedItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(Object data) {
    this.data = data;
    if (data instanceof TopicDTO) {
      dataTopic = (TopicDTO) data;
      if (dataTopic.getId().equals("1")) {
        setStyleName("trash");
        link.setText(dataTopic.getName());
      } else {
        setStyleName("folder-ged");
        link.setText(dataTopic.getName() + " (" + dataTopic.getPubCount() + ")");
      }
    } else if (data instanceof PublicationDTO) {
      dataPublication = (PublicationDTO) data;
      link.setText(dataPublication.getName());
      setStyleName("publication");
    }
    link.setStyleName("ui-btn ui-btn-icon-right ui-icon-carat-r");
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    EventBus.getInstance().fireEvent(new GedItemClickEvent(data));
  }
}
