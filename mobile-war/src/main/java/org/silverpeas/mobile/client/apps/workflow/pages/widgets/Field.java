/*
 * Copyright (C) 2000 - 2025 Silverpeas
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.apps.workflow.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.components.attachments.AttachmentsManager;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.workflow.FieldPresentationDTO;

public class Field extends Composite {

  private static FieldUiBinder uiBinder = GWT.create(FieldUiBinder.class);
  private FieldPresentationDTO data;

  @UiField
  HTML label;
  @UiField
  Anchor link;
  @UiField
  HTMLPanel container;
  protected ApplicationMessages msg = null;

  interface FieldUiBinder extends UiBinder<Widget, Field> {}

  public Field() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
    link.setVisible(false);
  }

  public void setData(FieldPresentationDTO data) {
    this.data = data;
    String value = "";
    if (data.getValue() != null) {
      value = data.getValue();
    }

    if (data.getType().equalsIgnoreCase("file")) {
      label.setText(data.getLabel() + " : ");
      link.setVisible(true);
      link.setText(value);
      label.getElement().getStyle().setDisplay(Style.Display.INLINE);
      AttachmentsManager
          .generateLink(data.getId(), data.getInstanceId(), SpMobil.getUser().getLanguage(),
              data.getValue(), link);
    } else {
      label.setHTML(data.getLabel() + " : " + value);
    }
  }
}
