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

package org.silverpeas.mobile.client.apps.survey.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.survey.AnswerDTO;
import org.silverpeas.mobile.shared.dto.survey.QuestionDTO;
import org.silverpeas.mobile.shared.dto.survey.ResponseDTO;

public class QuestionItem extends Composite {

  private QuestionDTO data;
  private static QuestionItemUiBinder uiBinder = GWT.create(QuestionItemUiBinder.class);
  @UiField
  HTML label;

  @UiField
  HTMLPanel field;

  @UiField
  HTMLPanel container;
  protected ApplicationMessages msg = null;

  interface QuestionItemUiBinder extends UiBinder<Widget, QuestionItem> {}

  public QuestionItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(QuestionDTO data) {
    this.data = data;
    label.setText(data.getLabel());

    if (data.getType().equalsIgnoreCase("open")) {
      TextArea text = new TextArea();
      text.getElement().setAttribute ("ref", data.getAnswers().get(0).getId());
      field.add(text);
    } else if (data.getType().equalsIgnoreCase("radio")) {
      for (AnswerDTO answer : data.getAnswers()) {
        RadioButton r = new RadioButton(data.getId(), answer.getLabel());
        r.getElement().setAttribute("ref", answer.getId());
        field.add(r);
        if (answer.getImage() != null) {
          Image img = new Image();
          img.setStyleName("image");
          img.setUrl(answer.getImage());
          field.add(img);
        }
      }
    }  else if (data.getType().equalsIgnoreCase("checkbox")) {
      for (AnswerDTO answer : data.getAnswers()) {
        CheckBox cb = new CheckBox(answer.getLabel());
        cb.getElement().setAttribute("ref", answer.getId());
        field.add(cb);
        if (answer.getImage() != null) {
          Image img = new Image();
          img.setStyleName("image");
          img.setUrl(answer.getImage());
          field.add(img);
        }
      }
    }  else if (data.getType().equalsIgnoreCase("list")) {
      ListBox l = new ListBox();
      for (AnswerDTO answer : data.getAnswers()) {
        l.addItem(answer.getLabel(), answer.getId());
      }
      field.add(l);
    }
  }

  public QuestionDTO updateWithResponse() {
    for (int i = 0; i < field.getWidgetCount(); i++) {
      Widget w = field.getWidget(i);
      if (w instanceof TextArea) {
        TextArea f = (TextArea) w;
        ResponseDTO response = new ResponseDTO();
        response.setId(f.getElement().getAttribute("ref"));
        response.setContent(f.getText());
        data.getResponses().add(response);
      } else if (w instanceof ListBox) {
        ListBox f = (ListBox) w;
        ResponseDTO response = new ResponseDTO();
        response.setId(f.getSelectedValue());
        response.setContent("");
        data.getResponses().add(response);
      } else if(w instanceof RadioButton) {
        RadioButton f = (RadioButton) w;
        if (f.getValue()) {
          ResponseDTO response = new ResponseDTO();
          response.setId(f.getElement().getAttribute("ref"));
          response.setContent("");
          data.getResponses().add(response);
        }
      } else if(w instanceof CheckBox) {
        CheckBox f = (CheckBox) w;
        if (f.getValue()) {
          ResponseDTO response = new ResponseDTO();
          response.setId(f.getElement().getAttribute("ref"));
          response.setContent("");
          data.getResponses().add(response);
        }
      }
    }
     return data;
  }
}
