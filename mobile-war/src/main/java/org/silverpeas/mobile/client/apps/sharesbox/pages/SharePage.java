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

package org.silverpeas.mobile.client.apps.sharesbox.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.sharesbox.resources.ShareMessages;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import java.util.Date;

public class SharePage extends PageContent {

  private static SharesBoxPageUiBinder uiBinder = GWT.create(SharesBoxPageUiBinder.class);

  @UiField(provided = true) protected ShareMessages msg = null;
  private TicketDTO data;

  @UiField
  HTMLPanel container;

  public void setData(TicketDTO data) {
    this.data = data;
    setPageTitle(msg.shareOf() + " " + data.getName());

    String rowTemplate = "<div class='field-share'><span>1</span><span>2</span></div>";

    String html = "<div class='header-share'>" + rowTemplate.replace("1", msg.name() + " : ").replace("2", data.getName());
    Date d = new Date();
    d.setTime(Long.parseLong(data.getCreationDate().trim()));
    DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy");
    html += rowTemplate.replace("1", msg.creationDate() + " : ").replace("2", fmt.format(d));
    html += "</div>";

    html += "<div class='field-infos-share'>";
    if (data.getNbAccessMax().equalsIgnoreCase("-1")) {
      html += rowTemplate.replace("1", msg.nbAccess() + " : ").replace("2", data.getNbAccess());
    } else {
      html += rowTemplate.replace("1", msg.nbAccess() + " : ").replace("2", data.getNbAccess() + " / " + data.getNbAccessMax());
    }
    if (data.getValid().equalsIgnoreCase("true")) {
      html += rowTemplate.replace("1", msg.active() + " : ").replace("2", "<input type='checkbox' disabled checked/>");
    } else {
      html += rowTemplate.replace("1", msg.active() + " : ").replace("2", "<input type='checkbox' disabled />");
    }
    html += "</div>";

    html += "<div class='field-link-share'>";
    String link = NetworkHelper.getHostname() + data.getUri();
    html += rowTemplate.replace("1", msg.link() + " : <br>").replace("2", "<a href='" + link + "'>" + link + "</a>");
    html += "</div>";

    container.getElement().setInnerHTML(html);

  }

  interface SharesBoxPageUiBinder extends UiBinder<Widget, SharePage> {
  }

  public SharePage() {
    msg = GWT.create(ShareMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void stop() {
    super.stop();
  }
}