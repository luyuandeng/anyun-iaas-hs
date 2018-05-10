/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.esb.component.api.jaxrs.response;

import com.anyun.cloud.api.ProcessTime;
import com.anyun.cloud.api.Response;
import com.anyun.cloud.api.Successed;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.JbiCommon;
import com.anyun.esb.component.api.jaxrs.RestMethodDefine;
import com.anyun.exception.JbiComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Produces;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/20/16
 */
public class JsonResponseBuilder implements ResponseBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonResponseBuilder.class);

    @Override
    public String build(RestMethodDefine restMethodDefine, long startTime, Response response) throws JbiComponentException {
        try {
            Successed successed = new Successed();
            successed.setSuccess(response);
            successed.setOperate(restMethodDefine.operate());
            String json = buildSuccessedJson(successed, startTime);
            LOGGER.debug("Build response json [{}]",json);
            return json;
        } catch (Exception ex) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("json response build error [" + ex.getMessage() + "]");
            exception.setUserMessage("返回的JSON构建失败");
            exception.setUserTitle("JSON构建失败");
            throw exception;
        }
    }

    private String buildSuccessedJson(Successed successedObject, long startTime) {
        String returnFromat = null;
        Object response = successedObject.getSuccess().getContent();
        if (StringUtils.isEmpty(successedObject.getSuccess().getType())) {
            successedObject.getSuccess().setType(response.getClass().getSimpleName());
        }
        ProcessTime processTime = new ProcessTime();
        processTime.setTimeUnit(ProcessTime.TimeUnit.MILLISECOND);
        processTime.setTime(System.currentTimeMillis() - startTime);
        successedObject.setProcessTime(processTime);
        returnFromat = JbiCommon.toJson(successedObject);
        return returnFromat;
    }


    @Override
    public Class<?> getResponseType() {
        return String.class;
    }

    @Override
    public boolean match(Produces produces) {
        for (String produce : produces.value()) {
            if (produce.equals("application/json") || produce.equals("json")) {
                return true;
            }
        }
        return false;
    }
}
