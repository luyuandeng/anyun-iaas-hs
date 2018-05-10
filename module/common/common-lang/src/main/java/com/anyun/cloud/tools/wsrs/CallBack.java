/*
 *
 *      CallBack.java
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anyun.cloud.tools.wsrs;

import com.anyun.cloud.tools.execption.BaseServiceException;
import com.anyun.cloud.tools.execption.ServiceParamCheckException;
import com.anyun.cloud.tools.wsrs.param.AbstractParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author ray
 * @param <T>
 */
public abstract class CallBack<T> {
    private static Logger LOGGER = LoggerFactory.getLogger(CallBack.class);

    protected abstract T process(AbstractParam param) throws BaseServiceException;

    public <J> AbstractParam<J> getParam(Class<J> clazz, Object requestParam) throws ServiceParamCheckException {
        InputStream requestBody = null;
        String json = null;
        if (requestParam == null && clazz == null) {
            return null;
        } else {
            if (clazz == null) {
                if (requestParam instanceof AbstractParam) {
                    return (AbstractParam<J>) ((AbstractParam) requestParam).check();
                } else {
                    throw new ServiceParamCheckException(900, "不兼容的参数类型");
                }
            } else {
                if(requestParam instanceof String) {
                    json = String.valueOf(requestParam);
                } else if (!(requestParam instanceof InputStream)) {
                    throw new ServiceParamCheckException(900, "错误的参数类型");
                } else {
                    requestBody = (InputStream) requestParam;
                }
            }
        }
        if(json == null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                json = out.toString();
            } catch (Exception ex) {
                throw new ServiceParamCheckException(900, "JSON读取异常");
            }
        }
        LOGGER.debug("Http Api param json string [{}]",json);
        try {
            GsonBuilder gb = new GsonBuilder();
            gb.disableHtmlEscaping();
            Gson gson = gb.create();
            J _param = gson.fromJson(json, clazz);
            if (_param == null) {
                throw new ServiceParamCheckException(900, "JSON格式化异常");
            }
            if (_param instanceof AbstractParam) {
                AbstractParam<J> bsp = (AbstractParam<J>) _param;
                return (AbstractParam<J>) bsp.check();
            } else {
                throw new ServiceParamCheckException(900, "JSON对象初始化异常");
            }
        } catch (JsonSyntaxException | ServiceParamCheckException ex) {
            if (ex instanceof ServiceParamCheckException) {
                throw (ServiceParamCheckException) ex;
            } else {
                throw new ServiceParamCheckException(900, "JSON格式化异常");
            }
        }
    }
}
