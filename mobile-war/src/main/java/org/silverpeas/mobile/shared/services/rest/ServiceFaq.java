/*
 * Copyright (C) 2000 - 2026 Silverpeas
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

package org.silverpeas.mobile.shared.services.rest;

import elemental2.core.Global;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.dto.faq.CategoryDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDetailDTO;

import java.util.List;

/**
 * @author svu
 */
public class ServiceFaq extends AbstractService {

    private static final String PATH = "/silverpeas/services/mobile/faq";

    public void getAllQuestions(String appId, RestCallback<List<QuestionDTO>> callback) {
        String url = PATH + "/" + encode(appId) + "/question/all";

        get(
                url,
                result -> mapArray(result, QuestionDTO::fromJSON),
                callback
        );
    }

    public void getAllCategories(String appId, RestCallback<List<CategoryDTO>> callback) {
        String url = PATH + "/" + encode(appId) + "/category/all";
        get(
                url,
                result -> mapArray(result, CategoryDTO::fromJSON),
                callback
        );
    }

    public void createQuestion(String appId, QuestionDetailDTO question, RestCallback<QuestionDetailDTO> callback) {
        String url = PATH + "/" + encode(appId) + "/question";
        post(
                url,
                Global.JSON.stringify(Js.asAny(question.toJSON())),
                result -> QuestionDetailDTO.fromJSON((JsPropertyMap<Object>) result),
                callback
        );
    }
}
