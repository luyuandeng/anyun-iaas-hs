/*
 *
 *      AnyunCloudParamValidator.java
 *      Copyright (C) <2015-?>  <twitchgg@yahoo.com>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.anyun.common.jbi.route.param.validator;

import com.anyun.cloud.tools.db.AbstractEntity;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.exception.ApiValidationException;

import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/21/16
 */
public abstract class AnyunCloudParamValidator <T extends AbstractEntity>{
    protected String bodyString;
    protected Map<String,Object> headers;

    public abstract void validation() throws ApiValidationException;

    public String getBodyString() {
        return bodyString;
    }

    public void setBodyString(String bodyString) {
        this.bodyString = bodyString;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    protected ApiValidationException buildNullException(String paramName) {
        ApiValidationException exception = new ApiValidationException(1001);
        exception.setMessage("Parameter [" + paramName + "] is not set");
        exception.setUserMessage("参数[" + paramName + "]未设置,不能为空");
        exception.setUserTitle("Parameter is not set");
        return exception;
    }
}
