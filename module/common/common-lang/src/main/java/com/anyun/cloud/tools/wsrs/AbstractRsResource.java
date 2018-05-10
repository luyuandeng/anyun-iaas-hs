/*
 *
 *      AbstractRsResource.java
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

import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.db.BaseEntity;
import com.anyun.cloud.tools.execption.BaseServiceException;
import com.anyun.cloud.tools.execption.ResourceAuthenticationException;
import com.anyun.cloud.tools.execption.ServiceParamCheckException;
import com.anyun.cloud.tools.wsrs.param.AbstractParam;
import com.anyun.cloud.tools.wsrs.param.BaseServiceParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TwitchGG
 * @version 1.0
 * @date 2015-4-26
 */
public class AbstractRsResource implements WsrsResource {
    private static Logger LOGGER = LoggerFactory.getLogger(AbstractRsResource.class);
    @Context
    private HttpHeaders headers;
    @Context
    MessageContext jaxrsContext;
    private ResourceAuthenticationManagement resourceAuthenticationManagement;
    private List<Object> services = new ArrayList<>();

    public AbstractRsResource() {
        resourceAuthenticationManagement = ResourceAuthenticationManagement.getManagement();
    }

    protected Response getJson(Object entity) {
        if (entity instanceof BaseEntity) {
            return Response.ok(((BaseEntity) entity).asJson())
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } else if (entity instanceof String) {
            return Response.ok(entity.toString())
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } else {
            GsonBuilder gb = new GsonBuilder();
            gb.disableHtmlEscaping();
            Gson gson = gb.create();
            String json = gson.toJson(entity);
            return Response.ok(json)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }

    protected Response getOk() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    protected Response getError(ErrorMessage error) {
        GsonBuilder gb = new GsonBuilder();
        gb.disableHtmlEscaping();
        Gson gson = gb.create();
        String message = gson.toJson(error);
        return Response.ok(message, MediaType.APPLICATION_JSON)
                .header("X-Application-Error-Code", error.getCode())
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    protected Response getError(int code, String message) {
        return getError(new ErrorMessage(code, message));
    }

    protected Response getErrors(int code, List<ErrorMessage> errors) {
        if (code == 0) {
            code = 500;
        }
        GsonBuilder gb = new GsonBuilder();
        gb.disableHtmlEscaping();
        Gson gson = gb.create();
        String message = gson.toJson(errors);
        return Response.ok(message, MediaType.APPLICATION_JSON)
                .header("X-Application-Error-Code", code)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    protected String getResuestBody(InputStream requestBody) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        String result = out.toString();
        if (StringUtils.isEmpty(result))
            throw new Exception("requestBody is not set");
        return out.toString();
    }

    protected <T> T getJsonBody(Class<T> clazz, InputStream requestBody) throws ServiceParamCheckException {
        String json = null;
        try {
            json = getResuestBody(requestBody);
        } catch (Exception ex) {
            throw new ServiceParamCheckException(900, "JSON读取异常");
        }
        return getJsonObject(clazz, json);
    }

    protected <T> T getJsonObject(Class<T> clazz, String json) throws ServiceParamCheckException {
        try {
            GsonBuilder gb = new GsonBuilder();
            gb.disableHtmlEscaping();
            Gson gson = gb.create();
            T param = gson.fromJson(json, clazz);
            if (param == null) {
                throw new ServiceParamCheckException(900, "JSON格式化异常");
            }
            if (param instanceof BaseServiceParam) {
                BaseServiceParam<T> bsp = (BaseServiceParam) param;
                return bsp.check();
            } else {
                return param;
            }
        } catch (JsonSyntaxException | ServiceParamCheckException ex) {
            if (ex instanceof ServiceParamCheckException) {
                throw (ServiceParamCheckException) ex;
            } else {
                throw new ServiceParamCheckException(900, "JSON格式化异常");
            }
        }
    }

    protected void saveAttachment(Attachment attachment, String fileName) throws IOException {
        java.nio.file.Path path = Paths.get(fileName);
        Files.deleteIfExists(path);
        InputStream in;
        in = attachment.getObject(InputStream.class);
        Files.copy(in, path);
        in.close();
    }

    public void addService(Object service) {
        this.services.add(service);
    }

    protected final <T> Response call(Class<? extends AbstractParam> clazz, Object requestBody, CallBack<T> callBack) {
        try {
            T value = callBack.process(callBack.getParam(clazz, requestBody));
            return getJson(value);
        } catch (ServiceParamCheckException | BaseServiceException ex) {
            return getError(ex.getCode(), ex.getMessage());
        }
    }

    public String getClientIpAddr() {
        String ip = headers.getHeaderString("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getHeaderString("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getHeaderString("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getHeaderString("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getHeaderString("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = jaxrsContext.getHttpServletRequest().getRemoteAddr();
        }
        return ip;
    }

    protected <T> T findService(Class<T> serviceType) throws BaseServiceException{
        Object service = null;
        for (Object _service : services) {
            if (_service.getClass().getName().equals(serviceType.getName()))
                service = _service;
        }
        if (service == null)
            throw new BaseServiceException(507, "Service [" + serviceType + "] is not define");
        return (T)service;
    }

    protected void checkApiResource(AbstractParam param) throws BaseServiceException{
        String remoteIp = getClientIpAddr();
        String apiPath = "/" + jaxrsContext.getUriInfo().getPath();
        LOGGER.debug("Remote client ip [{}],API URL path --> {}", remoteIp, apiPath);
        List<String> sessionTickList = headers.getRequestHeader("anyuncloud.user.session.tick");
        if (sessionTickList != null && sessionTickList.size() == 1) {
            String sessionTick = sessionTickList.get(0).replace("\"", "");
            UserSessionManagement.getSessionManagement().setUserSessionTick(sessionTick);
        }
        String _tick = UserSessionManagement.getSessionManagement().getUserSessionTick();
        if (StringUtils.isNotEmpty(_tick)) {
            _tick = _tick.replace("\"", "");
        }
        LOGGER.debug("Current user session tick [{}]", _tick);
        try {
            resourceAuthenticationManagement.checkApiResource(remoteIp, apiPath, _tick);
        } catch (ResourceAuthenticationException aex) {
            throw new BaseServiceException(505, aex);
        }
        if (param != null) {
            param.setSessionTick(_tick);
        }
    }

    public <T> Response call(Class<?> serviceType, final String method, final Object requestBody,
                             final Class<? extends AbstractParam> paramClass, Class<T> valueClass) {
        final Object finalService = findService(serviceType);
        return call(paramClass, requestBody, new CallBack<T>() {
            @Override
            protected T process(AbstractParam param) throws BaseServiceException {
                checkApiResource(param);
                Method m;
                try {
                    if (requestBody == null) {
                        m = finalService.getClass().getMethod(method);
                        LOGGER.debug("Invoke service [{}] method [{}]", finalService.getClass().getName(), m.getName());
                        return (T) m.invoke(finalService);
                    } else {
                        if (requestBody instanceof InputStream || requestBody instanceof String) {
                            m = finalService.getClass().getMethod(method, paramClass);
                        } else {
                            m = finalService.getClass().getMethod(method, requestBody.getClass());
                        }
                        LOGGER.debug("Invoke service [{}] method [{}]", finalService.getClass().getName(), m.getName());
                        return (T) m.invoke(finalService, param);
                    }
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e) {
                    if (e instanceof InvocationTargetException) {
                        InvocationTargetException ex = (InvocationTargetException) e;
                        Throwable sex = ex.getTargetException();
                        sex.printStackTrace();
                        if (sex instanceof BaseServiceException)
                            throw (BaseServiceException) sex;
                        else
                            throw new BaseServiceException(502, sex);
                    } else {
                        e.printStackTrace();
                        throw new BaseServiceException(501, "找不到服务实现方法 " + method);
                    }
                }
            }
        });
    }
}
